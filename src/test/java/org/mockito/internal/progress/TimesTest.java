/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;


public class TimesTest  {
	@Rule
	public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldNotAllowNegativeNumberOfInvocations() throws Exception {
       
    	exception.expect(MockitoException.class);
    	exception.expectMessage("Negative value is not allowed here");
        
    	VerificationModeFactory.times(-50);
    }
}
