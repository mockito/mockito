/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.mockito.exceptions.*;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
public class MockitoTest {

    private String notMock;
    private List mock;

    @Before
    public void setup() {
        mock = mock(List.class);
        notMock = "i'm not a notMock";
        StateResetter.reset();
    }
    
    @After
    public void resetState() {
        StateResetter.reset();
    }
    
    @Test
    public void shouldRemoveStubbedControlFromStateWhenStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        assertNull(MockitoState.instance().pullControlToBeStubbed());
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {
        Mockito.verify(notMock);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        Mockito.verify(notMock, 19);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(notMock);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingZeroInteractions() {
        Mockito.verifyZeroInteractions(notMock);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenStubbingVoid() {
        Mockito.stubVoid(notMock);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenGettingStrictVerifier() {
        Mockito.strictOrderVerifier(notMock);
    }
    
    @Test
    public void shouldDetectUnfinishedVerification() {
        verify(mock);
        try {
            verify(mock).get(0);
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedVerificationWhenVeryfingNoMoreInteractions() {
        verify(mock);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedVerificationWhenVeryfingZeroInteractions() {
        verify(mock);
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (MockitoException e) {}
    }
}
