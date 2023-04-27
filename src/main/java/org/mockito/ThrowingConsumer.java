/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.function.Consumer;

@SuppressWarnings("FunctionalInterfaceMethodChanged")
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    @Override
    default void accept(final T input) {
        try {
            acceptThrows(input);
        } catch (final RuntimeException | AssertionError e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T input) throws Throwable;
}
