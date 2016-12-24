package org.mockito.quality;

import org.mockito.Incubating;
import org.mockito.junit.MockitoRule;

/**
 * Configures the "strictness" of Mockito during test execution.
 * To understand how "strictness" can improve testability refer to the javadoc
 * of the tools that use "strictness", see docs for {@link MockitoRule#strictness(Strictness)}.
 *
 * @since 2.3.0
 */
@Incubating
public enum Strictness {

    /**
     * No extra strictness. Mockito 1.x behavior.
     *
     * @since 2.3.0
     */
    @Incubating
    LENIENT,

    /**
     * Helps keeping tests clean and improves debuggability.
     * Extra warnings emitted to the console, see {@link MockitoHint}.
     * Default Mockito 2.x behavior.
     *
     * @since 2.3.0
     */
    @Incubating
    WARN,

    /**
     * Ensures clean tests, reduces test code duplication, improves debuggability.
     * See {@link MockitoRule#strictness(Strictness)}.
     * Tentatively planned default for Mockito 3.x.
     *
     * @since 2.3.0
     */
    @Incubating
    STRICT_STUBS;
}
