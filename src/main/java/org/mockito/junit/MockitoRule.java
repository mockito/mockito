/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.mockito.Incubating;
import org.mockito.quality.Strictness;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;

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
     * can really make a difference when debugging and keeping tests clean.
     * It's a new feature introduced in Mockito 2.3.
     * Other levels of strictness - "Warn" - current default ({@link Strictness#WARN})
     * and "lenient" ({@link MockitoRule#silent()}) strictness was already present in Mockito 2.1.0.
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
     *     <li>{@link Strictness#STRICT_STUBS} - ensures clean tests, reduces test code duplication, improves debuggability.
     *     Adds following behavior:
     *      <ul>
     *          <li>Improved debuggability: the test fails early when code under test invokes stubbed method with different arguments (see {@link PotentialStubbingProblem}).</li>
     *          <li>Cleaner tests without unnecessary stubbings: the test fails when there are any unused stubs declared (see {@link UnnecessaryStubbingException}).</li>
     *          <li>Cleaner, more DRY tests ("Don't Repeat Yourself"): If you use {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}
     *              you no longer need to explicitly verify stubbed invocations. They are automatically verified.</li>
     *      </ul>
     * </ol>
     *
     * "Strict stubs" are tentatively planned to be the default for Mockito 3.x</li>
     * We are very eager to hear feedback about "strict stubbing" feature, let us know by commenting on GitHub
     * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
     * Strict stubbing is an attempt to improve testability and productivity with Mocktio. Tell us what you think!
     *
     * @since 2.3.0
     */
    @Incubating
    MockitoRule strictness(Strictness strictness);
}
