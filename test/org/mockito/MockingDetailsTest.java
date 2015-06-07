package org.mockito;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

public class MockingDetailsTest extends TestBase {
    
    @Test
    @SuppressWarnings("unchecked")
    public void should_provide_invocations() {
        final List<String> methodsInvoked = new ArrayList<String>(Arrays.asList("add", "remove", "clear"));
        
        final List<String> mockedList = mock(List.class);
        
        mockedList.add("one");
        mockedList.remove(0);
        mockedList.clear();
        
        final MockingDetails mockingDetails = new MockitoCore().mockingDetails(mockedList);
        final Collection<Invocation> invocations = mockingDetails.getInvocations();
        
        assertNotNull(invocations);
        assertEquals(invocations.size(), 3);
        for (final Invocation method : invocations) {
            assertTrue(methodsInvoked.contains(method.getMethod().getName()));
            if (method.getMethod().getName().equals("add")) {
                assertEquals(method.getArguments().length, 1);
                assertEquals(method.getArguments()[0], "one");
            }
        }    
    }

    @Test
    public void should_handle_null_input() {
        //TODO SF, decide how to handle it and ensure the there is a top level integ test for the mockingDetails().getInvocations()
        //assertTrue(new MockitoCore().mockingDetails(null).getInvocations().isEmpty());
    }
}
