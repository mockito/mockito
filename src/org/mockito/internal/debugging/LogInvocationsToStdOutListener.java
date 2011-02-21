package org.mockito.internal.debugging;

import java.io.PrintStream;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.InvocationListener;

/**
 * Logs all invocations to standard output.
 * 
 * Used for debugging interactions with a mock. 
 */
public class LogInvocationsToStdOutListener implements InvocationListener {

	PrintStream printStream = System.out;
	int ctrOfInteractions = 0;
	
	public void invokingWithReturnValue(PrintableInvocation invocation, Object returnValue, String locationOfStubbing) {
		printHeader();
		printCommonInfos(invocation);
		printlnIndented("Will return: >" + returnValue + "<" + ((returnValue == null) ? "" : " (" + returnValue.getClass().getName() + ")"));
		if (locationOfStubbing == null) {
			printMethodHasNotBeenStubbed();
		} else {
			printMethodHasBeenStubbed(locationOfStubbing);
		}
		printFooter();
	}

	public void invokingWithException(PrintableInvocation invocation, Exception exception, String locationOfStubbing) {
		printHeader();
		printCommonInfos(invocation);
		printlnIndented("Will throw: " + exception);
		if (locationOfStubbing == null) {
			printMethodHasNotBeenStubbed();
		} else {
			printMethodHasBeenStubbed(locationOfStubbing);
		}
		printFooter();
	}

	private void printHeader() {
		ctrOfInteractions++;
		printStream.println("############ Logging method invocation #" + ctrOfInteractions + " on mock/spy ########");
	}
	
	private void printMethodHasNotBeenStubbed() {
		printlnIndented("Method has not been stubbed.");
	}

	private void printMethodHasBeenStubbed(String locationOfStubbing) {
		printlnIndented("Method has been stubbed.");
		printlnIndented(locationOfStubbing);
	}

	private void printCommonInfos(PrintableInvocation invocation) {
		printStream.println(invocation.toString());
//		printStream.println("Handling method call on a mock/spy.");
		printlnIndented(invocation.getLocation().toString());
	}

	private void printFooter() {
		printStream.println("###########################################################");
	}
	
	private void printlnIndented(String message) {
		printStream.println("   " + message);
	}
	
}
