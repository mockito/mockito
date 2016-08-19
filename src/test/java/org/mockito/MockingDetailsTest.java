package org.mockito;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

public class MockingDetailsTest extends TestBase {
    
    @Test
    public void should_provide_invocations() {
        List<String> methodsInvoked = new ArrayList<String>() {{
            add("add");
            add("remove");
            add("clear");
        }};

        @SuppressWarnings("unchecked")
        List<String> mockedList = (List<String>) mock(List.class);
        
        mockedList.add("one");
        mockedList.remove(0);
        mockedList.clear();
        
        MockingDetails mockingDetails = new MockitoCore().mockingDetails(mockedList);
        Collection<Invocation> invocations = mockingDetails.getInvocations();
        
        assertNotNull(invocations);
        assertEquals(invocations.size(),3);
        for (Invocation method : invocations) {
            assertTrue(methodsInvoked.contains(method.getMethod().getName()));
            if (method.getMethod().getName().equals("add")) {
                assertEquals(method.getArguments().length,1);
                assertEquals(method.getArguments()[0],"one");
            }
        }    
    }

    @Test
    public void getting_interactions_when_input_mock_is_null() {
        try {
            mockingDetails(null)
                    .getInvocations();
            fail();
        } catch (NotAMockException e) {
            assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void getting_interactions_when_input_mock_is_not_mock() {
        try {
            mockingDetails(new Object())
                    .getInvocations();
            fail();
        } catch (NotAMockException e) {
            assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class java.lang.Object!", e.getMessage());
        }
    }
}
