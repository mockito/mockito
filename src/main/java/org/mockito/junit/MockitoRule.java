/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.mockito.Incubating;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;

/**
 * Mockito JUnit Rule helps keeping tests clean.
 * It initializes mocks, validates usage and detects incorrect stubbing.
 * JUnit Rule uses {@link org.mockito.MockitoSession} behind the hood.
 * <p>
 * Since 2.1.0, JUnit rule emits stubbing warnings and hints to System output
 * (see also {@link org.mockito.quality.MockitoHint}).
 * The JUnit rule can be used instead of {@link MockitoJUnitRunner}.
 * It requires JUnit at least 4.7.
 * <p>
 * The rule adds following behavior:
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
 *      so that explicit usage of {@link MockitoAnnotations#initMocks(Object)} is not necessary.
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
     * Equivalent of configuring {@link #strictness(Strictness)} with {@link Strictness#LENIENT}.
     * <p>
     * <strong>Please</strong> give us feedback about the stubbing warnings of JUnit rules
     * by commenting on GitHub <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
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

    /**
     * The strictness, especially "strict stubs" ({@link Strictness#STRICT_STUBS})
     * helps debugging and keeping tests clean.
     * It's a new feature introduced in Mockito 2.3.
     * Other levels of strictness - "Warn" - current default ({@link Strictness#WARN})
     * and "lenient" ({@link MockitoRule#silent()}) strictness were already present in Mockito 2.1.0.
     * Version 2.3.0 introduces "strict stubs" ({@link Strictness#STRICT_STUBS}).
     *
     * <pre class="code"><code class="java">
     * public class ExampleTest {
     *     &#064;Rule
     *     public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
     * }
     * </code></pre>
     *
     * How strictness level influences the behavior of JUnit rule:
     * <ol>
     *     <li>{@link Strictness#LENIENT} - equivalent of {@link MockitoRule#silent()} -
     *      no added behavior. The default of Mockito 1.x </li>
     *     <li>{@link Strictness#WARN} - helps keeping tests clean and with debuggability.
     *     Reports console warnings about unused stubs
     *     and stubbing argument mismatch (see {@link org.mockito.quality.MockitoHint}).
     *     The default of Mockito 2.x</li>
     *     <li>{@link Strictness#STRICT_STUBS} - ensures clean tests,
     *     reduces test code duplication, improves debuggability.
     *     See the details in the Javadoc for {@link Strictness#STRICT_STUBS}.
     * </ol>
     *
     * It is possible to tweak the strictness per test method.
     * Why would you need it? See the use cases in Javadoc for {@link PotentialStubbingProblem} class.
     *
     * <pre class="code"><code class="java">
     * public class ExampleTest {
     *     &#064;Rule
     *     public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
     *
     *     &#064;Test public void exampleTest() {
     *         //Change the strictness level only for this test method
     *         //Useful for edge cases (see Javadoc for PotentialStubbingProblem class)
     *         mockito.strictness(Strictness.LENIENT);
     *
     *         //remaining test code
     *     }
     * }
     * </code></pre>
     *
     * "Strict stubs" are tentatively planned to be the default for Mockito v3</li>
     * We are very eager to hear feedback about "strict stubbing" feature, let us know by commenting on GitHub
     * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
     * Strict stubbing is an attempt to improve testability and productivity with Mockito. Tell us what you think!
     *
     * @since 2.3.0
     */
    @Incubating
    MockitoRule strictness(Strictness strictness);
}
