/*
 * Copyright (c) 2003-2006 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

import static org.easymock.internal.ClassExtensionHelper.getInterceptor;

import java.lang.reflect.Method;

import org.easymock.internal.MocksControl;

/**
 * Instances of <code>MockClassControl</code> control the behavior of their
 * associated mock objects. For more information, see the EasyMock
 * documentation.
 * 
 * @see <a href="http://www.easymock.org/">EasyMock</a>
 * @deprecated Use org.easymock.classextension.EasyMock instead
 */
public class MockClassControl<T> extends MockControl<T> {

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createControl(Class<T> classToMock) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock.createControl(),
                classToMock);
    }

    /**
     * Same as {@link #createControl(Class)}but allows to pass a list of
     * methods to mock. All the other methods won't be. It means that if these
     * methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Method[] mockedMethods) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock.createControl(),
                classToMock, mockedMethods);
    }

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will check the
     * order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionFailedError</code>.
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createStrictControl(Class<T> classToMock) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock
                        .createStrictControl(), classToMock);
    }

    /**
     * Same as {@link #createStrictControl(Class)}but allows to pass a list of
     * methods to mock. All the other methods won't be. It means that if these
     * methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Method[] mockedMethods) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock
                        .createStrictControl(), classToMock, mockedMethods);
    }

    /**
     * Creates a mock control object for the specified class or interface. The
     * {@link MockClassControl}and its associated mock object will check not
     * the order of expected method calls. An unexpected method call on the mock
     * object will return an empty value (0, null, false).
     * 
     * @param classToMock
     *            the class to mock.
     * @return the mock control (which is a {@link MockClassControl}instance)
     */
    public static <T> MockControl<T> createNiceControl(Class<T> classToMock) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock.createNiceControl(),
                classToMock);
    }

    /**
     * Same as {@link #createNiceControl(Class, Method[])}but allows to pass a
     * list of methods to mock. All the other methods won't be. It means that if
     * these methods are called, their real code will be executed.
     * 
     * @param classToMock
     *            the class to mock
     * @param mockedMethods
     *            Methods to be mocked. If null, all methods will be mocked.
     * @return the mock control
     */
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Method[] mockedMethods) {
        return new MockClassControl<T>(
                (MocksControl) EasyMock.createNiceControl(),
                classToMock, mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createControl(Class)}
     */
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Class[] constructorTypes, Object[] constructorArgs) {
        return (MockClassControl<T>) createControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createControl(Class, Method[])}
     */
    public static <T> MockClassControl<T> createControl(Class<T> classToMock,
            Class[] constructorTypes, Object[] constructorArgs,
            Method[] mockedMethods) {
        return (MockClassControl<T>) createControl(classToMock, mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createStrictControl(Class)}
     */
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Class[] constructorTypes,
            Object[] constructorArgs) {
        return (MockClassControl<T>) createStrictControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createStrictControl(Class, Method[])}
     */
    public static <T> MockClassControl<T> createStrictControl(
            Class<T> classToMock, Class[] constructorTypes,
            Object[] constructorArgs, Method[] mockedMethods) {
        return (MockClassControl<T>) createStrictControl(classToMock,
                mockedMethods);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use {@link #createNiceControl(Class)}
     */
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Class[] constructorTypes,
            Object[] constructorArgs) {
        return (MockClassControl<T>) createNiceControl(classToMock);
    }

    /**
     * @deprecated No need to pick a constructor anymore. Constructor arguments
     *             are now ignored. Just use
     *             {@link #createNiceControl(Class, Method[])}
     */
    public static <T> MockClassControl<T> createNiceControl(
            Class<T> classToMock, Class[] constructorTypes,
            Object[] constructorArgs, Method[] mockedMethods) {
        return (MockClassControl<T>) createNiceControl(classToMock,
                mockedMethods);
    }

    private MockClassControl(MocksControl ctrl, Class<T> classToMock,
            Method[] mockedMethods) {
        super(ctrl, classToMock);
        // Set the mocked methods on the interceptor
        getInterceptor(getMock()).setMockedMethods(mockedMethods);
    }

    private MockClassControl(MocksControl ctrl, Class<T> classToMock) {
        super(ctrl, classToMock);
    }
}
