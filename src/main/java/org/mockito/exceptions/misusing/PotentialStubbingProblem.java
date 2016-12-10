/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoRule;

/**
 * Strict stubbing is a new feature introduced in Mockito 2.3.
 * Detecting potential stubbing problems is intended to help writing and debugging tests.
 * The {@code org.mockito.exceptions.misusing.PotentialStubbingProblem} exception is thrown when a mock method is stubbed with argument X in test but then invoked with argument Y in code.
 * Example:
 * <pre class="code"><code class="java">
 * //test method:
 * given(mock.getSomething(100)).willReturn(something);
 *
 * //code under test:
 * Something something = mock.getSomething(50); // <-- stubbing argument mismatch
 * </code></pre>
 * The stubbing argument mismatch typically happens in following use cases:
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
 *  Good looking stubbing via {@link Mockito#when(Object)} has its drawbacks: the framework cannot distinguish between
 *  actual invocation on mock and the stubbing attempt in the test.
 *  Hence the need to use {@link org.mockito.BDDMockito#willReturn(Object)} or {@link Mockito#doReturn(Object)} for certain edge cases.
 *  </li>
 *  <li>In Mockito 2.x, simply don't use {@link MockitoRule#strictness(Strictness)} with {@link Strictness#STRICT_STUBS} for that test.
 * You will lose stubbing strictness but at least you can complete the test.</li>
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
