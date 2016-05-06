package org.mockito.exceptions.misusing;

/**
 * Indicates presence of stubbings in the test class that were never used in the code during test execution.
 * Such stubbings should be removed from the test class to keep the test code clean.
 *
 * TODO this javadoc needs to better describe why it is important, what to do to work with it.
 */
public class UnnecessaryStubbingException extends RuntimeException {
    public UnnecessaryStubbingException(String message) {
        super(message);
    }
}
