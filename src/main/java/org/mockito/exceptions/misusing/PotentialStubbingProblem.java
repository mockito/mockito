/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoRule;

/**
 * Strict stubbing is a new opt-in feature for JUnit Rule ({@link MockitoRule#strictness(Strictness)})
 * and JUnit Runner ({@link org.mockito.junit.MockitoJUnitRunner.StrictStubs}).
 * If rule or runner cannot be used, strict stubbing can be enabled via {@link MockitoSession}.
 * <p>
 * TODO
 * What does strict stubbing mean?
 * <p>
 * Detecting potential stubbing problems is intended to help writing and debugging tests.
 * The {@code org.mockito.exceptions.misusing.PotentialStubbingProblem} exception is thrown
 * when mocked method is stubbed with some argument in test
 * but then invoked with <strong>different</strong> argument in code.
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
 * The stubbing argument mismatch is triggered in following use cases:
 * <ol>
 *     <li>Mistake or typo in the test code, the argument(s) used when declaring stubbings is unintentionally different</li>
 *     <li>Mistake or typo in the code under test, the argument(s) used in the code under test is unintentionally different</li>
 *     <li>Intentional use of stubbed method with different argument, either in the test (more stubbing) or in code under test</li>
 * </ol>
 * This exception is very useful for 95% of the cases (use cases 1 and 2).
 * However, it can give false negative signal for 5% of the cases (use case 3).
 * It is a trade-off for better debuggability and productivity of the typical cases.
 * <p>
 * What to do if you fall into use case 3? You have 2 options:
 * <ol>
 *  <li>Do you see this exception because you're stubbing the same method multiple times in the test?
 *  In that case, please use {@link org.mockito.BDDMockito#willReturn(Object)} or {@link Mockito#doReturn(Object)}
 *  family of methods for stubbing.
 *  Convenient stubbing via {@link Mockito#when(Object)} has its drawbacks: the framework cannot distinguish between
 *  actual invocation on mock (real code) and the stubbing declaration (test code).
 *  Hence the need to use {@link org.mockito.BDDMockito#willReturn(Object)} or {@link Mockito#doReturn(Object)} for certain edge cases.
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
 *  </li>
 *  <li>In Mockito 2.x, don't use {@link MockitoRule#strictness(Strictness)} with {@link Strictness#STRICT_STUBS} for that test.
 *  If you use JUnit Runner, don't use {@link org.mockito.junit.MockitoJUnitRunner.StrictStubs} for that test.
 * Strict stubbing will be unavailable for that test class.</li>
 * </ol>
 * <p>
 * We are very eager to hear feedback about "strict stubbing" feature, let us know by commenting on GitHub
 * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
 * Strict stubbing is an attempt to improve testability and productivity with Mocktio. Tell us what you think!
 *
 * @since 2.3.0
 */
public class PotentialStubbingProblem extends MockitoException {
    public PotentialStubbingProblem(String message) {
        super(message);
    }
}
