/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

// TODO this needs to go away - what if stack trace is empty due to
// multithreaded tests or something - I'm always accessing the first element on
// the ST!
public interface HasStackTrace {

    void setStackTrace(StackTraceElement[] stackTrace);

    StackTraceElement[] getStackTrace();

}