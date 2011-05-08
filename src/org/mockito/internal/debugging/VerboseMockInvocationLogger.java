package org.mockito.internal.debugging;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.InvocationListener;
import org.mockito.invocation.MethodCallReport;

import java.io.PrintStream;

/**
 * Logs all invocations to standard output.
 * 
 * Used for debugging interactions with a mock. 
 */
public class VerboseMockInvocationLogger implements InvocationListener {

    // visible for testing
	final PrintStream printStream;

	private int mockInvocationsCounter = 0;

    public VerboseMockInvocationLogger() {
        this(System.out);
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void reportInvocation(MethodCallReport methodCallReport) {
        if (methodCallReport.threwException()) {
            onCallWithThrowable(
                    methodCallReport.getInvocation(),
                    methodCallReport.getThrowable(),
                    methodCallReport.getLocationOfStubbing()
            );
        } else {
            onNormalCall(
                    methodCallReport.getInvocation(),
                    methodCallReport.getReturnedValue(),
                    methodCallReport.getLocationOfStubbing()
            );
        }
    }

    private void onNormalCall(PrintableInvocation invocation, Object returnedValue, String locationOfStubbing) {
		printHeader();
		printCommonInfos(invocation);
		printlnIndented("has returned: \"" + returnedValue + "\"" + ((returnedValue == null) ? "" : " (" + returnedValue.getClass().getName() + ")"));
		if (locationOfStubbing == null) {
			printMethodHasNotBeenStubbed();
		} else {
			printMethodHasBeenStubbed(locationOfStubbing);
		}
		printFooter();
	}

	private void onCallWithThrowable(PrintableInvocation invocation, Throwable throwable, String locationOfStubbing) {
		printHeader();
		printCommonInfos(invocation);
		printlnIndented("has thrown: " + throwable.getClass() + " with message " + throwable.getMessage());
		if (locationOfStubbing == null) {
			printMethodHasNotBeenStubbed();
		} else {
			printMethodHasBeenStubbed(locationOfStubbing);
		}
		printFooter();
	}

	private void printHeader() {
		mockInvocationsCounter++;
		printStream.println("############ Logging method invocation #" + mockInvocationsCounter + " on mock/spy ########");
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
		printStream.println("");
	}
	
	private void printlnIndented(String message) {
		printStream.println("   " + message);
	}
	
}
