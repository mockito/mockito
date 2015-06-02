/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.*;

import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

@SuppressWarnings("unchecked")
public class HandyReturnValues {

    public byte returnZero() {
        return 0;
    }

    public char returnChar() {
        return 0;
    }

    public <T> T returnNull() {
        return null;
    }

    public boolean returnFalse() {
        return false;
    }

    public String returnString() {
        return "";
    }

    public <T> T returnFor(Class<T> clazz) {
        // explicitly return null if type is not a primitive or a wrapper
        if (isPrimitiveOrWrapper(clazz)) {
            return defaultValueForPrimitiveOrWrapper(clazz);
        } 
        return null;
    }

    public Map returnMap() {
        return new HashMap();
    }

    public List returnList() {
        return new LinkedList();
    }

    public Set returnSet() {
        return new HashSet();
    }

    public <T> T returnFor(T instance) {
        return instance == null ? null : (T) returnFor(instance.getClass());
    }
}