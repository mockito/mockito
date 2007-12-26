package org.mockito.internal.util;

import java.util.LinkedList;
import java.util.List;

public class ListUtil {

    public static <T> List<T> filter(List<T> list, Filter<T> filter) {
        List<T> filtered = new LinkedList<T>();
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
