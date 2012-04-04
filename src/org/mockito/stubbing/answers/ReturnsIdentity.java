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
 */
public class ReturnsIdentity implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -589315085166295101L;

    public static final int LAST_ARGUMENT = -1;

    private final int argumentIndex;

    /**
     * Build the identity answer to return the argument at the given position in the argument array.
     *
     * @param argumentIndex The position of the argument identity to return in the invocation.
     *                      Using <code>-1</code> indicates the last argument.
     */
    public ReturnsIdentity(int argumentIndex) {
        this.argumentIndex = checkWithinAllowedRange(argumentIndex);
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return willReturnLastArg() ?
                lastArgumentOf(invocation) :
                argumentIndexOf(invocation);
    }

    private boolean willReturnLastArg() {
        return argumentIndex == LAST_ARGUMENT;
    }

    private Object argumentIndexOf(InvocationOnMock invocation) {
        Object[] arguments = invocation.getArguments();
        return arguments[checkWithinInvocationRange(invocation, argumentIndex)];
    }

    private Object lastArgumentOf(InvocationOnMock invocation) {
        Object[] arguments = invocation.getArguments();
        return arguments[checkWithinInvocationRange(invocation, arguments.length - 1)];
    }

    private int checkWithinAllowedRange(int argumentIndex) {
        if (argumentIndex < -1) {
            new Reporter().invalidArgumentRangeAtIdentityAnswerCreationTime();
        }
        return argumentIndex;
    }

    private int checkWithinInvocationRange(InvocationOnMock invocation, int actualArgumentIndex) {
        if (actualArgumentIndex < 0 || invocation.getArguments().length <= actualArgumentIndex) {
            new Reporter().invalidArgumentIndexRangeAtInvocationTime(invocation, willReturnLastArg(), argumentIndex);
        }

        return actualArgumentIndex;
    }

}
