/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.mockito.exceptions.base.MockitoException;

/**
 * {@code PotentialStubbingProblem} improves productivity by failing the test early when the user
 * misconfigures mock's stubbing.
 * <p>
 * {@code PotentialStubbingProblem} exception is a part of "strict stubbing" Mockito API
 * intended to drive cleaner tests and better productivity with Mockito mocks.
 * For more information see {@link Strictness}.
 * <p>
 * {@code PotentialStubbingProblem} is thrown when mocked method is stubbed with some argument in test
 * but then invoked with <strong>different</strong> argument in the code.
 * This scenario is called "stubbing argument mismatch".
 * <p>
 * Example:
 * <pre class="code"><code class="java">
 * //test method:
 * given(mock.getSomething(100)).willReturn(something);
 *
 * //code under test:
 * Something something = mock.getSomething(50); // <-- stubbing argument mismatch
 * </code></pre>
 * The stubbing argument mismatch typically indicates:
 * <ol>
 *     <li>Mistake, typo or misunderstanding in the test code, the argument(s) used when declaring stubbing are different by mistake</li>
 *     <li>Mistake, typo or misunderstanding in the code under test, the argument(s) used when invoking stubbed method are different by mistake</li>
 *     <li>Intentional use of stubbed method with different argument, either in the test (more stubbing) or in code under test</li>
 * </ol>
 * User mistake (use case 1 and 2) make up 95% of the stubbing argument mismatch cases.
 * {@code PotentialStubbingProblem} improves productivity in those scenarios
 * by failing early with clean message pointing out the incorrect stubbing or incorrect invocation of stubbed method.
 * In remaining 5% of the cases (use case 3) {@code PotentialStubbingProblem} can give false negative signal
 * indicating non-existing problem. The exception message contains information how to opt-out from the feature.
 * Mockito optimizes for enhanced productivity of 95% of the cases while offering opt-out for remaining 5%.
 * False negative signal for edge cases is a trade-off for general improvement of productivity.
 * <p>
 * What to do if you fall into use case 3 (false negative signal)? You have 2 options:
 * <ol>
 *  <li>Do you see this exception because you're stubbing the same method multiple times in the same test?
 *  In that case, please use {@link org.mockito.BDDMockito#willReturn(Object)} or {@link Mockito#doReturn(Object)}
 *  family of methods for stubbing.
 *  Convenient stubbing via {@link Mockito#when(Object)} has its drawbacks: the framework cannot distinguish between
 *  actual invocation on mock (real code) and the stubbing declaration (test code).
 *  Hence the need to use {@link org.mockito.BDDMockito#willReturn(Object)} or {@link Mockito#doReturn(Object)} for certain edge cases.
 *  It is a well known limitation of Mockito API and another example how Mockito optimizes its clean API for 95% of the cases
 *  while still supporting edge cases.
 *  </li>
 *  <li>Reduce the strictness level in the test method (only for JUnit Rules):
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *     &#064;Rule
 *     public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
 *
 *     &#064;Test public void exampleTest() {
 *         //Change the strictness level only for this test method:
 *         mockito.strictness(Strictness.LENIENT);
 *
 *         //remaining test code
 *     }
 * }
 * </code></pre>
 *  Currently, reducing strictness is only available to JUnit rules.
 *  If you need it in a different context let us know at <a href="https://github.com/mockito/mockito/issues/857">issue 857</a>.
 *  </li>
 *  <li>To opt-out in Mockito 2.x, simply remove the strict stubbing setting in the test class.</li>
 * </ol>
 * <p>
 * Mockito team is very eager to hear feedback about "strict stubbing" feature, let us know by commenting on GitHub
 * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
 * Strict stubbing is an attempt to improve testability and productivity with Mockito. Tell us what you think!
 *
 * @since 2.3.0
 */
public class PotentialStubbingProblem extends MockitoException {
    public PotentialStubbingProblem(String message) {
        super(message);
    }
}
