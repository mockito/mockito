/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.VerificationMode;

/**
 * Allows strict order verification. E.g:
 * 
 * <pre>
 * Strictly strictly = createStrictOrderVerifier(firstMock, secondMock);
 * 
 * strictly.verify(firstMock).add("should be called first");
 * strictly.verify(secondMock).add("should be called second");
 * </pre>
 * 
 * See examples {@link Mockito#createStrictOrderVerifier(Object...)}
 */
public interface Strictly {

    /**
     * Verifies mock object strictly in order. E.g:
     * 
     * <pre>
     * Strictly strictly = createStrictOrderVerifier(firstMock, secondMock);
     * 
     * strictly.verify(firstMock).add("should be called first");
     * strictly.verify(secondMock).add("should be called second");
     * </pre>
     * <p>
     * 
     * See examples {@link Mockito}
     * 
     * @param mock to be verified
     * 
     * @return mock object itself
     */
    <T> T verify(T mock);

    /**
     * Verifies mock object strictly in order. E.g:
     * 
     * <pre>
     * Strictly strictly = createStrictOrderVerifier(firstMock, secondMock);
     * 
     * strictly.verify(firstMock, times(2)).add("should be called first two times");
     * strictly.verify(secondMock, atLeastOnce()).add("should be called second");
     * </pre>
     * <p>
     * 
     * See examples {@link Mockito}
     * 
     * @param mock to be verified
     * @param mode - times(x) or atLeastOnce()
     * 
     * @return mock object itself
     */
    <T> T verify(T mock, VerificationMode verificationMode);
}