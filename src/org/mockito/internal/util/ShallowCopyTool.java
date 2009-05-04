package org.mockito.internal.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class ShallowCopyTool {

    public <T> void copyToMock(T from, T mock) throws UnableToCopyFieldValue {
        Class clazz = from.getClass();
        Class mockSuperClass = mock.getClass().getSuperclass();
        assert mockSuperClass == clazz : "Classes must have the same type: class of the object from: " + clazz + ", mock super class: " + mockSuperClass;

        while (clazz != Object.class) {
            copyValues(from, mock, clazz);
            clazz = clazz.getSuperclass();
        }
    }

    private <T> void copyValues(T from, T mock, Class classFrom)
            throws UnableToCopyFieldValue {
        Field[] fields = classFrom.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            try {
                //TODO: undo
                fields[i].setAccessible(true);
                Object value = fields[i].get(from);
                fields[i].set(mock, value);
            } catch (Throwable t) {
                //TODO: add missing unit test
                throw new UnableToCopyFieldValue("Unable to copy value to field: " + fields[i], t); 
            } 
        }
    }
}