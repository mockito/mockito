/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockitoutil.ClassLoaders.isolatedClassLoader;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

// See issue 453
public class MockingMultipleInterfacesTest extends TestBase {

    class Foo {}
    interface IFoo {}
    interface IBar {}
    
    @Test
    public void should_allow_multiple_interfaces() {
        //when
        Foo mock = mock(Foo.class, withSettings().extraInterfaces(IFoo.class, IBar.class));
        
        //then
        assertThat(mock, is(IFoo.class));
        assertThat(mock, is(IBar.class));
    }
    
    @Test
    public void should_scream_when_null_passed_instead_of_an_interface() {
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
    public void should_scream_when_no_args_passed() {
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
    public void should_scream_when_null_passed_instead_of_an_array() {
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
    public void should_scream_when_non_interface_passed() {
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
    public void should_scream_when_the_same_interfaces_passed() {
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
    public void should_mock_class_with_interfaces_of_different_class_loader_AND_different_classpaths() throws ClassNotFoundException {
        // Note : if classes are in the same classpath, SearchingClassLoader can find the class/classes and load them in the first matching classloader
        Class<?> interface1 = isolatedClassLoader()
                .withCodeSourceUrls("test-resources/multiple-classpaths/cp1")
                .withPrivateCopyOf("test.TestedClass1")
                .build()
                .loadClass("test.TestedClass1");
        Class<?> interface2 = isolatedClassLoader()
                .withCodeSourceUrls("test-resources/multiple-classpaths/cp2")
                .withPrivateCopyOf("test.TestedClass2")
                .build()
                .loadClass("test.TestedClass2");

        try {
            Object mocked = mock(interface1, withSettings().extraInterfaces(interface2));
            assertTrue("mock should be assignable from interface2 type", interface2.isInstance(mocked));
        } catch (MockitoException e) {
            fail("Couldn't mock interfaces with different class loaders and different classpaths");
        }
    }
}