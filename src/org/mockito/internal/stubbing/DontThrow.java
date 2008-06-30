/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class DontThrow extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final DontThrow DONT_THROW = new DontThrow(null);
    private Answer answer;

    public DontThrow(Answer answer) {
        if (answer == null) {
            answer = new Answer() {
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return null;
                }
            };
        }

        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    public boolean hasAnswer() {
        return answer != null;
    }
}
