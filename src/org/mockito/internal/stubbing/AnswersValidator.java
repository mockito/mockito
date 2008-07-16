package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidator {

    private Reporter reporter = new Reporter();
    
    public void validate(Answer<?> answer, Invocation invocation) {
        if (answer instanceof ThrowsException) {
            validateException(answer, invocation);
        }
        
        if (answer instanceof Returns) {
            validateReturnValue(answer, invocation);
        }
    }

    private void validateReturnValue(Answer<?> answer, Invocation invocation) {
        if (invocation.isVoid() && ((Returns) answer).hasReturnValue()) {
            reporter.cannotStubVoidMethodWithAReturnValue();
        }
    }

    private void validateException(Answer<?> answer, Invocation invocation) {
        Throwable throwable = ((ThrowsException) answer).getThrowable();
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