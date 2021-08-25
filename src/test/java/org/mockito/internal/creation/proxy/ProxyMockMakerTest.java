/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.proxy;

import org.junit.Test;
import org.mockito.internal.creation.AbstractMockMakerTest;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyMockMakerTest
        extends AbstractMockMakerTest<ProxyMockMaker, ProxyMockMakerTest.SomeInterface> {

    public ProxyMockMakerTest() {
        super(new ProxyMockMaker(), SomeInterface.class);
    }

    @Test
    public void should_create_mock_from_interface() {
        SomeInterface proxy =
                mockMaker.createMock(settingsFor(SomeInterface.class), dummyHandler());

        Class<?> superClass = proxy.getClass().getSuperclass();
        assertThat(superClass).isEqualTo(Proxy.class);
    }

    @Test
    public void should_create_mock_from_interface_with_extra_interface() {
        SomeInterface proxy =
                mockMaker.createMock(
                        settingsFor(SomeInterface.class, Serializable.class), dummyHandler());

        Class<?> superClass = proxy.getClass().getSuperclass();
        assertThat(superClass).isEqualTo(Proxy.class);
        assertThat(proxy).isInstanceOf(Serializable.class);
    }

    @Test
    public void should_discover_mockable_input() {
        assertThat(mockMaker.isTypeMockable(Object.class).mockable()).isFalse();
        assertThat(mockMaker.isTypeMockable(Object.class).nonMockableReason())
                .isEqualTo("non-interface");
        assertThat(mockMaker.isTypeMockable(SomeInterface.class).mockable()).isTrue();
    }

    @Test
    public void can_compute_hash_code() throws Throwable {
        SomeInterface proxy =
                mockMaker.createMock(settingsFor(SomeInterface.class), dummyHandler());

        InvocationHandler handler = Proxy.getInvocationHandler(proxy);

        assertThat(handler.invoke(proxy, Object.class.getMethod("hashCode"), null))
                .isEqualTo(System.identityHashCode(proxy));
    }

    @Test
    public void can_compute_equality() throws Throwable {
        SomeInterface proxy =
                mockMaker.createMock(settingsFor(SomeInterface.class), dummyHandler());

        InvocationHandler handler = Proxy.getInvocationHandler(proxy);

        assertThat(
                        handler.invoke(
                                proxy,
                                Object.class.getMethod("equals", Object.class),
                                new Object[] {proxy}))
                .isEqualTo(true);
        assertThat(
                        handler.invoke(
                                proxy,
                                Object.class.getMethod("equals", Object.class),
                                new Object[] {null}))
                .isEqualTo(false);
        assertThat(
                        handler.invoke(
                                proxy,
                                Object.class.getMethod("equals", Object.class),
                                new Object[] {new Object()}))
                .isEqualTo(false);
    }

    @Test
    public void can_invoke_toString() throws Throwable {
        SomeInterface proxy =
                mockMaker.createMock(settingsFor(SomeInterface.class), dummyHandler());

        InvocationHandler handler = Proxy.getInvocationHandler(proxy);

        assertThat(handler.invoke(proxy, Object.class.getMethod("toString"), null)).isNull();
    }

    interface SomeInterface {}
}
