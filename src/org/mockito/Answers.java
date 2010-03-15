/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.defaultanswers.GloballyConfiguredAnswer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMocks;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.stubbing.Answer;

/**
 * Enumeration of pre-configured mock answers
 * <p>
 * You can use it to pass extra parameters to &#064;Mock annotation, see more info here: {@link Mock}
 * <p>
 * Example:
 * <pre>
 *   &#064;Mock(answer = RETURNS_DEEP_STUBS) UserProvider userProvider;
 * </pre>
 * <b>This is not the full list</b> of Answers available in Mockito. Some interesting answers can be found in org.mockito.stubbing.answers package.
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