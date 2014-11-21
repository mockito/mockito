package org.mockito.release.util;

/**
 * Utility for validation of arguments
 */
public abstract class ArgumentValidation {

    /**
     * None of the input targets can not be null otherwise IllegalArgumentException is thrown.
     * Every second argument must a String describing the previous element.
     */
    public static void notNull(Object ... targets) {
        if (targets.length % 2 != 0) {
            throw new IllegalArgumentException("notNull method requires pairs of argument + message");
        }

        boolean nullFound = false;
        for (Object t : targets) {
            if (t == null) {
                nullFound = true;
                continue;
            }
            if (nullFound) {
                if (!(t instanceof String)) {
                    throw new IllegalArgumentException("notNull method requires pairs of argument + message");
                }
                throw new IllegalArgumentException(((String) t).concat(" cannot be null."));
            }
        }
    }
}
