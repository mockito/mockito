/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.mock;

import org.mockito.Incubating;
import org.mockito.listeners.InvocationListener;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Set;

/**
 * Informs about the mock settings. An immutable view of {@link org.mockito.MockSettings}.
 */
public interface MockCreationSettings<T> {

    /**
     * Mocked type. An interface or class the mock should implement / extend.
     */
    Class<T> getTypeToMock();

    /**
     * the extra interfaces the mock object should implement.
     */
    Set<Class<?>> getExtraInterfaces();

    /**
     * the name of this mock, as printed on verification errors; see {@link org.mockito.MockSettings#name}.
     */
    MockName getMockName();

    /**
     * the default answer for this mock, see {@link org.mockito.MockSettings#defaultAnswer}.
     */
    Answer<?> getDefaultAnswer();

    /**
     * the spied instance - needed for spies.
     */
    Object getSpiedInstance();

    /**
     * if the mock is serializable, see {@link org.mockito.MockSettings#serializable}.
     */
    boolean isSerializable();

    /**
     * @return the serializable mode of this mock
     */
    SerializableMode getSerializableMode();

    /**
     * Whether the mock is only for stubbing, i.e. does not remember
     * parameters on its invocation and therefore cannot
     * be used for verification
     */
    boolean isStubOnly();

    /**
     * The invocation listeners attached to this mock, see {@link org.mockito.MockSettings#invocationListeners}.
     */
    List<InvocationListener> getInvocationListeners();

    /**
     * Informs whether the mock instance should be created via constructor
     *
     * @since 1.10.12
     */
    @Incubating
    boolean isUsingConstructor();

    /**
     * Used when mocking non-static inner classes in conjunction with {@link #isUsingConstructor()}
     *
     * @return the outer class instance used for creation of the mock object via the constructor.
     * @since 1.10.12
     */
    @Incubating
    Object getOuterClassInstance();
}