package org.mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.Invocation;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockingDetailsTest {
	
	@Test
	public void testGetInvocations() {
		List<String> methodsInvoked = new ArrayList<String>() {{
			add("add");
			add("remove");
			add("clear");
		}};
		
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
}
