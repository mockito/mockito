/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.internal.runners.*;
import org.junit.runner.*;
import org.junit.runner.manipulation.*;
import org.junit.runner.notification.*;

/**
 * This class is nearly the same as JUnit4ClassRunner (Junit 4.4) that has been made deprecated at JUnit 4.5 by the junit team. 
 * I need this implementation to provide consistent runner for previous versions of JUnit < 4.5
 * Unfortunately I cannot use JUnit4ClassRunner itself because it is not available in JUnit < 4.4
 */
@SuppressWarnings("all") // suppressing all warnings because it is not really our code.
public class LegacyJUnitRunner extends Runner {

    private final List<Method> fTestMethods;
    private TestClass fTestClass;
    private final TestCreationListener testStartedCallback;

    public LegacyJUnitRunner(Class<?> klass,
            TestCreationListener testStartedCallback)
            throws InitializationError {
        this.testStartedCallback = testStartedCallback;
        fTestClass = new TestClass(klass);
        fTestMethods = getTestMethods();
        validate();
    }

    protected List<Method> getTestMethods() {
        return fTestClass.getTestMethods();
    }

    protected void validate() throws InitializationError {
        MethodValidator methodValidator = new MethodValidator(fTestClass);
        methodValidator.validateMethodsForDefaultRunner();
        methodValidator.assertValid();
    }

    @Override
    public void run(final RunNotifier notifier) {
        new ClassRoadie(notifier, fTestClass, getDescription(), new Runnable() {
            public void run() {
                runMethods(notifier);
            }
        }).runProtected();
    }

    protected void runMethods(final RunNotifier notifier) {
        for (Method method : fTestMethods) {
            invokeTestMethod(method, notifier);
        }
    }

    @Override
    public Description getDescription() {
        Description spec = Description.createSuiteDescription(getName(),
                classAnnotations());
        List<Method> testMethods = fTestMethods;
        for (Method method : testMethods) {
            spec.addChild(methodDescription(method));
        }
        return spec;
    }

    protected Annotation[] classAnnotations() {
        return fTestClass.getJavaClass().getAnnotations();
    }

    protected String getName() {
        return getTestClass().getName();
    }

    protected Object createTest() throws Exception {
        Object test = getTestClass().getConstructor().newInstance();

        testStartedCallback.testCreated(test);

        return test;
    }

    protected void invokeTestMethod(Method method, RunNotifier notifier) {
        Description description = methodDescription(method);
        Object test;
        try {
            test = createTest();
        } catch (InvocationTargetException e) {
            testAborted(notifier, description, e.getCause());
            return;
        } catch (Exception e) {
            testAborted(notifier, description, e);
            return;
        }
        TestMethod testMethod = wrapMethod(method);
        new MethodRoadie(test, testMethod, notifier, description).run();
    }

    private void testAborted(RunNotifier notifier, Description description,
            Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }

    protected TestMethod wrapMethod(Method method) {
        return new TestMethod(method, fTestClass);
    }

    protected String testName(Method method) {
        return method.getName();
    }

    protected Description methodDescription(Method method) {
        return Description.createTestDescription(getTestClass().getJavaClass(),
                testName(method), testAnnotations(method));
    }

    protected Annotation[] testAnnotations(Method method) {
        return method.getAnnotations();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        for (Iterator<Method> iter = fTestMethods.iterator(); iter.hasNext();) {
            Method method = iter.next();
            if (!filter.shouldRun(methodDescription(method))) {
                iter.remove();
            }
        }
        if (fTestMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    public void sort(final Sorter sorter) {
        Collections.sort(fTestMethods, new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                return sorter.compare(methodDescription(o1),
                        methodDescription(o2));
            }
        });
    }

    protected TestClass getTestClass() {
        return fTestClass;
    }
}