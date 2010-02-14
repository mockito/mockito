package org.mockito.internal.util.junit;

import org.junit.runner.notification.Failure;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.internal.util.reflection.Whitebox;

public class JUnitFailureHacker {

    public void appendWarnings(Failure failure, String warnings) {
        //TODO: this has to protect the use in case jUnit changes and this internal state logic fails
        Throwable throwable = (Throwable) Whitebox.getInternalState(failure, "fThrownException");

        String newMessage = "contains both: actual test failure *and* Mockito warnings.\n" +
                warnings + "\n *** The actual failure is because of: ***\n";

        ExceptionIncludingMockitoWarnings e = new ExceptionIncludingMockitoWarnings(newMessage, throwable);
        e.setStackTrace(throwable.getStackTrace());
        Whitebox.setInternalState(failure, "fThrownException", e);
    }   
}