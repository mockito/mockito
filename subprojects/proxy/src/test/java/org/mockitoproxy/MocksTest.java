/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoproxy;

import net.bytebuddy.ClassFileVersion;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void can_call_default_method_java_16() {
        if (ClassFileVersion.ofThisVm().isLessThan(ClassFileVersion.JAVA_V16)) {
            return; // Not supported prior to Java 16.
        }

        SomeInterface mock = Mockito.mock(SomeInterface.class);
        assertThat(mock, instanceOf(Proxy.class));

        when(mock.m()).thenCallRealMethod();

        assertThat(mock.d(), is("default"));

        verify(mock).d();
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

    @Test(expected = MockitoException.class)
    public void cannot_create_mock_of_non_object_class() {
        Number number = Mockito.mock(Number.class);
    }

    public interface SomeInterface {

        Object m();

        default Object d() {
            return "default";
        }
    }
}
