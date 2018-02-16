/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation.bytebuddy;

import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;

/**
 * A weak reference that is converted into a strong reference when serialized.
 */
public class SerializeableWeakReference<T> extends WeakReference<T> implements SerializableReference<T> {
    private static final long serialVersionUID = 275065433923510472L;

    public SerializeableWeakReference(T t) {
        super(t);
    }

    private Object writeReplace() throws ObjectStreamException {
        return new SerializableStrongReference<T>(get(), true);
    }

    @Override
    public T get() {
        T ref = super.get();

        if (ref == null) {
            throw new IllegalStateException("The mock was garbage collected. This should not " +
                "happen as long as the calling code keeps a reference to mock. Code using the " +
                "public mockito API should always do that. Internal users should request the " +
                "constructor of InterceptedInvocation to not use weak references.\n" +
                "If this exception is seen by code using the public mockito API, please file a " +
                "bug.");
        }

        return ref;
    }
}
