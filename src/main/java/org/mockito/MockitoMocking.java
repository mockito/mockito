package org.mockito;

/**
 * Allows to manage Mockito strictness (debugging, cleaner tests) without JUnit.
 *
 * TODO: javadoc here and in the Rule and Runner. Possibly push the documentation to Strictness object.
 *
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *     &#064;Mock Foo foo;
 *     MockitoMocking mocking = Mockito.startMocking(this, Strictness.STRICT_STUBS);
 *     &#064;After public void after() {
 *         mocking.finishMocking();
 *     }
 *     // test methods ...
 * }
 * </code></pre>
 *
 * @since 2.6.0
 */
@Incubating
public interface MockitoMocking {

    /**
     * Must be invoked after the test has completed
     *
     * @since 2.6.0
     */
    @Incubating
    void finishMocking();
}
