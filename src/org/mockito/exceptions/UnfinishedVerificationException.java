package org.mockito.exceptions;

public class UnfinishedVerificationException extends MockitoException {
    
    private static final long serialVersionUID = 1L;

    public UnfinishedVerificationException() {
        super(  "\n" +
        		"Previous verify(mock) doesn't have a method call." +
        		"\n" +
        		"Should be something like that: verify(mock).doSomething()");
    }
}
