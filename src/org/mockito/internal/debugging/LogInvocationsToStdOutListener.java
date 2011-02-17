package org.mockito.internal.debugging;

import java.io.PrintStream;

import org.mockito.invocation.InvocationListener;
import org.mockito.invocation.InvocationOnMock;

/**
 * Logs all invocations to standard output.
 * 
 * Used for debugging interactions with a mock. 
 */
public class LogInvocationsToStdOutListener implements InvocationListener {

	PrintStream printStream = System.out;
	
	public void invoking(InvocationOnMock invocation) {
		printStream.println(invocation.toString());
	}
}
