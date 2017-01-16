package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
     * When this method is invoked it <strong>does not perform</strong> initialization of mocks!
     * Only when {@link #startMocking()} is invoked then annotated fields will be initialized.
     * Traditional API to initialize mocks, the {@link MockitoAnnotations#initMocks(Object)} method
     * does not support configuring {@link Strictness}.
     * <p>
     * See examples in {@link MockitoSession}.
     *
     * @param testClassInstance contains fields with Mockito annotations to be initialized.
     *  Passing {@code null} is permitted and will make the session use a default value.
     *  The current default is '{@code new Object()}'.
     * @return this builder instance for fluent configuration of {@code MockitoSession}.
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder initMocks(Object testClassInstance);

    /**
     * Configures strictness of {@code MockitoSession} instance.
     * See examples in {@link MockitoSession}.
     *
     * @param strictness for {@code MockitoSession} instance.
     *  Passing {@code null} is permitted and will make the session use a default value.
     *  The current default is {@link Strictness#STRICT_STUBS}.
     *
     * @return this builder instance for fluent configuration of {@code MockitoSession}.
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder strictness(Strictness strictness);

    /**
     * Creates new {@code MockitoSession} instance.
     * See examples in {@link MockitoSession}.
     *
     * @return new {@code MockitoSession} instance
     * @since 2.7.0
     */
    @Incubating
    MockitoSession startMocking();
}
