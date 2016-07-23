/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.runners.StrictRunner;

import java.lang.reflect.InvocationTargetException;


/**
 * Compatible with <b>JUnit 4.4 and higher</b>, this runner adds following behavior:
 * <ul>
 *   <li>
 *       (new since Mockito 2.*) Detects unused stubs in the test code.
 *       See {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}.
 *       To opt-out from this feature, use {@code}&#064;RunWith(MockitoJUnitRunner.Silent.class){@code}
 *   <li>
 *      Initializes mocks annotated with {@link Mock},
 *      so that explicit usage of {@link MockitoAnnotations#initMocks(Object)} is not necessary. 
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
 * <b>&#064;RunWith(MockitoJUnitRunner.class)</b>
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
public class MockitoJUnitRunner extends Runner implements Filterable {

    /**
     * This Mockito JUnit Runner implementation ignores unused stubs
     * (e.g. it remains 'silent' even if unused stubs are present).
     * This was the behavior of Mockito JUnit runner in versions 1.*.
     * Using this implementation of the runner is not recommended.
     * Engineers should care for removing unused stubbings because they are dead code,
     * they add unnecessary details, potentially making the test code harder to comprehend.
     * If you have good reasons to use the silent runner, let us know at the mailing list
     * or raise an issue in our issue tracker.
     *
     * See also {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @since 2.*
     */
    public static class Silent extends MockitoJUnitRunner {
        public Silent(Class<?> klass) throws InvocationTargetException {
            super(new RunnerFactory().create(klass));
        }
    }

    /**
     * Detects unused stubs and reports them as failures. Default behavior.
     * See {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}
     *
     * @since 2.*
     */
    public static class Strict extends MockitoJUnitRunner {
        public Strict(Class<?> klass) throws InvocationTargetException {
            super(new StrictRunner(new RunnerFactory().create(klass), klass));
        }
    }

    private final RunnerImpl runner;

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        //by default, StrictRunner is used. We can change that potentially based on feedback from users
        this(new StrictRunner(new RunnerFactory().create(klass), klass));
    }

    MockitoJUnitRunner(RunnerImpl runner) throws InvocationTargetException {
        this.runner = runner;
    }

    @Override
    public void run(final RunNotifier notifier) {           
        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        //filter is required because without it UnrootedTests show up in Eclipse
        runner.filter(filter);
    }
}