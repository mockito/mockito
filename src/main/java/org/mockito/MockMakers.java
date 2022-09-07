/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.plugins.MockMaker;

/**
 * Constants for built-in implementations of {@code MockMaker}.
 * You may use the constants of this class for {@link MockSettings#mockMaker(String)} or {@link Mock#mockMaker()}.
 * The string values of these constants may also be used in the resource file <code>mockito-extensions/org.mockito.plugins.MockMaker</code>
 * as described in the class documentation of {@link MockMaker}.
 *
 * @since 4.8.0
 */
public final class MockMakers {
    /**
     * Inline mock maker which can mock final types, enums and final methods.
     * This mock maker cannot mock native methods,
     * and it does not support {@link MockSettings#extraInterfaces(Class[]) extra interfaces}.
     *
     * @see <a href="Mockito.html#39">Mocking final types, enums and final methods</a>
     */
    public static final String INLINE = "mock-maker-inline";
    /**
     * Proxy mock maker which avoids code generation, but can only mock interfaces.
     *
     * @see <a href="Mockito.html#50">Avoiding code generation when restricting mocks to interfaces</a>
     */
    public static final String PROXY = "mock-maker-proxy";
    /**
     * Subclass mock maker which mocks types by creating subclasses.
     * This is the first built-in mock maker which has been provided by Mockito.
     * Since this mock maker relies on subclasses, it cannot mock final classes and methods.
     */
    public static final String SUBCLASS = "mock-maker-subclass";

    private MockMakers() {}
}
