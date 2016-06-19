package org.mockitousage.plugins.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.plugins.StackTraceCleanerProvider;

/**
 * By Szczepan Faber on 9/15/12
 */
public class MyStackTraceCleanerProvider implements StackTraceCleanerProvider {

    public static boolean ENABLED = true;

    public StackTraceCleaner getStackTraceCleaner(final StackTraceCleaner defaultCleaner) {
        return new StackTraceCleaner() {
			@Override
			public boolean isIn(StackTraceElement candidate) {
				if (ENABLED && candidate.getMethodName().contains("excludeMe")) {
                    return false;
                }
                return defaultCleaner.isIn(candidate);
			}
        };
    }
}