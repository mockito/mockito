/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.listeners.MockitoListener;
import org.mockito.quality.MockitoHint;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;

/**
 * {@code MockitoSession} is an optional, highly recommended feature
 * that helps driving cleaner tests by eliminating boilerplate code and adding extra validation.
 * If you already use {@link MockitoJUnitRunner} or {@link MockitoRule}
 * *you don't need* {@code MockitoSession} because it is used by the runner/rule.
 * <p>
 * {@code MockitoSession} is a session of mocking, during which the user creates and uses Mockito mocks.
 * Typically the session is an execution of a single test method.
 * {@code MockitoSession} initializes mocks, validates usage and detects incorrect stubbing.
 * When the session is started it must be concluded with {@link #finishMocking()}
 * otherwise {@link UnfinishedMockingSessionException} is triggered when the next session is created.
 * <p>
 * {@code MockitoSession} is useful when you cannot use {@link MockitoJUnitRunner} or {@link MockitoRule}.
 * For example, you work with TestNG instead of JUnit.
 * Another example is when different JUnit runner is in use (Jukito, Springockito)
 * and it cannot be combined with Mockito's own runner.
 * <p>
 * Framework integrators are welcome to use {@code MockitoSession} and give us feedback by commenting on
 * <a href="https://github.com/mockito/mockito/issues/857">issue 857</a>.
 * <p>
 *
 * Example:
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *     &#064;Mock Foo foo;
 *
 *     //Keeping session object in a field so that we can complete session in 'tear down' method.
 *     //It is recommended to hide the session object, along with 'setup' and 'tear down' methods in a base class / runner.
 *     //Keep in mind that you can use Mockito's JUnit runner or rule instead of MockitoSession and get the same behavior.
 *     MockitoSession mockito;
 *
 *     &#064;Before public void setup() {
 *         //initialize session to start mocking
 *         mockito = Mockito.mockitoSession()
 *            .initMocks(this)
 *            .strictness(Strictness.STRICT_STUBS)
 *            .startMocking();
 *     }
 *
 *     &#064;After public void tearDown() {
 *         //It is necessary to finish the session so that Mockito
 *         // can detect incorrect stubbing and validate Mockito usage
 *         //'finishMocking()' is intended to be used in your test framework's 'tear down' method.
 *         mockito.finishMocking();
 *     }
 *
 *     // test methods ...
 * }
 * </code></pre>
 *
 * <p>
 * Why to use {@code MockitoSession}?
 * What's the difference between {@code MockitoSession}, {@link MockitoJUnitRunner}, {@link MockitoRule}
 * and traditional {@link MockitoAnnotations#openMocks(Object)}?
 * <p>
 * Great questions!
 * There is no need to use {@code MockitoSession} if you already use {@link MockitoJUnitRunner} or {@link MockitoRule}.
 * If you are JUnit user who does not leverage Mockito rule or runner we strongly recommend to do so.
 * Both the runner and the rule support strict stubbing which can really help driving cleaner tests.
 * See {@link MockitoJUnitRunner.StrictStubs} and {@link MockitoRule#strictness(Strictness)}.
 * If you cannot use Mockito's JUnit support (for example, you are on TestNG) {@code MockitoSession} exactly is for you!
 * You can automatically take advantage of strict stubbing ({@link Strictness}),
 * automatic initialization of annotated mocks ({@link MockitoAnnotations}),
 * and extra validation ({@link Mockito#validateMockitoUsage()}).
 * If you use Mockito annotations with {@link MockitoAnnotations#openMocks(Object)}
 * but not Mockito runner/rule please try out Mockito's JUnit support (runner or rule) or
 * start using {@code MockitoSession}. You'll get cleaner tests and better productivity.
 * <p>
 * Mockito team would really appreciate feedback about {@code MockitoSession} API.
 * Help us out by commenting at <a href="https://github.com/mockito/mockito/issues/857">issue 857</a>.
 *
 * @since 2.7.0
 */
@Incubating
@NotExtensible
public interface MockitoSession {

    /**
     * Changes the strictness of this {@code MockitoSession}.
     * The new strictness will be applied to operations on mocks and checks performed by {@link #finishMocking()}.
     * This method is used behind the hood by {@link MockitoRule#strictness(Strictness)} method.
     * In most healthy tests, this method is not needed.
     * We keep it for edge cases and when you really need to change strictness in given test method.
     * For use cases see Javadoc for {@link PotentialStubbingProblem} class.
     *
     * @param strictness new strictness for this session.
     * @since 2.15.0
     */
    @Incubating
    void setStrictness(Strictness strictness);

    /**
     * Must be invoked when the user is done with mocking for given session (test method).
     * It detects unused stubbings and may throw {@link UnnecessaryStubbingException}
     * or emit warnings ({@link MockitoHint}) depending on the {@link Strictness} level.
     * The method also detects incorrect Mockito usage via {@link Mockito#validateMockitoUsage()}.
     * <p>
     * In order to implement {@link Strictness} Mockito session keeps track of mocking using {@link MockitoListener}.
     * This method cleans up the listeners and ensures there is no leftover state after the session finishes.
     * It is necessary to invoke this method to conclude mocking session.
     * For more information about session lifecycle see {@link MockitoSessionBuilder#startMocking()}.
     * <p>
     * This method is intended to be used in your test framework's 'tear down' method.
     * In the case of JUnit it is the "&#064;After" method.
     * <p>
     * For example, see javadoc for {@link MockitoSession}.
     *
     * @see #finishMocking(Throwable)
     * @since 2.7.0
     */
    @Incubating
    void finishMocking();

    /**
     * Must be invoked when the user is done with mocking for given session (test method).
     * When a {@linkplain Throwable failure} is specified, certain checks are disabled to avoid
     * confusion that may arise because there are multiple competing failures. Other than that,
     * this method behaves exactly like {@link #finishMocking()}.
     * <p>
     * This method is intended to be used by framework integrations. When using MockitoSession
     * directly, most users should rather use {@link #finishMocking()}.
     * {@link MockitoRule} uses this method behind the hood.
     *
     * @param failure the exception that caused the test to fail; passing {@code null} is permitted
     * @see #finishMocking()
     * @since 2.15.0
     */
    @Incubating
    void finishMocking(Throwable failure);
}
