/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.creation.bytebuddy;

import java.io.ObjectStreamException;

public class SerializableStrongReference<T> implements SerializableReference<T> {
    private static final long serialVersionUID = 172345563923510472L;

    private final T ref;
    private final boolean deserializeAsWeakRef;

    public SerializableStrongReference(T ref, boolean deserializeAsWeakRef) {
        this.ref = ref;
        this.deserializeAsWeakRef = deserializeAsWeakRef;
    }

    @Override
    public T get() {
        return ref;
    }

    private Object readResolve() throws ObjectStreamException {
        if (deserializeAsWeakRef) {
            return new SerializeableWeakReference<T>(ref);
        } else {
            return this;
        }
    }
}
