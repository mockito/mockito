/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;


public class Result implements IAnswer {

    private IAnswer value;

    private Result(IAnswer value) {
        this.value = value;
    }

    public static Result createThrowResult(final Throwable throwable) {
        return new Result(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                throw throwable;
            }
        });
    }
    public static Result createReturnResult(final Object value) {

        return new Result(new IAnswer<Object>() {
            public Object answer() throws Throwable {
                return value;
            }
        });
    }

    public static Result createAnswerResult(IAnswer answer) {
        return new Result(answer);
    }

    public Object answer() throws Throwable {
        return value.answer();
    }
}
