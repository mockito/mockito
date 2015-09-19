/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

/**
 * Allow the ongoing injection of a mock candidate.
 */
public interface OngoingInjector {

    /**
     * Inject the mock.
     *
     * <p>
     * Please check the actual implementation.
     * </p>
     *
     * @return the mock that was injected, <code>null</code> otherwise.
     */
    Object thenInject();

    /**
     * Injector that will do nothing, and will return <code>null</code> as no mocks will be injected
     */
    OngoingInjector nop = new OngoingInjector() {
        public Object thenInject() {
            return null;
        }
    };
}
