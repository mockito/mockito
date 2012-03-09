package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("unchecked")
public class StubbingWithDelegate {

	@Test
	public void when_not_stubbed_delegate_should_be_called() {
		List<String> delegatedList = new ArrayList<String>();
		delegatedList.add("un") ;

		List<String> mock = mock(List.class, withSettings().forwardTo(delegatedList)) ;

		mock.add("two") ;

        assertEquals(2, mock.size());
	}

	@Test
	public void when_stubbed_the_delegate_should_not_be_called() {
		List<String> delegatedList = new ArrayList<String>();
		delegatedList.add("un") ;
		List<String> mock = mock(List.class, withSettings().forwardTo(delegatedList)) ;

		doReturn(10).when(mock).size();

		mock.add("two") ;

		assertEquals(10, mock.size());
        assertEquals(2, delegatedList.size());
	}

	@Test
	public void delegate_should_not_be_called_when_stubbed2() {
		List<String> delegatedList = new ArrayList<String>();
		delegatedList.add("un") ;
		List<String> mockedList = mock(List.class, withSettings().forwardTo(delegatedList)) ;

		doReturn(false).when(mockedList).add(Mockito.anyString()) ;

        mockedList.add("two") ;

		assertEquals(1, mockedList.size()) ;
		assertEquals(1, delegatedList.size()) ;
	}
}
