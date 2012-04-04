package org.mockito.stubbing.answers;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

/**
 * Returns the passed parameter identity at specified index.
 *
 * <p>The <code>argumentIndex</code> represents the index in the argument array of the invocation.</p>
 * <p>If this number equals -1 then the last argument is returned.</p>
 *
 * @see org.mockito.AdditionalAnswers
 * @since 1.9.5
 */
public class ReturnsIdentity implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -589315085166295101L;

    public static final int LAST_ARGUMENT = -1;

    private final int wantedArgumentIndex;

    /**
     * Build the identity answer to return the argument at the given position in the argument array.
     *
     * @param wantedArgumentIndex The position of the argument identity to return in the invocation.
     *                      Using <code>-1</code> indicates the last argument.
     */
    public ReturnsIdentity(int wantedArgumentIndex) {
        this.wantedArgumentIndex = checkWithinAllowedRange(wantedArgumentIndex);
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        validateIndexWithinInvocationRange(invocation);
        return invocation.getArguments()[actualArgumentIndex(invocation)];
    }


    private int actualArgumentIndex(InvocationOnMock invocation) {
        return returningLastArg() ?
                lastArgumentIndexOf(invocation) :
                argumentIndexOf(invocation);
    }

    private boolean returningLastArg() {
        return wantedArgumentIndex == LAST_ARGUMENT;
    }

    private int argumentIndexOf(InvocationOnMock invocation) {
        return wantedArgumentIndex;
    }

    private int lastArgumentIndexOf(InvocationOnMock invocation) {
        return invocation.getArguments().length - 1;
    }

    private int checkWithinAllowedRange(int argumentIndex) {
        if (argumentIndex != LAST_ARGUMENT && argumentIndex < 0) {
            new Reporter().invalidArgumentRangeAtIdentityAnswerCreationTime();
        }
        return argumentIndex;
    }

    public int wantedArgumentIndex() {
        return wantedArgumentIndex;
    }

    public void validateIndexWithinInvocationRange(InvocationOnMock invocation) {
        if (!argumentIndexInRange(invocation)) {
            new Reporter().invalidArgumentIndexRangeAtInvocationTime(invocation,
                                                                     returningLastArg(),
                                                                     wantedArgumentIndex);
        }
    }

    private boolean argumentIndexInRange(InvocationOnMock invocation) {
        int actualArgumentIndex = actualArgumentIndex(invocation);
        if (actualArgumentIndex < 0) {
            return false;
        }
        if (!invocation.getMethod().isVarArgs()) {
            return invocation.getArguments().length > actualArgumentIndex;
        }
        // for all varargs accepts positive ranges
        return true;
    }

    public Class returnedTypeOnSignature(InvocationOnMock invocation) {
        int actualArgumentIndex = actualArgumentIndex(invocation);

        if(!invocation.getMethod().isVarArgs()) {
            return invocation.getMethod().getParameterTypes()[actualArgumentIndex];
        }

        Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        int varargPosition = parameterTypes.length - 1;

        if(actualArgumentIndex < varargPosition) {
            return parameterTypes[actualArgumentIndex];
        } else {
            return parameterTypes[varargPosition].getComponentType();
        }
    }
}
