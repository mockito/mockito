package org.mockito.quality;

import org.mockito.Incubating;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

/**
 * Configures the "strictness" of Mockito during a mocking session.
 * A session typically maps to a single test method invocation.
 * <p>
 * How strictness level influences the behavior of the test?
 * <ol>
 *     <li>{@link Strictness#LENIENT} - no added behavior.
 *       The default of Mockito 1.x.
 *       Recommended only if you cannot use {@link #STRICT_STUBS} nor {@link #WARN}.</li>
 *     <li>{@link Strictness#WARN} - helps keeping tests clean and with debuggability.
 *       Reports console warnings about unused stubs
 *       and stubbing argument mismatch (see {@link org.mockito.quality.MockitoHint}).
 *       The default behavior of Mockito 2.x when {@link JUnitRule} or {@link MockitoJUnitRunner} are used.</li>
 *       Recommended if you cannot use {@link #STRICT_STUBS}.
 *     <li>{@link Strictness#STRICT_STUBS} - ensures clean tests, reduces test code duplication, improves debuggability.
 *       Best combination of flexibility and productivity. Highly recommended.
 *       Planned as default for Mockito v3.
 *       See {@link #STRICT_STUBS} for the details.
 * </ol>
 *
 * Strictness can be configured using {@link MockitoRule#strictness(Strictness)},
 * {@link MockitoJUnitRunner.StrictStubs} or {@link MockitoSession}.
 *
 * @since 2.3.0
 */
@Incubating
public enum Strictness {

    /**
     * No extra strictness. Mockito 1.x behavior.
     * Recommended only if you cannot use {@link #STRICT_STUBS} nor {@link #WARN}.
     *
     * @since 2.3.0
     */
    @Incubating
    LENIENT,

    /**
     * Helps keeping tests clean and improves debuggability.
     * Extra warnings emitted to the console, see {@link MockitoHint}.
     * Default Mockito 2.x behavior.
     * Recommended if you cannot use {@link #STRICT_STUBS}.
     *
     * @since 2.3.0
     */
    @Incubating
    WARN,

    /**
     * Ensures clean tests, reduces test code duplication, improves debuggability.
     * Offers best combination of flexibility and productivity.
     * Highly recommended.
     * Planned as default for Mockito v3.
     * <p>
     * Adds following behavior:
     *  <ul>
     *      <li>Improved debuggability: the test fails early when code under test invokes
     *          stubbed method with different arguments (see {@link PotentialStubbingProblem}).</li>
     *      <li>Cleaner tests without unnecessary stubbings:
     *          the test fails when there are any unused stubs declared (see {@link UnnecessaryStubbingException}).</li>
     *      <li>Cleaner, more DRY tests ("Don't Repeat Yourself"):
     *          If you use {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}
     *          you no longer need to explicitly verify stubbed invocations.
     *          They are automatically verified.</li>
     *  </ul>
     *
     * @since 2.3.0
     */
    @Incubating
    STRICT_STUBS;
}
