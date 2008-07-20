package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidator {

    private Reporter reporter = new Reporter();
    
    public void validate(Answer<?> answer, Invocation invocation) {
        if (answer instanceof ThrowsException) {
            validateException((ThrowsException) answer, invocation);
        }
        
        if (answer instanceof Returns) {
            validateReturnValue((Returns) answer, invocation);
        }
        
        if (answer instanceof DoesNothing) {
            validateVoidReturn((DoesNothing) answer, invocation);
        }
    }

    private void validateVoidReturn(DoesNothing answer, Invocation invocation) {
        if (!invocation.isVoid()) {
            reporter.cannotStubNonVoidMethodWithAVoidReturn();
        }
    }

    private void validateReturnValue(Returns answer, Invocation invocation) {
        if (invocation.isVoid()) {
            reporter.cannotStubVoidMethodWithAReturnValue();
        }
    }

    private void validateException(ThrowsException answer, Invocation invocation) {
        Throwable throwable = answer.getThrowable();
        if (throwable == null) {
            reporter.cannotStubWithNullThrowable();
        }
        
        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
        
        if (!invocation.isValidException(throwable)) {
            reporter.checkedExceptionInvalid(throwable);
        }
    }
}