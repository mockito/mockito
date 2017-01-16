package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

/**
 * Fluent builder interface for {@link org.mockito.MockitoSession} objects.
 * See {@link Mockito#mockitoSession()}.
 *
 * TODO SF javadoc everywhere
 *
 * @since 2.7.0
 */
@Incubating
public interface MockitoSessionBuilder {

    /**
     * Configures the test class instance for initialization of fields annotated with Mockito annotations
     * like {@link org.mockito.Mock}.
     * <strong>Does not perform</strong> the initialization when called!
     * The initialization will
     *
     *
     * @param testClassInstance
     * @return
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder initMocks(Object testClassInstance);

    /**
     * Configures strictness of {@code MockitoSession} instance.
     *
     * @param strictness to use
     * @return
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder strictness(Strictness strictness);

    /**
     * Creates new {@code MockitoSession} instance configured
     * @return
     * @since 2.7.0
     */
    @Incubating
    MockitoSession startMocking();
}
