/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.LinkedList;
import java.util.List;

public class ListUtil {

    public static <T> LinkedList<T> filter(List<T> list, Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<T>();
        for (T t : list) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    public static interface Filter<T> {
        boolean isOut(T object);
    }
}
