package org.mockito.internal.util;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMockingDetailsTest {

	@Mock
	private Foo	foo;
	@Mock
	private Bar bar;
	@Spy
	private Gork gork;
	private Foo anotherFoo	= new Foo();

	@Test
	public void testMockIsMock() throws Exception {
		assertEquals(true, Mockito.mockingDetails(foo).isMock());
		assertEquals(true, Mockito.mockingDetails(bar).isMock());
		assertEquals(true, Mockito.mockingDetails(gork).isMock());
	}
	
	@Test
	public void testNotMockIsNotMock() throws Exception {
		assertEquals(false, Mockito.mockingDetails(anotherFoo).isMock());
	}
	
	@Test
	public void testSpyIsSpy() throws Exception {
		assertEquals(true, Mockito.mockingDetails(gork).isSpy());
	}
	
	@Test
	public void testNotSpyIsNotSpy() throws Exception {
		assertEquals(false, Mockito.mockingDetails(foo).isSpy());
		assertEquals(false, Mockito.mockingDetails(bar).isSpy());
		assertEquals(false, Mockito.mockingDetails(anotherFoo).isSpy());
	}
	
	@Test
	public void testGetRealClassNotInterface() throws Exception {
		assertEquals(Foo.class, Mockito.mockingDetails(foo).getRealClass());
	}

	@Test
	public void testGetRealClassInterface() throws Exception {
		assertEquals(Bar.class, Mockito.mockingDetails(bar).getRealClass());
	}

	public static class Foo { }

	public static interface Bar { }
	
	public static class Gork { }
}
