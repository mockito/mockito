/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.mockito.Incubating;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.quality.MockitoHint;
import org.mockito.quality.Strictness;

/**
 * Mockito JUnit Rule helps keeping tests clean.
 * It initializes mocks, validates usage and detects incorrect stubbing.
 * Make sure to configure your rule with {@link #strictness(Strictness)} which automatically
 * detects <strong>stubbing argument mismatches</strong> and is planned to be the default in Mockito v3.
 * <p>
 * Since Mockito 2.1.0, JUnit rule emits stubbing warnings and hints to System output (see {@link MockitoHint}).
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
 *      See also {@link MockitoHint}.
 *   <li>
 *      Initializes mocks annotated with {@link org.mockito.Mock},
 *      so that explicit usage of {@link MockitoAnnotations#openMocks(Object)} is not necessary.
 *      Mocks are initialized before each test method.
 *   <li>
 *      Validates framework usage after each test method. See javadoc for {@link org.mockito.Mockito#validateMockitoUsage()}.
 *   <li>
 *      It is highly recommended to use the rule with {@link #strictness(Strictness)} configured to {@link Strictness#STRICT_STUBS}.
 *      It drives cleaner tests and improves debugging experience.
 *      The only reason this feature is not turned on by default
 *      is because it would have been an incompatible change
 *      and Mockito strictly follows <a href="http://semver.org">semantic versioning</a>.
 *
 * </ul>
 * Example use:
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *
 *     //Creating new rule with recommended Strictness setting
 *     &#064;Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
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
 * If you would like to take advantage of Mockito JUnit rule features
 * but you cannot use the rule because, for example, you use TestNG, there is a solution!
 * {@link MockitoSession} API is intended to offer cleaner tests and improved debuggability
 * to users that cannot use Mockito's built-in JUnit support (runner or the rule).
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
     * See also {@link MockitoHint}.
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
     * Other levels of strictness - "warn" - ({@link Strictness#WARN})
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
     * See Javadoc for {@link Strictness} to learn how strictness influences the behavior of the JUnit rule.
     * See {@link Strictness#STRICT_STUBS} to learn why is it recommended to use "strict stubbing".
     * <p>
     * It is possible to tweak the strictness per test method.
     * Why would you need it? See the use cases in Javadoc for {@link PotentialStubbingProblem} class.
     * In order to tweak strictness per stubbing see {@link Mockito#lenient()}, per mock see {@link MockSettings#lenient()}.
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
     * "Strict stubs" are planned to be the default for Mockito v3</li>
     * We are very eager to hear feedback about "strict stubbing" feature, let us know by commenting on GitHub
     * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
     * Strict stubbing is an attempt to improve testability and productivity with Mockito. Tell us what you think!
     *
     * @since 2.3.0
     */
    @Incubating
    MockitoRule strictness(Strictness strictness);
}
