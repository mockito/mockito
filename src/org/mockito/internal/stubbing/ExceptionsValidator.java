package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;

public class ExceptionsValidator {

    private Reporter reporter = new Reporter();
    
    public void validate(Throwable throwable, Invocation invocation) {
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