/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.NotExtensible;
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.quality.Strictness;

/**
 * Fluent builder interface for {@code MockitoSession} objects.
 * See the documentation and examples in Javadoc for {@link MockitoSession}.
 *
 * @since 2.7.0
 */
@Incubating
@NotExtensible
public interface MockitoSessionBuilder {

    /**
     * Adds the test class instance for initialization of fields annotated with Mockito annotations
     * like {@link org.mockito.Mock}.
     * When this method is invoked it <strong>does not perform</strong> initialization of mocks on the spot!
     * Only when {@link #startMocking()} is invoked then annotated fields will be initialized.
     * Traditional API to initialize mocks, the {@link MockitoAnnotations#openMocks(Object)} method
     * has limited support for driving cleaner tests because it does not support configuring {@link Strictness}.
     * Want cleaner tests and better productivity?
     * Migrate from {@link MockitoAnnotations#openMocks(Object)}
     * to {@link MockitoSession}!
     * <p>
     * This method may be called multiple times to add multiple, e.g. nested, test class instances.
     * <p>
     * See code sample in {@link MockitoSession}.
     *
     * @param testClassInstance test class instance that contains fields with Mockito annotations to be initialized.
     *  Passing {@code null} is permitted but will be ignored.
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
     * @since 2.7.0
     */
    @Incubating
    MockitoSessionBuilder initMocks(Object testClassInstance);

    /**
     * Adds the test class instances for initialization of fields annotated with Mockito annotations
     * like {@link org.mockito.Mock}.
     * <p>
     * In most scenarios, you only need to init mocks on a single test class instance.
     * This method is useful for advanced framework integrations (like JUnit Jupiter), when a test uses multiple, e.g. nested, test class instances.
     * <p>
     * This method calls {@link #initMocks(Object)} for each passed test class instance.
     *
     * @param testClassInstances test class instances that contains fields with Mockito annotations to be initialized.
     *  Passing {@code null} or an empty array is permitted but will be ignored.
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
     * @see #initMocks(Object)
     * @since 2.15.0
     */
    @Incubating
    MockitoSessionBuilder initMocks(Object... testClassInstances);

    /**
     * Configures the name of the {@code MockitoSession} instance.
     * <p>
     * The name is used to output {@linkplain org.mockito.quality.MockitoHint hints} when
     * {@linkplain MockitoSession#finishMocking() finishing} a session.
     * <p>
     * This method is intended to be used by framework integrations, e.g. JUnit. When building
     * a {@code MockitoSession} for direct use, users are not expected to call it.
     *
     * @param name of {@code MockitoSession} instance.
     *  Passing {@code null} is permitted and will make the session use a default value.
     *  The current default is the name of the last test class instance passed to
     *  {@link #initMocks(Object)} or {@link #initMocks(Object...)}, if available;
     *  otherwise, {@code "<Unnamed Session>"} is used.
     *
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
     * @see org.mockito.quality.MockitoHint
     * @since 2.15.0
     */
    @Incubating
    MockitoSessionBuilder name(String name);

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
     * Configures logger used by {@code MockitoSession} for emitting
     * {@linkplain org.mockito.quality.MockitoHint warnings} when finishing the session.
     * <p>
     * Please note that the use of {@linkplain Strictness#STRICT_STUBS strict stubs} is
     * recommended over emitting warnings because warnings are easily ignored and spoil the log output.
     * Instead of using this method, please consider setting strictness with {@link #strictness(Strictness)}.
     *
     * @param logger for warnings emitted when finishing {@code MockitoSession}.
     *  Passing {@code null} is permitted and will make the session use a default value.
     *  By default, warnings will be logged to the console.
     *
     * @return the same builder instance for fluent configuration of {@code MockitoSession}.
     * @see org.mockito.quality.MockitoHint
     * @since 2.15.0
     */
    @Incubating
    MockitoSessionBuilder logger(MockitoSessionLogger logger);

    /**
     * Starts new mocking session! Creates new {@code MockitoSession} instance to initialize the session.
     * At this point annotated fields are initialized per {@link #initMocks(Object)} method.
     * When you are done with the session it is required to invoke {@link MockitoSession#finishMocking()}.
     * This will trigger stubbing validation, cleaning up the internal state like removal of internal listeners.
     * <p>
     * Mockito tracks created sessions internally and prevents the user from creating new sessions without
     * using {@link MockitoSession#finishMocking()}.
     * When you run tests concurrently in multiple threads, it is legal for each thread to have single active Mockito session.
     * When you attempt to start new session in a thread that already has an unfinished session
     * {@link UnfinishedMockingSessionException} will be triggered.
     * <p>
     * See examples in {@link MockitoSession}.
     *
     * @return new {@code MockitoSession} instance
     * @since 2.7.0
     * @throws UnfinishedMockingSessionException
     *  when previous session was not concluded with {@link MockitoSession#finishMocking()}
     */
    @Incubating
    MockitoSession startMocking() throws UnfinishedMockingSessionException;
}
