/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;

@SuppressWarnings("unchecked")
public class MockitoTest extends TestBase {

    //TODO I want to have ruby script that will collect all java code from examples in javadoc/documentation
    // and create a test case that I can manually fix and run and make sure examples are valid.
    // Or better. Write examples as unit tests, annotate them in the way so that javadoc can be generated out of it.
    
    //TODO check if performace can be tweaked (e.g: LL -> AL)
    
    //TODO names for @Mock - take field names
    //TODO return 0 for Integer, etc
    
    @Test
    public void shouldRemoveStubbableFromProgressAfterStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.stub(mock.add("test")).toReturn(true);
        
        assertNull(Mockito.MOCKING_PROGRESS.pullOngoingStubbing());
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {
        Mockito.verify("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        Mockito.verify("notMock", times(19));
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
    public void shouldValidateMockWhenCreatingInOrderObject() {
        Mockito.inOrder("notMock");
    }
}
