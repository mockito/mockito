/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoExperimental.*;

import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoSyntaxExperiments {
    
    public void goodOldEasyMockVerifySyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");

        verify(mock).add(2, "test2");
        verify(mock, 5).add(2, "test2");
            //or
//            verify(mock).times(5).add(2, "test2");
        verifyNoMoreInteractions(mock);
        verifyZeroInteractions(mock);
    }
    
    public void oldSchoolAssertSyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");

        //Second for old style assertions
        assertInvoked(mock).add(2, "test2");
        assertInvoked(mock, 5).add(2, "test2");
            //or
//            assertInvoked(mock).times(5).add(2, "test2");
        assertNoMoreInteractions(mock);
        assertZeroInteractions(mock);
    }
    
    public void shinyNewAssertThatSyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");
        
        assertThat(wasInvoked(mock)).add(2, "test2");
        assertThat(wasInvoked(mock, 5)).add(2, "test2");
        assertThat(noMoreInteractions(mock));
        assertThat(zeroInteractions(mock));
    }
}
