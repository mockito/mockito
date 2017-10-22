/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.MockitoHint;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;

/**
 * Provides mocking information.
 * For example, you can identify whether a particular object is either a mock or a spy.
 * For examples and more information please refer to the javadoc of the individual methods on this class.
 *
 * @since 1.9.5
 */
public interface MockingDetails {

    /**
     * Informs if the object is a mock. isMock() for null input returns false.
     * @return true if the object is a mock or a spy (spy is a different kind of mock, but it is still a mock).
     *
     * @since 1.9.5
     */
    boolean isMock();

    /**
     * Informs if the object is a spy. isSpy() for null input returns false.
     * @return true if the object is a spy.
     *
     * @since 1.9.5
     */
    boolean isSpy();

    /**
     * All method invocations on this mock.
     * Can be empty - it means there were no interactions with the mock.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     * <p>
     * Manipulating the collection (e.g. by removing, adding elements) is safe and has no effect on the mock.
     * <p>
     * Throws meaningful exception when object wrapped by MockingDetails is not a mock.
     *
     * @since 1.10.0
     */
    Collection<Invocation> getInvocations();

    /**
     * Returns various mock settings provided when the mock was created, for example:
     *  mocked class, mock name (if any), any extra interfaces (if any), etc.
     * See also {@link MockCreationSettings}.
     * <p>
     * This method is useful for framework integrators and for certain edge cases.
     * <p>
     * If <code>null</code> or non-mock was passed to {@link Mockito#mockingDetails(Object)}
     * then this method will throw with an appropriate exception.
     * After all, non-mock objects do not have any mock creation settings.
     * @since 2.1.0
     */
    MockCreationSettings<?> getMockCreationSettings();

    /**
     * Returns stubbings declared on this mock object.
     * <pre class="code"><code class="java">
     *   Mockito.mockingDetails(mock).getStubbings()
     * </code></pre>
     * What is 'stubbing'?
     * Stubbing is your when(x).then(y) declaration, e.g. configuring the mock to behave in a specific way,
     * when specific method with specific arguments is invoked on a mock.
     * Typically stubbing is configuring mock to return X when method Y is invoked.
     * <p>
     * Why do you need to access stubbings of a mock?
     * In a normal workflow of creation clean tests, there is no need for this API.
     * However, it is useful for advanced users, edge cases or framework integrators.
     * For example, Mockito internally uses this API to report and detect unused stubbings
     * that should be removed from test. Unused stubbings are dead code that needs to be removed
     * (see {@link MockitoHint}).
     * <p>
     * Manipulating the collection (e.g. by removing, adding elements) is safe and has no effect on the mock.
     * <p>
     * This method throws meaningful exception when object wrapped by MockingDetails is not a mock.
     *
     * @since 2.2.3
     */
    Collection<Stubbing> getStubbings();

    /**
     * Returns printing-friendly list of the invocations that occurred with the mock object.
     * Additionally, this method prints stubbing information, including unused stubbings.
     * For more information about unused stubbing detection see {@link MockitoHint}.
     * <p>
     * You can use this method for debugging,
     *  print the output of this method to the console to find out about all interactions with the mock.
     * <p>
     * Content that is printed is subject to change as we discover better ways of presenting important mock information.
     * Don't write code that depends on the output of this method.
     * If you need to know about interactions and stubbings, use {@link #getStubbings()} and {@link #getInvocations()}.
     * <p>
     * This method was moved from the deprecated and semi-hidden type {@link MockitoDebugger}.
     * <p>
     * This method throws meaningful exception when object wrapped by MockingDetails is not a mock.
     *
     * @since 2.2.6
     */
    String printInvocations();

    /**
     * Returns the {@link MockHandler} associated with this mock object.
     * The handler is the core of mock object method handling.
     * This method is useful for framework integrators.
     * For example, other frameworks may use mock handler to simulate method calls on the Mock object.
     *
     * @return mock handler instance of this mock
     * @since 2.10.0
     */
    @Incubating
    MockHandler getMockHandler();

    /**
     * Returns the mock object which is associated with this this instance of {@link MockingDetails}.
     * Basically, it's the object that you have passed to {@link Mockito#mockingDetails(Object)} method.
     *
     * @return the mock object of this mocking details instance
     *
     * @since 2.11.0
     */
    Object getMock();
}
