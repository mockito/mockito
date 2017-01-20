package org.mockito;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

/**
 * {@code MockitoSession} is an optional, highly recommended feature
 * that helps driving cleaner tests by eliminating boilerplate code and adding extra validation.
 * <p>
 * {@code MockitoSession} is a session of mocking, during which the user creates and uses Mockito mocks.
 * Typically the session is an execution of a single test method.
 * {@code MockitoSession} initializes mocks, validates usage and detects incorrect stubbing.
 * It helps keeping tests clean, {@link MockitoJUnitRunner} and {@link MockitoRule} use it behind the hood.
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
 * and traditional {@link MockitoAnnotations#initMocks(Object)}?
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
 * If you use Mockito annotations with {@link MockitoAnnotations#initMocks(Object)}
 * but not Mockito runner/rule please try out Mockito's JUnit support (runner or rule) or
 * start using {@code MockitoSession}. You'll get cleaner tests and better productivity.
 * <p>
 * Mockito team would really appreciate feedback about {@code MockitoSession} API.
 * Help us out by commenting at <a href="https://github.com/mockito/mockito/issues/857">issue 857</a>.
 *
 * @since 2.7.0
 */
@Incubating
public interface MockitoSession {

    /**
     * Must be invoked after mockito session has completed.
     * This method is intended to be used in your test framework's 'tear down' method.
     * In case of JUnit it is the "&#064;After" method.
     * <p>
     * For example, see javadoc for {@link MockitoSession}.
     *
     * @since 2.7.0
     */
    @Incubating
    void finishMocking();
}
