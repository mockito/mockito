package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.internal.rules.JUnitRule;

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
 *     public MockitoJUnitRule mockitoJUnitRule = MockitoJUnitRule.rule();</b>
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
public class MockitoJUnitRule implements MethodRule {

    private final JUnitRule jUnitRule;

    /**
     */
    public MockitoJUnitRule() {
        this.jUnitRule = new JUnitRule();
    }

    /**
     * Please use {@link MockitoJUnitRule#MockitoJUnitRule()}.
     * @param targetTest the test class instance where the rule is declared. Cannot be null.
     */
    @Deprecated
    public MockitoJUnitRule(Object targetTest) {
        this();
    }

    /**
     * @return new default MockitoJUnitRule.
     */
    public static MockitoJUnitRule rule() {
        return new MockitoJUnitRule();
    }

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return jUnitRule.apply(base, target);
    }
}
