/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.quality;

import org.mockito.Incubating;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

/**
 * Configures the "strictness" of Mockito, affecting the behavior of stubbings and verification.
 * "Strict stubbing" is a new feature in Mockito 2 that drives cleaner tests and better productivity.
 * The easiest way to leverage it is via Mockito's JUnit support ({@link MockitoJUnit}) or Mockito Session ({@link MockitoSession}).
 * <p>
 * How strictness influences the behavior of the test?
 * <ol>
 *     <li>{@link Strictness#STRICT_STUBS} - ensures clean tests, reduces test code duplication, improves debuggability.
 *       Best combination of flexibility and productivity. Highly recommended.
 *       Planned as default for Mockito v3.
 *       Enable it via {@link MockitoRule}, {@link MockitoJUnitRunner} or {@link MockitoSession}.
 *       See {@link #STRICT_STUBS} for the details.</li>
 *     <li>{@link Strictness#LENIENT} - no added behavior.
 *       The default of Mockito 1.x.
 *       Recommended only if you cannot use {@link #STRICT_STUBS}</li>
 *     <li>{@link Strictness#WARN} - cleaner tests but only if you read the console output.
 *       Reports console warnings about unused stubs
 *       and stubbing argument mismatch (see {@link org.mockito.quality.MockitoHint}).
 *       The default behavior of Mockito 2.x when {@link JUnitRule} or {@link MockitoJUnitRunner} are used.
 *       Recommended if you cannot use {@link #STRICT_STUBS}.
 *       Introduced originally with Mockito 2 because console warnings was the only compatible way of adding such feature.</li>
 * </ol>
 *
 * @since 2.3.0
 */
@Incubating
public enum Strictness {

    /**
     * No extra strictness. Mockito 1.x behavior.
     * Recommended only if you cannot use {@link #STRICT_STUBS}.
     * <p>
     * For more information see {@link Strictness}.
     *
     * @since 2.3.0
     */
    @Incubating
    LENIENT,

    /**
     * Helps keeping tests clean and improves debuggability only if you read the console output.
     * Extra warnings emitted to the console, see {@link MockitoHint}.
     * Default Mockito 2.x behavior.
     * Recommended only if you cannot use {@link #STRICT_STUBS} because console output is ignored most of the time.
     * <p>
     * For more information see {@link Strictness}.
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
     * Enable it via our JUnit support ({@link MockitoJUnit}) or {@link MockitoSession}.
     * <p>
     * Adds following behavior:
     *  <ul>
     *      <li>Improved productivity: the test fails early when code under test invokes
     *          stubbed method with different arguments (see {@link PotentialStubbingProblem}).</li>
     *      <li>Cleaner tests without unnecessary stubbings:
     *          the test fails when unused stubs are present (see {@link UnnecessaryStubbingException}).</li>
     *      <li>Cleaner, more DRY tests ("Don't Repeat Yourself"):
     *          If you use {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}
     *          you no longer need to explicitly verify stubbed invocations.
     *          They are automatically verified for you.</li>
     *  </ul>
     *
     * For more information see {@link Strictness}.
     *
     * @since 2.3.0
     */
    @Incubating
    STRICT_STUBS;
}
