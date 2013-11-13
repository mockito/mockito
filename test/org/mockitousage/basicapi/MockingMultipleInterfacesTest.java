/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MockingMultipleInterfacesTest extends TestBase {

    class Foo {}
    interface IFoo {}
    interface IBar {}
    
    @Test
    public void shouldAllowMultipleInterfaces() {
        //when
        Foo mock = mock(Foo.class, withSettings().extraInterfaces(IFoo.class, IBar.class));
        
        //then
        assertThat(mock, is(IFoo.class));
        assertThat(mock, is(IBar.class));
    }
    
    @Test
    public void shouldScreamWhenNullPassedInsteadOfAnInterface() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces(IFoo.class, null));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() does not accept null parameters", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNoArgsPassed() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces());
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() requires at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNullPassedInsteadOfAnArray() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces((Class[]) null));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("extraInterfaces() requires at least one interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenNonInterfacePassed() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces(Foo.class));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("Foo which is not an interface", e.getMessage());
        }
    }
    
    @Test
    public void shouldScreamWhenTheSameInterfacesPassed() {
        try {
            //when
            mock(IMethods.class, withSettings().extraInterfaces(IMethods.class));
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("You mocked following type: IMethods", e.getMessage());
        }
    }
    
	@Test
	public void shouldMockClassWithInterfacesOfDifferentClassloader()
			throws ClassNotFoundException {
		// from test-resources/multiple-interfaces/
		Class<?> interface1 = new ClassLoader1().loadClass("test.TestedClass1");
		Class<?> interface2 = new Classloader2().loadClass("test.TestedClass2");

		try {
			Object mocked = Mockito.mock(interface1, Mockito.withSettings()
					.extraInterfaces(interface2));
			assertTrue(interface2.isInstance(mocked));
		} catch (MockitoException e) {
			fail("Cannot mock interfaces with different classloaders");
		}

	}

	final class ClassLoader1 extends ClassLoader {
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (name.equals("test.TestedClass1")) {
				try {
					File file = new File(
							"test-resources/multiple-interfaces/TestedClass1.class");
					byte[] bytes = new byte[(int) file.length()];
					FileInputStream fileInputStream = new FileInputStream(file);
					try {
						fileInputStream.read(bytes);
						return defineClass("test.TestedClass1", bytes, 0,
								bytes.length);
					} finally {
						fileInputStream.close();
					}
				} catch (IOException e) {
					throw new ClassNotFoundException("Cannot create class: TestedClass1.class", e);
				}
			}
			return super.loadClass(name);
		}
	}

	final class Classloader2 extends ClassLoader {
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (name.equals("test.TestedClass2")) {
				try {
					File file = new File(
							"test-resources/multiple-interfaces/TestedClass2.class");
					byte[] bytes = new byte[(int) file.length()];
					FileInputStream fileInputStream = new FileInputStream(file);
					try {
						fileInputStream.read(bytes);
						return defineClass("test.TestedClass2", bytes, 0,
								bytes.length);
					} finally {
						fileInputStream.close();
					}

				} catch (IOException e) {
					throw new ClassNotFoundException("Cannot create class: TestedClass1.class", e);
				}
			}
			return super.loadClass(name);
		}
	}
}