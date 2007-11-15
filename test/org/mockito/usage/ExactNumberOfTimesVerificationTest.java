package org.mockito.usage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;
import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class ExactNumberOfTimesVerificationTest {

    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanExpected() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 100).clear();
            fail();
        } catch (AssertionError error) {
            assertThat(error.getMessage(), equalTo("Expected to be invoked 100 times but was 3"));
        }
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanExpected() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 1).clear();
            fail();
        } catch (AssertionError error) {
            assertThat(error.getMessage(), equalTo("Expected to be invoked 1 times but was 3"));
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasNotInvoked() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);

        Mockito.verify(mock, 0).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (AssertionError error) {
            assertThat(error.getMessage(), equalTo("Expected to be invoked 15 times but was 0"));
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasInvokedOnce() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);

        mock.clear();
        
        Mockito.verify(mock, 1).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (AssertionError error) {
            assertThat(error.getMessage(), equalTo("Expected to be invoked 15 times but was 1"));
        }
    }
    
    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        LinkedList mock = Mockito.mock(LinkedList.class);
        
        Mockito.stub(mock.add("test")).andReturn(false);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        mock.add("test");
        mock.add("test");
        
        Mockito.verify(mock, 2).add("test");
    }
}
