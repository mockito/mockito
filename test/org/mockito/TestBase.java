/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.mockito.util.Assertor;

/**
 * the easiest way to make sure that tests clean up invalid state is to require
 * valid state for all tests.
 */
public class TestBase {

    @Before
    public void init() {
        Mockito.MOCKING_PROGRESS.validateState();
        MockitoAnnotations.initMocks(this);
    }
    
	//I'm really tired of matchers, enter the assertor!
	protected <T> void assertThat(T o, Assertor<T> a) {
		a.assertValue(o);
	}
	
	protected <T> void assertThat(T actual, Matcher<T> m) {
		org.junit.Assert.assertThat(actual, m);
	}
	
	protected <T> void assertThat(String message, T actual, Matcher<T> m) {
		org.junit.Assert.assertThat(message, actual, m);
	}
	
    public static <T> Assertor<Throwable> messageContains(final String text) {
        return new Assertor<Throwable>() {
        	public void assertValue(Throwable value) {
        		assertTrue("String: " + value.getMessage() + " should contain: " + text
        				, ((Throwable) value).getMessage().contains(text));
        	}
        };
    }
    
    public static <T> Assertor<Throwable> causeMessageContains(final String text) {
        return new Assertor<Throwable>() {
        	public void assertValue(Throwable value) {
        		Throwable cause = ((Throwable) value).getCause();
        		assertNotNull("Exception cause should not be null", cause);
        		assertTrue("\nException message >>>" + cause.getMessage() + "\n>>> should contain: " + text,
        				cause.getMessage().contains(text));
        	}
        };
    }
}