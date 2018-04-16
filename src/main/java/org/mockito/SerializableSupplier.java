package org.mockito;

import org.mockito.internal.util.Supplier;

import java.io.Serializable;

/**
 * An interface to make Suppliers serializable.
 * @param <T>
 */
public interface SerializableSupplier<T> extends Serializable, Supplier<T> {
}
