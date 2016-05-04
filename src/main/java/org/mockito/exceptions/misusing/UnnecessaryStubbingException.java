package org.mockito.exceptions.misusing;

/**
 * Indicates presence of stubbings in the test class that were never used in the code during test execution.
 * Such stubbings should be removed from the test class to keep the test code clean.
 */
public class UnnecessaryStubbingException extends RuntimeException {
    public UnnecessaryStubbingException(String message) {
        super(message);
    }
}
