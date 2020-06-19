/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import java.lang.reflect.InvocationTargetException;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.StrictRunner;
import org.mockito.quality.MockitoHint;
import org.mockito.quality.Strictness;

/**
 * Mockito JUnit Runner keeps tests clean and improves debugging experience.
 * Make sure to try out {@link MockitoJUnitRunner.StrictStubs} which automatically
 * detects <strong>stubbing argument mismatches</strong> and is planned to be the default in Mockito v3.
 * Runner is compatible with JUnit 4.4 and higher and adds following behavior:
 * <ul>
 *   <li>
 *       (new since Mockito 2.1.0) Detects unused stubs in the test code.
 *       See {@link UnnecessaryStubbingException}.
 *       Similar to JUnit rules, the runner also reports stubbing argument mismatches as console warnings
 *       (see {@link MockitoHint}).
 *       To opt-out from this feature, use {@code}&#064;RunWith(MockitoJUnitRunner.Silent.class){@code}
 *   <li>
 *      Initializes mocks annotated with {@link Mock},
 *      so that explicit usage of {@link MockitoAnnotations#openMocks(Object)} is not necessary.
 *      Mocks are initialized before each test method.
 *   <li>
 *      Validates framework usage after each test method. See javadoc for {@link Mockito#validateMockitoUsage()}.
 *   <li>
 *      It is highly recommended to use {@link MockitoJUnitRunner.StrictStubs} variant of the runner.
 *      It drives cleaner tests and improves debugging experience.
 *      The only reason this feature is not turned on by default
 *      is because it would have been an incompatible change
 *      and Mockito strictly follows <a href="http://semver.org">semantic versioning</a>.
 * </ul>
 *
 * Runner is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * Explicitly validating framework usage is also optional because it is triggered automatically by Mockito every time you use the framework.
 * See javadoc for {@link Mockito#validateMockitoUsage()}.
 * <p>
 * Read more about &#064;Mock annotation in javadoc for {@link MockitoAnnotations}
 * <pre class="code"><code class="java">
 * <b>&#064;RunWith(MockitoJUnitRunner.StrictStubs.class)</b>
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
 *
 * If you would like to take advantage of Mockito JUnit runner features
 * but you cannot use the runner because, for example, you use TestNG, there is a solution!
 * {@link MockitoSession} API is intended to offer cleaner tests and improved debuggability
 * to users that cannot use Mockito's built-in JUnit support (runner or the rule).
 */
public class MockitoJUnitRunner extends Runner implements Filterable {

    /**
     * This Mockito JUnit Runner implementation *ignores*
     * stubbing argument mismatches ({@link MockitoJUnitRunner.StrictStubs})
     * and *does not detect* unused stubbings.
     * The runner remains 'silent' even if incorrect stubbing is present.
     * It is an equivalent of {@link Strictness#LENIENT}.
     * This was the behavior of Mockito JUnit runner in versions 1.x.
     * Using this implementation of the runner is not recommended.
     * Engineers should care for removing unused stubbings because they are dead code,
     * they add unnecessary details, potentially making the test code harder to comprehend.
     * If you have good reasons to use the silent runner, let us know at the mailing list
     * or raise an issue in our issue tracker.
     * The purpose of silent implementation is to satisfy edge/unanticipated use cases,
     * and to offer users an opt-out.
     * Mockito framework is opinionated to drive clean tests but it is not dogmatic.
     * <p>
     * See also {@link UnnecessaryStubbingException}.
     * <p>
     * Usage:
     * <pre class="code"><code class="java">
     * <b>&#064;RunWith(MockitoJUnitRunner.Silent.class)</b>
     * public class ExampleTest {
     *   // ...
     * }
     * </code></pre>
     *
     * @since 2.1.0
     */
    public static class Silent extends MockitoJUnitRunner {
        public Silent(Class<?> klass) throws InvocationTargetException {
            super(new RunnerFactory().create(klass));
        }
    }

    /**
     * Detects unused stubs and reports them as failures. Default behavior in Mockito 2.x.
     * To improve productivity and quality of tests please consider newer API, the {@link StrictStubs}.
     * <p>
     * For more information on detecting unusued stubs, see {@link UnnecessaryStubbingException}.
     * For more information on stubbing argument mismatch warnings see {@link MockitoHint}.
     *
     * @since 2.1.0
     */
    public static class Strict extends MockitoJUnitRunner {
        public Strict(Class<?> klass) throws InvocationTargetException {
            super(new StrictRunner(new RunnerFactory().createStrict(klass), klass));
        }
    }

    /**
     * Improves debugging tests, helps keeping the tests clean.
     * Highly recommended to improve productivity and quality of tests.
     * Planned default behavior for Mockito v3.
     * Adds behavior equivalent to {@link Strictness#STRICT_STUBS}.
     * <p>
     * Usage:
     * <pre class="code"><code class="java">
     * <b>&#064;RunWith(MockitoJUnitRunner.StrictStubs.class)</b>
     * public class ExampleTest {
     *   // ...
     * }
     * </code></pre>
     *
     * @since 2.5.1
     */
    public static class StrictStubs extends MockitoJUnitRunner {
        public StrictStubs(Class<?> klass) throws InvocationTargetException {
            super(new StrictRunner(new RunnerFactory().createStrictStubs(klass), klass));
        }
    }

    private final InternalRunner runner;

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        // by default, StrictRunner is used. We can change that potentially based on feedback from
        // users
        this(new StrictRunner(new RunnerFactory().createStrict(klass), klass));
    }

    MockitoJUnitRunner(InternalRunner runner) throws InvocationTargetException {
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
        // filter is required because without it UnrootedTests show up in Eclipse
        runner.filter(filter);
    }
}
