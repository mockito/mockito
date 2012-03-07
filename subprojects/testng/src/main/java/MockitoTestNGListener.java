package org.mockito.testng;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Mockito TestNG Listener, this listener adds the following behavior to your test :
 * <ul>
 *   <li>
 *      Initializes mocks annotated with {@link Mock},
 *      so that <strong>explicit usage of {@link org.mockito.MockitoAnnotations#initMocks(Object)} is not necessary</strong>.
 *      Mocks are initialized before each test method.
 *   <li>
 *      validates framework usage after each test method. See javadoc for {@link Mockito#validateMockitoUsage()}.
 * </ul>
 *
 * Runner is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * Explicitly validating framework usage is also optional because it is triggered automatically by Mockito every time you use the framework.
 * See javadoc for {@link Mockito#validateMockitoUsage()}.
 * <p>
 * Read more about &#064;Mock annotation in javadoc for {@link MockitoAnnotations}
 * <pre class="code"><code class="java">
 * <b>&#064;Listeners(org.mockito.testng.MockitoTestNGListener.class)</b>
 * public class ExampleTest {
 *
 *     &#064;Mock
 *     private List list;
 *
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * </code></pre>
 */
public class MockitoTestNGListener implements ITestListener, IInvokedMethodListener {

    /**
     * Init fields with annotated fields for instances that are annotated with the TestNG listener
     * {@link MockitoTestNGListener}.
     *
     * @param context TestContext
     */
    public void onStart(ITestContext context) {
        if(context.getAllTestMethods() == null) {
            return;
        }

        for (Object instance : allTestInstancesWithMockitoListener(context.getAllTestMethods())) {
            MockitoAnnotations.initMocks(instance);
        }
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) { }


    /**
     * Validate Mockito's state then reset the mocks.
     *
     * @param method The test method.
     * @param testResult TestNG result.
     */
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        resetMocks(testResult.getInstance());
        Mockito.validateMockitoUsage();
    }

    private void resetMocks(Object instance) {
        Mockito.reset(instanceMocksOf(instance).toArray());
    }

    private Set<Object> instanceMocksOf(Object instance) {
        Set<Object> instanceMocks = new HashSet<Object>();
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if(declaredField.isAnnotationPresent(Mock.class) || declaredField.isAnnotationPresent(Spy.class)) {
                declaredField.setAccessible(true);
                try {
                    Object fieldValue = declaredField.get(instance);
                    if (fieldValue != null) {
                        instanceMocks.add(fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Could not access field " + declaredField.getName());
                }
            }
        }
        return instanceMocks;
    }

    private Set<Object> allTestInstancesWithMockitoListener(ITestNGMethod[] testMethods) {
        Set<Object> testInstancesWithMockitoListener = new HashSet<Object>();
        for (Object testInstance : allTestInstances(testMethods)) {
            if(hasMockitoTestNGListener(testInstance)) {
                testInstancesWithMockitoListener.add(testInstance);
            }
        }
        return testInstancesWithMockitoListener;
    }

    private boolean hasMockitoTestNGListener(Object testInstance) {
        Listeners listeners = testInstance.getClass().getAnnotation(Listeners.class);
        if (listeners == null) {
            return false;
        }

        for (Class<? extends ITestNGListener> listenerClass : listeners.value()) {
            if (MockitoTestNGListener.class == listenerClass) {
                return true;
            }
        }
        return false;
    }

    private Set<Object> allTestInstances(ITestNGMethod[] testMethods) {
        Set<Object> allTestInstances = new HashSet<Object>();
        for (ITestNGMethod testMethod : testMethods) {
            allTestInstances.add(testMethod.getInstance());
        }
        return allTestInstances;
    }

    public void onTestStart(ITestResult result) { }
    public void onTestSuccess(ITestResult result) { }
    public void onTestFailure(ITestResult result) { }
    public void onTestSkipped(ITestResult result) { }
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    public void onFinish(ITestContext context) { }
}