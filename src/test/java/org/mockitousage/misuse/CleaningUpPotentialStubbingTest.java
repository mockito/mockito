/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

public class CleaningUpPotentialStubbingTest extends TestBase {

    @Mock private IMethods mock;
    
    @Test
    public void shouldResetOngoingStubbingOnVerify() {
        // first test
        mock.booleanReturningMethod();
        verify(mock).booleanReturningMethod();
        
        // second test
        assertOngoingStubbingIsReset();
    }

    @Test
    public void shouldResetOngoingStubbingOnInOrder() {
        mock.booleanReturningMethod();
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).booleanReturningMethod();
        assertOngoingStubbingIsReset();
    }
    
    @Test
    public void shouldResetOngoingStubbingOnDoReturn() {
        mock.booleanReturningMethod();
        doReturn(false).when(mock).booleanReturningMethod();
        assertOngoingStubbingIsReset();
    }

    private void assertOngoingStubbingIsReset() {
        try {
            //In real, there might be a call to real object or a final method call
            //I'm modelling it with null
            when(null).thenReturn("anything");
            fail();
        } catch (MissingMethodInvocationException e) {}
    }
}