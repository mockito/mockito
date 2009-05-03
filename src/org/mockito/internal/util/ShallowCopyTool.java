package org.mockito.internal.util;

import java.lang.reflect.Field;

public class ShallowCopyTool {

    public <T> void copy(T from, T to) throws UnableToCopyFieldValue {
        Field[] fieldsFrom = from.getClass().getDeclaredFields();
        Field[] fieldsTo = to.getClass().getDeclaredFields();
        assert fieldsFrom.length == fieldsTo.length : "Objects should be of the same type";

        for (int i = 0; i < fieldsFrom.length; i++) {
            try {
                fieldsFrom[i].setAccessible(true);
                fieldsTo[i].setAccessible(true);
                Object value = fieldsFrom[i].get(from);
                fieldsTo[i].set(to, value);
            } catch (Throwable t) {
                throw new UnableToCopyFieldValue(
                        "Unable to copy value from field: " + fieldsFrom[i] + 
                        " to field: " + fieldsTo[i], t); 
            } 
        }
    }
}