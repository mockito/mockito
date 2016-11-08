/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.MethodRule;

/**
 * Since 2.1.0, JUnit rule emits stubbing warnings and hints to System output
 * (see also {@link org.mockito.quality.MockitoHint}).
 * The JUnit rule can be used instead of {@link MockitoJUnitRunner}.
 * It requires JUnit at least 4.7.
 *
 * This rule adds following behavior:
 * <ul>
 *   <li>
 *      Since 2.1.0, stubbing warnings and hints are printed to System output.
 *      Hints contain clickable links that take you right to the line of code that contains a possible problem.
 *      <strong>Please</strong> give us feedback about the stubbing warnings of JUnit rules in the issue tracker
 *      (<a href="https://github.com/mockito/mockito/issues/384">issue 384</a>).
 *      It's a new feature of Mockito 2.1.0. It aims to help debugging tests.
 *      If you wish the previous behavior, see {@link MockitoRule#silent()}.
 *      However, we would really like to know why do you wish to silence the warnings!
 *      See also {@link org.mockito.quality.MockitoHint}.
 *   <li>
 *      Initializes mocks annotated with {@link org.mockito.Mock},
 *      so that explicit usage of {@link org.mockito.MockitoAnnotations#initMocks(Object)} is not necessary.
 *      Mocks are initialized before each test method.
 *   <li>
 *      Validates framework usage after each test method. See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 *
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
public interface MockitoRule extends MethodRule {

    /**
     * Rule will not report stubbing warnings during test execution.
     * By default, stubbing warnings are printed to Standard output to help debugging.
     * <p>
     * <strong>Please</strong> give us feedback about the stubbing warnings of JUnit rules.
     * It's a new feature of Mockito 2.1.0. It aims to help debugging tests.
     * We want to make sure the feature is useful.
     * We would really like to know why do you wish to silence the warnings!
     * See also {@link org.mockito.quality.MockitoHint}.
     * <p>
     *
     * Example:
     * <pre class="code"><code class="java">
     * public class ExampleTest {
     *
     *     &#064;Rule
     *     public MockitoRule rule = MockitoJUnit.rule().silent();
     *
     * }
     * </code></pre>
     *
     * @since 2.1.0
     */
    MockitoRule silent();
}
