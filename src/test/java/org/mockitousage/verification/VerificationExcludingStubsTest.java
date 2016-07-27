/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class VerificationExcludingStubsTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void shouldAllowToExcludeStubsForVerification() throws Exception {
        //given
        when(mock.simpleMethod()).thenReturn("foo");

        //when
        String stubbed = mock.simpleMethod(); //irrelevant call because it is stubbing
        mock.objectArgMethod(stubbed);

        //then
        verify(mock).objectArgMethod("foo");

        //verifyNoMoreInteractions fails:
        try { verifyNoMoreInteractions(mock); fail(); } catch (NoInteractionsWanted e) {}
        
        //but it works when stubs are ignored:
        ignoreStubs(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldExcludeFromVerificationInOrder() throws Exception {
        //given
        when(mock.simpleMethod()).thenReturn("foo");

        //when
        mock.objectArgMethod("1");
        mock.objectArgMethod("2");
        mock.simpleMethod(); //calling the stub

        //then
        InOrder inOrder = inOrder(ignoreStubs(mock));
        inOrder.verify(mock).objectArgMethod("1");
        inOrder.verify(mock).objectArgMethod("2");
        inOrder.verifyNoMoreInteractions();
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void shouldIgnoringStubsDetectNulls() throws Exception {
        ignoreStubs(mock, null);
    }

    @Test(expected = NotAMockException.class)
    public void shouldIgnoringStubsDetectNonMocks() throws Exception {
        ignoreStubs(mock, new Object());
    }

}