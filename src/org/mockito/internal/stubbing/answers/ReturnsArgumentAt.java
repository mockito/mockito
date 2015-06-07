/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Returns the passed parameter identity at specified index.
 *
 * <p>The <code>argumentIndex</code> represents the index in the argument array of the invocation.</p>
 * <p>If this number equals -1 then the last argument is returned.</p>
 *
 * @see org.mockito.AdditionalAnswers
 * @since 1.9.5
 */
public class ReturnsArgumentAt implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -589315085166295101L;

    public static final int LAST_ARGUMENT = -1;

    private final int wantedArgumentPosition;

    /**
     * Build the identity answer to return the argument at the given position in the argument array.
     *
     * @param wantedArgumentPosition The position of the argument identity to return in the invocation.
     *                      Using <code>-1</code> indicates the last argument.
     */
    public ReturnsArgumentAt(final int wantedArgumentPosition) {
        this.wantedArgumentPosition = checkWithinAllowedRange(wantedArgumentPosition);
    }

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        validateIndexWithinInvocationRange(invocation);
        return invocation.getArguments()[actualArgumentPosition(invocation)];
    }


    private int actualArgumentPosition(final InvocationOnMock invocation) {
        return returningLastArg() ? lastArgumentIndexOf(invocation)
                        : argumentIndexOf(invocation);
    }

    private boolean returningLastArg() {
        return wantedArgumentPosition == LAST_ARGUMENT;
    }

    private int argumentIndexOf(final InvocationOnMock invocation) {
        return wantedArgumentPosition;
    }

    private int lastArgumentIndexOf(final InvocationOnMock invocation) {
        return invocation.getArguments().length - 1;
    }

    private int checkWithinAllowedRange(final int argumentPosition) {
        if (argumentPosition != LAST_ARGUMENT && argumentPosition < 0) {
            new Reporter().invalidArgumentRangeAtIdentityAnswerCreationTime();
        }
        return argumentPosition;
    }

    public int wantedArgumentPosition() {
        return wantedArgumentPosition;
    }

    public void validateIndexWithinInvocationRange(final InvocationOnMock invocation) {
        if (!argumentPositionInRange(invocation)) {
            new Reporter().invalidArgumentPositionRangeAtInvocationTime(invocation,
                                                                        returningLastArg(),
                                                                        wantedArgumentPosition);
        }
    }

    private boolean argumentPositionInRange(final InvocationOnMock invocation) {
        final int actualArgumentPosition = actualArgumentPosition(invocation);
        if (actualArgumentPosition < 0) {
            return false;
        }
        if (!invocation.getMethod().isVarArgs()) {
            return invocation.getArguments().length > actualArgumentPosition;
        }
        // for all varargs accepts positive ranges
        return true;
    }

    public Class returnedTypeOnSignature(final InvocationOnMock invocation) {
        final int actualArgumentPosition = actualArgumentPosition(invocation);

        if(!invocation.getMethod().isVarArgs()) {
            return invocation.getMethod().getParameterTypes()[actualArgumentPosition];
        }

        final Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        final int varargPosition = parameterTypes.length - 1;

        if(actualArgumentPosition < varargPosition) {
            return parameterTypes[actualArgumentPosition];
        } else {
            return parameterTypes[varargPosition].getComponentType();
        }
    }
}
