/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.creation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Regression tests for issue #2303*/
public class PackagePrivateWithContextClassLoaderTest {

    private ClassLoader oldContextClassloader;

    public static class PublicClass {

        int packagePrivateMethod() {
            return 0;
        }
    }

    public interface PublicInterface {

    }

    interface PackagePrivateInterface {

    }

    static class PackagePrivateClass {

    }

    @Before
    public void setUp() {
        oldContextClassloader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new ClassLoader(oldContextClassloader) {
        });
    }

    @After
    public void teardown() {
        Thread.currentThread().setContextClassLoader(oldContextClassloader);
    }

    @Test
    public void should_be_able_to_mock_package_private_method() throws Exception {
        PublicClass publicClass = mock(PublicClass.class);
        when(publicClass.packagePrivateMethod()).thenReturn(3);
        assertThat(publicClass.packagePrivateMethod()).isEqualTo(3);
    }

    @Test
    public void should_be_able_to_mock_package_private_class() throws Exception {
        PackagePrivateClass mock = mock(PackagePrivateClass.class);
    }

    @Test
    public void should_be_able_to_mock_package_private_interface() throws Exception {
        PackagePrivateInterface mock = mock(PackagePrivateInterface.class);
    }

    @Test
    public void should_be_able_to_mock_package_private_extra_interface() throws Exception {
        PackagePrivateInterface mock = (PackagePrivateInterface) mock(PublicInterface.class,
            withSettings().extraInterfaces(PackagePrivateInterface.class));
    }
}
