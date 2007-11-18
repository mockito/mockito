package org.mockito.exceptions;

public class UnfinishedVerificationException extends RuntimeException {
    
    public UnfinishedVerificationException() {
        super(  "\n" +
        		"Previous verify(mock) doesn't have a method call." +
        		"\n" +
        		"Should be something like that: verify(mock).doSomething()");
    }
}
