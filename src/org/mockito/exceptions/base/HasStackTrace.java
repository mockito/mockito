/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

public interface HasStackTrace {
    
    void setStackTrace(StackTraceElement[] stackTrace);
    
    StackTraceElement[] getStackTrace();

}