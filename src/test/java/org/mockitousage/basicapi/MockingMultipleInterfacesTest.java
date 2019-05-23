/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockitoutil.ClassLoaders.inMemoryClassLoader;
import static org.mockitoutil.SimpleClassGenerator.makeMarkerInterface;

// See issue 453
public class MockingMultipleInterfacesTest {

    class Foo {}
    interface IFoo {}
    interface IBar {}

    @Test
    public void should_allow_multiple_interfaces() {
        //when
        Foo mock = mock(Foo.class, withSettings().extraInterfaces(IFoo.class, IBar.class));

        //then
        assertThat(mock).isInstanceOf(IFoo.class);
        assertThat(mock).isInstanceOf(IBar.class);
    }

    @Test
    public void should_scream_when_null_passed_instead_of_an_interface() {
        try {
            //when
            mock(Foo.class, withSettings().extraInterfaces(IFoo.class, null));
            fail();
        } catch (MockitoException e) {
            //then
            assertThat(e.getMessage()).contains("extraInterfaces() does not accept null parameters");
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
            assertThat(e.getMessage()).contains("extraInterfaces() requires at least one interface");
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
            assertThat(e.getMessage()).contains("extraInterfaces() requires at least one interface");
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
            assertThat(e.getMessage()).contains("Foo which is not an interface");
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
            assertThat(e.getMessage()).contains("You mocked following type: IMethods");
        }
    }

    @Test
    public void should_mock_class_with_interfaces_of_different_class_loader_AND_different_classpaths() throws ClassNotFoundException {
        // Note : if classes are in the same classpath, SearchingClassLoader can find the class/classes and load them in the first matching classloader
        Class<?> interface1 = inMemoryClassLoader()
                .withClassDefinition("test.Interface1", makeMarkerInterface("test.Interface1"))
                .build()
                .loadClass("test.Interface1");
        Class<?> interface2 = inMemoryClassLoader()
                .withClassDefinition("test.Interface2", makeMarkerInterface("test.Interface2"))
                .build()
                .loadClass("test.Interface2");

        Object mocked = mock(interface1, withSettings().extraInterfaces(interface2));
        assertThat(interface2.isInstance(mocked)).describedAs("mock should be assignable from interface2 type").isTrue();
    }
}
