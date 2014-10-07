package org.mockito.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.internal.rules.JUnitRule;

import static org.mockito.internal.util.Checks.checkNotNull;

/**
 * The MockitoJUnitRule Rule can be used instead of {@link org.mockito.runners.MockitoJUnitRunner}.
 * Requires JUnit 4.9 (at least).
 *
 * This rule adds following behavior:
 * <ul>
 *   <li>
 *      Initializes mocks annotated with {@link org.mockito.Mock},
 *      so that explicit usage of {@link org.mockito.MockitoAnnotations#initMocks(Object)} is not necessary.
 *      Mocks are initialized before each test method.
 *   <li>
 *      validates framework usage after each test method. See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 * </ul>
 * Example use:
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *
 *     &#064;Rule
 *     public MockitoJUnitRule mockitoJUnitRule = new MockitoJUnitRule(this);</b>
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
public class MockitoJUnitRule implements TestRule {

    private final JUnitRule jUnitRule;

    /**
     * @param targetTest the test class instance where the rule is declared. Cannot be null.
     */
    public MockitoJUnitRule(Object targetTest) {
        checkNotNull(targetTest, "Mockito JUnit rule target");
        this.jUnitRule = new JUnitRule(targetTest);
    }

    public Statement apply(final Statement base, Description description) {
        return jUnitRule.apply(base, description);
    }

}
