/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.mockito.internal.util.Primitives.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        if (isPrimitiveWrapper(clazz)) {
            return primitiveWrapperOf(clazz);
        } 
        return primitiveValueOrNullFor(clazz);
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