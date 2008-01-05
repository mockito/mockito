/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.mockito.Mockito;

/**
 * Allows verifying that certain behavior happened at least once or exact number of times. E.g:
 * <pre>
 *   verify(mock, times(5)).someMethod("should be called five times");
 *   
 *   verify(mock, atLeastOnce()).someMethod("should be called at least once");
 * </pre>
 * 
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationMode {}