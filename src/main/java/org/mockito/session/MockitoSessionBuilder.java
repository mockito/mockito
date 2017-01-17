package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

/**
 * Fluent builder interface for {@code MockitoSession} objects.
 * See the documentation and examples in Javadoc for {@link MockitoSession}.
 *
 * @since 2.7.0
 */
@Incubating
public interface MockitoSessionBuilder {

    /**
     * Configures the test class instance for initialization of fields annotated with Mockito annotations
     * like {@link org.mockito.Mock}.
     * When this method is invoked it <strong>does not perform</strong> initialization of mocks on the spot!
     * Only when {@link #startMocking()} is invoked then annotated fields will be initialized.
     * Traditional API to initialize mocks, the {@link MockitoAnnotations#initMocks(Object)} method
     * has limited support for driving cleaner tests because it does not support configuring {@link Strictness}.
     * Want cleaner tests and better productivity?
     * Migrate from {@link MockitoAnnotations#initMocks(Object)}
     * to {@link MockitoSession}!
     * <p>
     * See code sample in {@link MockitoSession}.
     *
     * @param testClassInstance test class instance that contains fields with Mockito annotations to be initialized.
     *  Passing {@code null} is permitted and will make the session use a default value.
     *  The current default is '{@code new Object()}'.
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
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
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder strictness(Strictness strictness);

    /**
     * Starts new mocking session! Creates new {@code MockitoSession} instance to initialize the session.
     * At this point annotated fields are initialized per {@link #initMocks(Object)} method.
     * See examples in {@link MockitoSession}.
     *
     * @return new {@code MockitoSession} instance
     * @since 2.7.0
     */
    @Incubating
    MockitoSession startMocking();
}
