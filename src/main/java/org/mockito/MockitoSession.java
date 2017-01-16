package org.mockito;

import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.mockito.junit.MockitoJUnitRunner.StrictStubs;

/**
 * Using MockitoSession can help users drive higher quality tests by leveraging stricter stubbing ({@link Strictness}).
 * <p>
 * Typically, MockitoSession maps to a single test method invocation.
 * MockitoSession offers users strict stubbing ({@link Strictness})
 * without the need to use Mockito's JUnit support
 * ({@link MockitoRule#strictness(Strictness)} and {@link StrictStubs}).
 * Example use cases include TestNG users; users that cannot use Mockito's JUnit support
 * because they already use different runner (Jukito, Springockito);
 * <p>
 * TODO: javadoc here and in the Rule and Runner, Strictness object and Mockito.mockitoSession()
 *
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *     &#064;Mock Foo foo;
 *     MockitoSession mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
 *     &#064;After public void after() {
 *         mockito.finishMocking();
 *     }
 *     // test methods ...
 * }
 * </code></pre>
 *
 * @since 2.7.0
 */
@Incubating
public interface MockitoSession {

    /**
     * Must be invoked after mockito session has completed
     *
     * @since 2.7.0
     */
    @Incubating
    void finishMocking();
}
