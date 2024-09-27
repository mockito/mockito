/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.creation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** Regression tests for issue #2303*/
public class PackagePrivateWithContextClassLoaderTest {

    private ClassLoader oldContextClassloader;

    public abstract static class PublicClass {
        int packagePrivateMethod() {
            return 0;
        }

        abstract void packagePrivateAbstractMethod();
    }

    public interface PublicInterfaceWithPackagePrivateMethodParam {
        void doSomething(PackagePrivateInterface i);
    }

    public interface PublicInterfaceWithPackagePrivateMethodReturn {
        PackagePrivateInterface doSomething();
    }

    public interface PublicInterfaceOverridesPackagePrivateMethodReturn {
        PublicChildOfPackagePrivate doSomething();
    }

    public interface PublicInterface {}

    interface PackagePrivateInterface {}

    public interface PublicChildOfPackagePrivate extends PackagePrivateInterface {}

    static class PackagePrivateClass {}

    @Before
    public void setUp() {
        oldContextClassloader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new ClassLoader(oldContextClassloader) {});
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
    public void should_be_able_to_mock_interface_method_package_private_param() throws Exception {
        PublicInterfaceWithPackagePrivateMethodParam publicClass =
                mock(PublicInterfaceWithPackagePrivateMethodParam.class);
        publicClass.doSomething(null);
    }

    @Test
    public void should_be_able_to_mock_interface_method_package_private_return() throws Exception {
        PublicInterfaceWithPackagePrivateMethodReturn publicClass =
                mock(PublicInterfaceWithPackagePrivateMethodReturn.class);
        PackagePrivateInterface packagePrivateInterface = publicClass.doSomething();
    }

    @Test
    public void should_be_able_to_mock_interface_method_package_private_return_override()
            throws Exception {
        PublicInterfaceOverridesPackagePrivateMethodReturn publicClass =
                mock(PublicInterfaceOverridesPackagePrivateMethodReturn.class);
        PackagePrivateInterface packagePrivateInterface = publicClass.doSomething();
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
        PackagePrivateInterface mock =
                (PackagePrivateInterface)
                        mock(
                                PublicInterface.class,
                                withSettings().extraInterfaces(PackagePrivateInterface.class));
    }

    /**
     * In this test we have a class that delegates loading of mockito/JDK classes to its parent,
     * but defines in its own for others. If mockito selects the defining classloader of the mock
     * to the classloader of mockito, calling the abstract package-private method will fail - the
     * defining classloader of the mocked type's package is different from the generated mock class
     * package. Because the nonDelegatingLoader is a child of mockito's loader, it's more specific
     * and should be preferred.
     */
    @Test
    public void classloader_with_parent_but_does_not_delegate() throws Exception {
        ClassLoader nonDelegatingLoader = new NotAlwaysDelegatingClassLoader();
        Thread.currentThread().setContextClassLoader(nonDelegatingLoader);
        Class<?> loaded =
                Class.forName(LoadedByCustomLoader.class.getName(), false, nonDelegatingLoader);
        Method attemptMock = loaded.getDeclaredMethod("attemptMock");
        attemptMock.invoke(null);
    }

    public static class LoadedByCustomLoader {
        public static void attemptMock() {
            PublicClass mock = mock(PublicClass.class);
            mock.packagePrivateAbstractMethod();
        }
    }

    /**
     * This classloader has a parent, but doesn't always delegate to it.
     */
    public static final class NotAlwaysDelegatingClassLoader extends ClassLoader {

        /**
         * Initial size of buffer used to read class data.
         */
        /*  Note: should be enough for most classes, and is not a hard limit. */
        private static final int BUF_SIZE = 4096;

        public NotAlwaysDelegatingClassLoader() {
            super(NotAlwaysDelegatingClassLoader.class.getClassLoader());
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // First, check if the class has already been loaded. If not, load it
            // ourselves or delegate to the parent.
            Class<?> result = findLoadedClass(name);
            if (result == null) {
                // All classes defined in this testsuite should be loaded by this classloader,
                // but any other class (e.g. those coming from java.* or org.mockito.* packages)
                // will be loaded by the parent.
                if (name.startsWith("org.mockitousage.")) {
                    result = findClass(name);
                } else {
                    return super.loadClass(name, resolve);
                }
            }
            if (resolve) {
                resolveClass(result);
            }
            return result;
        }

        @Override
        public Class<?> findClass(String className) throws ClassNotFoundException {
            try {
                // Create a package for this class, unless it's in the default package.
                int dotpos = className.lastIndexOf('.');
                if (dotpos != -1) {
                    String pkgname = className.substring(0, dotpos);
                    if (getPackage(pkgname) == null) {
                        definePackage(pkgname, null, null, null, null, null, null, null);
                    }
                }
                String resourceName = className.replace('.', File.separatorChar) + ".class";
                InputStream input = getSystemResourceAsStream(resourceName);
                if (input == null) {
                    throw new ClassNotFoundException("Couldn't find resource " + resourceName);
                }
                byte[] classData = loadClassData(input);
                return defineClass(className, classData, 0, classData.length, null);
            } catch (IOException e) {
                throw new ClassNotFoundException("Cannot load " + className, e);
            }
        }

        /**
         * Load class data from a given input stream.
         */
        private byte[] loadClassData(InputStream input) throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream(BUF_SIZE);
            byte[] buffer = new byte[BUF_SIZE];
            int readCount;
            while ((readCount = input.read(buffer, 0, BUF_SIZE)) >= 0) {
                output.write(buffer, 0, readCount);
            }
            return output.toByteArray();
        }
    }
}
