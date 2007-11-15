package org.mockito.exceptions;

import java.util.*;

public class MockVerificationAssertionError extends AssertionError {

    public MockVerificationAssertionError() {
        super("Mock verification failed");
        
        List<StackTraceElement> filteredStackTrace = new LinkedList<StackTraceElement>();
        for(StackTraceElement trace : getStackTrace()) {
            
//            filteredStackTrace
        }
    }
}
