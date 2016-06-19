package org.mockito;

import org.junit.Test;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

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
    public void should_handle_null_input() {
        //TODO SF, decide how to handle it and ensure the there is a top level integ test for the mockingDetails().getInvocations()
        //assertTrue(new MockitoCore().mockingDetails(null).getInvocations().isEmpty());
    }
}
