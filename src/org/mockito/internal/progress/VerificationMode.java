/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.Mockito;

/**
 * Allows verifying that certain behavior happened at least once / exact number of times / never. E.g:
 * <pre>
 *   verify(mock, times(5)).someMethod("was called five times");
 *   
 *   verify(mock, atLeastOnce()).someMethod("was called at least once");
 *   
 *   verify(mock, never()).someMethod("was never called");
 * </pre>
 * 
 * <b>times(1) is the default</b> and can be omitted
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationMode {}