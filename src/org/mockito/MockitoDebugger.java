package org.mockito;

import org.mockitousage.IMethods;
import org.mockitousage.debugging.DebuggingTestCasesTest;

public interface MockitoDebugger {
    void printInvocations(Object ... mocks);
}
