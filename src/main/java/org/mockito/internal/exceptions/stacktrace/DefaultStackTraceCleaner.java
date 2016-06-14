package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;

/**
* This predicate is used to filter "good" {@link StackTraceElement}. Good 
*/
public class DefaultStackTraceCleaner implements StackTraceCleaner {

	@Override
	public boolean isIn(StackTraceElement e) {
		if (isFromMockitoRunner(e.getClassName()) || isFromMockitoRule(e.getClassName())) {
			return true;
		} else if (isMockDispatcher(e.getClassName()) || isFromMockito(e.getClassName())) {
			return false;
		} else {
			return true;
		}
	}

	private static boolean isMockDispatcher(String className) {
		return  (className.contains("$$EnhancerByMockitoWithCGLIB$$")|| className.contains("$MockitoMock$"));
	}

	private static boolean isFromMockito(String className) {
		return className.startsWith("org.mockito.");
	}

	private static boolean isFromMockitoRule(String className) {
		return className.startsWith("org.mockito.internal.junit.JUnitRule");
	}

	private static boolean isFromMockitoRunner(String className) {
		return className.startsWith("org.mockito.internal.runners.") || className.startsWith("org.mockito.runners.");
	}
}
