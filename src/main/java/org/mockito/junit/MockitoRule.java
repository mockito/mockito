package org.mockito.junit;

import org.junit.rules.MethodRule;

/**
 * The JUnit rule can be used instead of {@link org.mockito.runners.MockitoJUnitRunner}.
 * It requires JUnit at least 4.7.
 *
 * This rule adds following behavior:
 * <ul>
 *   <li>
 *      Initializes mocks annotated with {@link org.mockito.Mock},
 *      so that explicit usage of {@link org.mockito.MockitoAnnotations#initMocks(Object)} is not necessary.
 *      Mocks are initialized before each test method.
 *   <li>
 *      Validates framework usage after each test method. See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 *   <li>
 *      Since 2.*, on test failure, stubbing warnings are printed to System output. They might be useful for debugging.
 * </ul>
 * Example use:
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *
 *     &#064;Rule
 *     public MockitoRule rule = MockitoJUnit.rule();
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
 * @since 1.10.17
 */
public interface MockitoRule extends MethodRule {}
