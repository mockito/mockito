/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.mock;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.mockito.MockSettings;
import org.mockito.NotExtensible;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.plugins.MockMaker;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

/**
 * Informs about the mock settings. An immutable view of {@link org.mockito.MockSettings}.
 */
@NotExtensible
public interface MockCreationSettings<T> {

    /**
     * Mocked type. An interface or class the mock should implement / extend.
     */
    Class<T> getTypeToMock();

    /**
     * The generic type of the mock, if any.
     */
    Type getGenericTypeToMock();

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
     * Whether the mock should not make a best effort to preserve annotations.
     */
    boolean isStripAnnotations();

    /**
     * Returns {@link StubbingLookupListener} instances attached to this mock via {@link MockSettings#stubbingLookupListeners(StubbingLookupListener...)}.
     * The resulting list is mutable, you can add/remove listeners even after the mock was created.
     * <p>
     * For more details see {@link StubbingLookupListener}.
     *
     * @since 2.24.6
     */
    List<StubbingLookupListener> getStubbingLookupListeners();

    /**
     * {@link InvocationListener} instances attached to this mock, see {@link org.mockito.MockSettings#invocationListeners(InvocationListener...)}.
     */
    List<InvocationListener> getInvocationListeners();

    /**
     * {@link VerificationStartedListener} instances attached to this mock,
     * see {@link org.mockito.MockSettings#verificationStartedListeners(VerificationStartedListener...)}
     *
     * @since 2.11.0
     */
    List<VerificationStartedListener> getVerificationStartedListeners();

    /**
     * Informs whether the mock instance should be created via constructor
     *
     * @since 1.10.12
     */
    boolean isUsingConstructor();

    /**
     * Used when arguments should be passed to the mocked object's constructor, regardless of whether these
     * arguments are supplied directly, or whether they include the outer instance.
     *
     * @return An array of arguments that are passed to the mocked object's constructor. If
     * {@link #getOuterClassInstance()} is available, it is prepended to the passed arguments.
     *
     * @since 2.7.14
     */
    Object[] getConstructorArgs();

    /**
     * Used when mocking non-static inner classes in conjunction with {@link #isUsingConstructor()}
     *
     * @return the outer class instance used for creation of the mock object via the constructor.
     * @since 1.10.12
     */
    Object getOuterClassInstance();

    /**
     *  @deprecated Use {@link MockCreationSettings#getStrictness()} instead.
     *
     * Informs if the mock was created with "lenient" strictness, e.g. having {@link Strictness#LENIENT} characteristic.
     * For more information about using mocks with lenient strictness, see {@link MockSettings#lenient()}.
     *
     * @since 2.20.0
     */
    @Deprecated
    boolean isLenient();

    /**
     * Sets strictness level for the mock, e.g. having {@link Strictness#STRICT_STUBS} characteristic.
     * For more information about using mocks with custom strictness, see {@link MockSettings#strictness(Strictness)}.
     *
     * @since 4.6.0
     */
    Strictness getStrictness();

    /**
     * Returns the {@link MockMaker} which shall be used to create the mock.
     * When the return value is {@code null}, the default shall be used.
     *
     * @see MockSettings#mockMaker(String)
     * @since 4.8.0
     */
    String getMockMaker();

    /**
     * Returns the {@link MockType} for the mock being created.
     *
     * @see MockType
     * @since 5.9.0
     */
    MockType getMockType();
}
