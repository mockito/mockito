package org.mockito.testng;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;

/**
 * Mockito TestNG Listener, this listener adds the following behavior to your test :
 * <ul>
 *     <li>
 *         Initializes mocks annotated with {@link org.mockito.Mock}, so that <strong>explicit usage of
 *         {@link org.mockito.MockitoAnnotations#initMocks(Object)} is not necessary</strong>.
 *         <strong>Note :</strong> With TestNG, mocks are initialized before any TestNG method, either a <em>configuration
 *         method</em> (&#064;BeforeMethod, &#064;BeforeClass, etc) or a <em>test</em> method, i.e. mocks are initialized
 *         once only once for each test instance.
 *     </li>
 *     <li>
 *         As mocks are initialized only once, they will be reset after each <em>test method</em>.
 *         See javadoc {@link org.mockito.Mockito#reset(Object[])}
 *     </li>
 *     <li>
 *         Validates framework usage after each test method. See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 *     </li>
 * </ul>
 *
 * <p>
 * The listener is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * Explicitly validating framework usage is also optional because it is triggered automatically by Mockito every time you use the framework.
 * See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 *
 * <p>
 * Read more about &#064;Mock annotation in javadoc for {@link org.mockito.MockitoAnnotations}
 *
 * <pre class="code"><code class="java">
 * <b>&#064;Listeners(MockitoTestNGListener.class)</b>
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
public class MockitoTestNGListener implements IInvokedMethodListener {

    private final MockitoBeforeTestNGMethod beforeTest = new MockitoBeforeTestNGMethod();
    private final MockitoAfterTestNGMethod afterTest = new MockitoAfterTestNGMethod();


    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (hasMockitoTestNGListenerInTestHierarchy(testResult.getTestClass().getRealClass())) {
            beforeTest.applyFor(method, testResult);
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (hasMockitoTestNGListenerInTestHierarchy(testResult.getTestClass().getRealClass())) {
            afterTest.applyFor(method, testResult);
        }
    }

    protected boolean hasMockitoTestNGListenerInTestHierarchy(Class<?> testClass) {
        for (Class<?> clazz = testClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (hasMockitoTestNGListener(clazz)) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMockitoTestNGListener(Class<?> clazz) {
        Listeners listeners = clazz.getAnnotation(Listeners.class);
        if (listeners == null) {
            return false;
        }

        for (Class<? extends ITestNGListener> listenerClass : listeners.value()) {
            if (listenerClass() == listenerClass) {
                return true;
            }
        }
        return false;
    }

    protected Class<MockitoTestNGListener> listenerClass() {
        return MockitoTestNGListener.class;
    }

}
