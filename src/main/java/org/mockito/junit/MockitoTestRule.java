/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.Incubating;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.quality.MockitoHint;
import org.mockito.quality.Strictness;

/**
 * Equivalent to {@link MockitoRule}, but also inherits from {@link TestRule}. For more information,
 * please see the documentation on {@link MockitoRule}.
 */
public interface MockitoTestRule extends TestRule {

    /**
     * Rule will not report stubbing warnings during test execution. By default, stubbing warnings are
     * printed to Standard output to help debugging. Equivalent of configuring {@link
     * #strictness(Strictness)} with {@link Strictness#LENIENT}.
     *
     * <p><strong>Please</strong> give us feedback about the stubbing warnings of JUnit rules by
     * commenting on GitHub <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
     * It's a new feature of Mockito 2.1.0. It aims to help debugging tests. We want to make sure the
     * feature is useful. We would really like to know why do you wish to silence the warnings! See
     * also {@link MockitoHint}.
     *
     * <p>Example:
     *
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
    MockitoTestRule silent();

    /**
     * The strictness, especially "strict stubs" ({@link Strictness#STRICT_STUBS}) helps debugging and
     * keeping tests clean. It's a new feature introduced in Mockito 2.3. Other levels of strictness -
     * "warn" - ({@link Strictness#WARN}) and "lenient" ({@link MockitoRule#silent()}) strictness were
     * already present in Mockito 2.1.0. Version 2.3.0 introduces "strict stubs" ({@link
     * Strictness#STRICT_STUBS}).
     *
     * <pre class="code"><code class="java">
     * public class ExampleTest {
     *     &#064;Rule
     *     public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
     * }
     * </code></pre>
     *
     * See Javadoc for {@link Strictness} to learn how strictness influences the behavior of the JUnit
     * rule. See {@link Strictness#STRICT_STUBS} to learn why is it recommended to use "strict
     * stubbing".
     *
     * <p>It is possible to tweak the strictness per test method. Why would you need it? See the use
     * cases in Javadoc for {@link PotentialStubbingProblem} class. In order to tweak strictness per
     * stubbing see {@link Mockito#lenient()}, per mock see {@link MockSettings#lenient()}.
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
     * "Strict stubs" are planned to be the default for Mockito v3 We are very eager to hear feedback
     * about "strict stubbing" feature, let us know by commenting on GitHub <a
     * href="https://github.com/mockito/mockito/issues/769">issue 769</a>. Strict stubbing is an
     * attempt to improve testability and productivity with Mockito. Tell us what you think!
     *
     * @since 2.3.0
     */
    @Incubating
    MockitoTestRule strictness(Strictness strictness);

}
