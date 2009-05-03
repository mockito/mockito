package org.mockito.internal.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ShallowCopyTool {

    public <T> void copyToMock(T from, T mock) throws UnableToCopyFieldValue {
        Class<? extends Object> classFrom = from.getClass();
        Class<?> classTo = mock.getClass().getSuperclass();
        assert classTo == classFrom : "Classes must have the same type: class from: " + classFrom + ", class to: " + classTo;

        Field[] fieldsFrom = classFrom.getDeclaredFields();
        Field[] fieldsTo = classTo.getDeclaredFields();
        assert fieldsFrom.length == fieldsTo.length : "Objects should be of the same type";

        for (int i = 0; i < fieldsFrom.length; i++) {
            if (Modifier.isStatic(fieldsFrom[i].getModifiers())) {
                continue;
            }
            try {
                fieldsFrom[i].setAccessible(true);
                fieldsTo[i].setAccessible(true);
                Object value = fieldsFrom[i].get(from);
                fieldsTo[i].set(mock, value);
            } catch (Throwable t) {
                //TODO: add missing unit test
                throw new UnableToCopyFieldValue(
                        "Unable to copy value from field: " + fieldsFrom[i] + 
                        " to field: " + fieldsTo[i], t); 
            } 
        }
    }
}