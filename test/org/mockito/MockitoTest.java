/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;

@SuppressWarnings("unchecked")
public class MockitoTest extends RequiresValidState {

    @Test
    public void shouldRemoveStubbedControlFromStateWhenStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        assertNull(Mockito.mockingProgress.pullStubable());
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {
        Mockito.verify("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        Mockito.verify("notMock", 19);
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingZeroInteractions() {
        Mockito.verifyZeroInteractions("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenStubbingVoid() {
        Mockito.stubVoid("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenGettingStrictVerifier() {
        Mockito.createStrictOrderVerifier("notMock");
    }
}
