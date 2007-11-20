package org.mockito.exceptions;

public interface HasFilterableStackTrace {
    
    void setStackTrace(StackTraceElement[] stackTrace);
    
    StackTraceElement[] getStackTrace();
    
    StackTraceElement[] getUnfilteredStackTrace();

}
