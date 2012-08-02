package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;

/**
* by Szczepan Faber, created at: 7/29/12
*/
public class DefaultStackTraceCleaner implements StackTraceCleaner {
    public boolean isOut(StackTraceElement e) {
        boolean fromMockObject = e.getClassName().contains("$$EnhancerByMockitoWithCGLIB$$");
        boolean fromOrgMockito = e.getClassName().startsWith("org.mockito.");
        boolean isRunner = e.getClassName().startsWith("org.mockito.runners.");
        boolean isInternalRunner = e.getClassName().startsWith("org.mockito.internal.runners.");
        return (fromMockObject || fromOrgMockito) && !isRunner && !isInternalRunner;
    }
}
