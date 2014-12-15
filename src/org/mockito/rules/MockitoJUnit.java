package org.mockito.rules;

import org.junit.rules.MethodRule;
import org.mockito.junit.MockitoJUnitRule;

/**
 * The MockitoJUnit rule can be used instead of {@link org.mockito.runners.MockitoJUnitRunner}.
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
 *     public MethodRule mockitoJUnitRule = MockitoJUnit.rule();</b>
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
public class MockitoJUnit {

    /**
     * @return new MockitoJUnitRule.
     */
    public static MethodRule rule() {
        return new MockitoJUnitRule();
    }
}
