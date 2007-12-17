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
 * See examples {@link Mockito#verify(Object, VerificationMode)}
 */
public interface VerificationMode {}