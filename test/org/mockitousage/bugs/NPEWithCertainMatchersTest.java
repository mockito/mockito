/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.AdditionalMatchers.*;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class NPEWithCertainMatchersTest extends TestBase {

    @Mock IMethods mock;
    
    @After
    public void clearState() {
        this.resetState();
    }

    @Test
    public void shouldNotThrowNPEWhenIntegerPassed() {
        mock.intArgumentMethod(100);

        verify(mock).intArgumentMethod(isA(Integer.class));
    }

    @Test
    public void shouldNotThrowNPEWhenIntPassed() {
        mock.intArgumentMethod(100);
        
        verify(mock).intArgumentMethod(isA(Integer.class));
    }
    
    @Test
    public void shouldNotThrowNPEWhenIntegerPassedToEq() {
        mock.intArgumentMethod(100);
        
        verify(mock).intArgumentMethod(eq(new Integer(100)));
    }

    @Test
    public void shouldNotThrowNPEWhenIntegerPassedToSame() {
        mock.intArgumentMethod(100);

        verify(mock, never()).intArgumentMethod(same(new Integer(100)));
    }
}