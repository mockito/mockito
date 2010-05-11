/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.Mockito;

/**
 * Similar to {@link VerificationMode} but allows combining with other modes. E.g:
 * 
 * <pre>
 * verify(mock, timeout(100).times(5)).foo();
 * 
 * verify(mock, timeout(100).never()).bar();
 * 
 * verify(mock, timeout(200).atLeastOnce()).baz();
 * </pre>
 * 
 * <p>
 * See examples in javadoc for {@link Mockito#verify(Object, VerificationMode)}
 */
public interface FluentVerificationMode extends VerificationMode {
    
   
    
}