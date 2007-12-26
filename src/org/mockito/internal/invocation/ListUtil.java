package org.mockito.internal.invocation;

import java.util.List;

public class ListUtil {

    public static <T> List<T> filter(List<T> list, Remove<T> remove) {
        return null;
    }
    
    public static interface Remove<T> {
        boolean remove(T object);
    }
}
