package org.mockito.usage;

import static org.mockito.Mockito.*;

import java.util.List;

import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class MockitoSyntaxExperiments {
    
    public void goodOldEasyMockVerifySyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");

        Mockito.verify(mock).add(2, "test2");
        Mockito.verify(mock, 5).add(2, "test2");
            //or
//            Mockito.verify(mock).times(5).add(2, "test2");
        Mockito.verifyNoMoreInteractions(mock);
        Mockito.verifyZeroInteractions(mock);
    }
    
    public void oldSchoolAssertSyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");

        //Second for old style assertions
        Mockito.assertInvoked(mock).add(2, "test2");
        Mockito.assertInvoked(mock, 5).add(2, "test2");
            //or
//            Mockito.assertInvoked(mock).times(5).add(2, "test2");
        Mockito.assertNoMoreInteractions(mock);
        Mockito.assertZeroInteractions(mock);
    }
    
    public void shinyNewAssertThatSyntax() {
        List mock = mock(List.class);
        
        //stub it
        stub(mock.add("test")).andReturn(true);
        
        //use it
        mock.add(2, "test2");
        
        Mockito.assertThat(wasInvoked(mock)).add(2, "test2");
        Mockito.assertThat(wasInvoked(mock, 5)).add(2, "test2");
        Mockito.assertThat(noMoreInteractions(mock));
        Mockito.assertThat(zeroInteractions(mock));
    }
    
    public void verifiesMocksInOrder() {
//        Mockito.verifyInOrder(new Ordering() { void sequence() {
//            Mockito.verify(mock).clear();
//            Mockito.verify(mock2).clear();
//        }};);
    }
}
