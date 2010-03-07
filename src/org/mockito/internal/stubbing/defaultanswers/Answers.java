/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.stubbing.Answer;

/**
 * Enumeration of pre-configured mock answers
 */
public enum Answers {

    RETURNS_DEFAULTS(new GloballyConfiguredAnswer()),
    RETURNS_SMART_NULLS(new ReturnsSmartNulls()),
    RETURNS_MOCKS(new ReturnsMocks()),
    RETURNS_DEEP_STUBS(new ReturnsDeepStubs()),
    CALLS_REAL_METHODS(new CallsRealMethods())
	;

    private Answer<Object> implementation;

    private Answers(Answer<Object> implementation) {
        this.implementation = implementation;
    }

    public Answer<Object> get() {
        return implementation;
    }
}