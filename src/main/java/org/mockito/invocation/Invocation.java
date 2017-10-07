/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.NotExtensible;

/**
 * A method call on a mock object. Contains all information and state needed for the Mockito framework to operate.
 * This API might be useful for developers who extend Mockito.
 * <p>
 * The javadoc does not have lots of examples or documentation because its audience is different.
 * Vast majority of users don't need to use the Invocation. It's mostly useful for other framework authors
 * that extend Mockito.
 * <p>
 * Creating own implementations of this interface is not recommended.
 * If you are a framework integrator and you need to programmatically create instances of invocations see {@link InvocationFactory}.
 *
 * @since 1.9.5
 */
@NotExtensible
public interface Invocation extends InvocationOnMock, DescribedInvocation {

    /**
     * @return whether the invocation has been already verified.
     * Needed for {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}
     */
    boolean isVerified();

    /**
     * @return the sequence number of the Invocation. Useful to determine the order of invocations.
     * Used by verification in order.
     */
    int getSequenceNumber();

    /**
     * @return the location in code of this invocation.
     */
    Location getLocation();

    /**
     * Returns unprocessed arguments whereas {@link #getArguments()} returns
     * arguments already processed (e.g. varargs expended, etc.).
     *
     * @return unprocessed arguments, exactly as provided to this invocation.
     */
    Object[] getRawArguments();

    /**
     * Returns unprocessed arguments whereas {@link #getArguments()} returns
     * arguments already processed (e.g. varargs expended, etc.).
     *
     * @return unprocessed arguments, exactly as provided to this invocation.
     */
    Class<?> getRawReturnType();

    /**
     * Marks this invocation as verified so that it will not cause verification error at
     * {@link org.mockito.Mockito#verifyNoMoreInteractions(Object...)}
     */
    void markVerified();

    /**
     * @return the stubbing information for this invocation. May return null - this means
     * the invocation was not stubbed.
     */
    StubInfo stubInfo();

    /**
     * Marks this invocation as stubbed.
     *
     * @param stubInfo the information about stubbing.
     */
    void markStubbed(StubInfo stubInfo);

    /**
     * Informs if the invocation participates in verify-no-more-invocations or verification in order.
     *
     * @return whether this invocation should be ignored for the purposes of
     * verify-no-more-invocations or verification in order.
     */
    boolean isIgnoredForVerification();

    /**
     * Configures this invocation to be ignored for verify-no-more-invocations or verification in order.
     * See also {@link #isIgnoredForVerification()}
     */
    void ignoreForVerification();
}
