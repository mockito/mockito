/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoproxy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

public class MocksTest {

    @Test
    public void can_create_mock_interface() {
        SomeInterface mock = Mockito.mock(SomeInterface.class);
        assertThat(mock, instanceOf(Proxy.class));

        when(mock.m()).thenReturn("value");

        assertThat(mock.m(), is("value"));

        verify(mock).m();
    }

    @Test
    public void can_create_mock_object() {
        Object mock = Mockito.mock(Object.class);
        assertThat(mock, instanceOf(Proxy.class));

        when(mock.toString()).thenReturn("value");

        assertThat(mock.toString(), is("value"));
    }

    @Test
    public void can_call_default_method_if_available() {
        SomeInterface mock = Mockito.mock(SomeInterface.class);
        assertThat(mock, instanceOf(Proxy.class));

        when(mock.d()).thenCallRealMethod();

        assertThat(mock.d(), is("default"));

        verify(mock).d();
    }

    @Test
    public void cannot_call_default_method_if_not_available() {
        SomeInterface mock = Mockito.mock(SomeInterface.class);
        assertThat(mock, instanceOf(Proxy.class));

        try {
            when(mock.m()).thenCallRealMethod();
            fail("Expected failure when requesting real method for non-default method");
        } catch (MockitoException e) {
            assertThat(e.getMessage(), CoreMatchers.containsString("real method"));
        }
    }

    @Test
    public void can_create_mock_different_class_loader() throws Exception {
        ClassLoader loader = new ClassLoader(null) {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.startsWith(MocksTest.class.getPackage().getName())) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try (InputStream inputStream = MocksTest.class.getClassLoader()
                        .getResourceAsStream(name.replace('.', '/') + ".class")) {
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        throw new AssertionError(e);
                    }
                    byte[] classFile = outputStream.toByteArray();
                    return defineClass(name, classFile, 0, classFile.length);
                }
                return super.loadClass(name);
            }
        };


        Object mock = Mockito.mock(Class.forName(SomeInterface.class.getName(), true, loader));
        assertThat(mock, instanceOf(Proxy.class));

        when(mock.toString()).thenReturn("value");

        assertThat(mock.toString(), is("value"));
    }

    @Test
    public void cannot_create_mock_of_non_object_class() {
        assertThatThrownBy(
                () -> {
                    Number number = Mockito.mock(Number.class);
                })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Cannot mock/spy class",
                        "Mockito cannot mock/spy because :",
                        " - non-interface");
    }

    public interface SomeInterface {

        Object m();

        default Object d() {
            return "default";
        }
    }
}
