package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;

/**
* This predicate is used to filter "good" {@link StackTraceElement}. Good 
*/
public class DefaultStackTraceCleaner implements StackTraceCleaner {
    

	@Override
	public boolean apply(StackTraceElement candidate) {
		String className = candidate.getClassName();
		
		if (isFromMockitoRunner(className))
			return true;
		
		if (isFromMockitoRule(className))
			return true;
		
	    if (isFromMockito(className)){
	    	if (isTest(className))
	    		return true;
	    	return false;
	    }
	   
	    if (isMock(className))
	    	return false;
	    
	    return true;
	}

	private boolean isTest(String className) {
		return className.endsWith("Test");
	}

	private boolean isMock(String className) {
		return  (className.contains("$$EnhancerByMockitoWithCGLIB$$")|| className.contains("$MockitoMock$"));
	}

	private boolean isFromMockito(String className) {
		return className.startsWith("org.mockito.");
	}

	private boolean isFromMockitoRule(String className) {
		return className.startsWith("org.mockito.internal.junit.JUnitRule");
	}

	private boolean isFromMockitoRunner(String className) {
		return className.startsWith("org.mockito.internal.runners.") || className.startsWith("org.mockito.runners.");
	}
}
