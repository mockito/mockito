/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMaster {

    /**
     * Finds the generic type (parametrized type) of the field. If the field is not generic it returns Object.class. 
     * 
     * @param field the field to inspect
     */
    public Class getGenericType(Field field) {        
        Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            if (actual instanceof Class) {
                return (Class) actual;
            } else if (actual instanceof ParameterizedType) {
                //in case of nested generics we don't go deep
                return (Class) ((ParameterizedType) actual).getRawType();
            }
        }
        
        return Object.class;
    }

}
