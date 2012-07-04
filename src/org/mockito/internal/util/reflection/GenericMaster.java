/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.*;

@SuppressWarnings("unchecked")
public class GenericMaster {

    /**
     * Finds the generic type (parametrized type) of the field. If the field is not generic it returns Object.class. 
     * 
     * @param field
     */
    public Class getGenericType(Field field) {        
        Type generic = field.getGenericType();
        if (generic != null && generic instanceof ParameterizedType) {
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

    public Class<?> identifyGenericReturnType(Method method, Class<?> onClass) {
        Type genericReturnType = method.getGenericReturnType();

        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;

            if (parameterizedType.getActualTypeArguments().length > 0) {
                Type type = parameterizedType.getActualTypeArguments()[0];

                if (type instanceof Class) {
                    return (Class) type;
                }
                if (type instanceof TypeVariable) {
                    return identifyReturnTypeFromClass((TypeVariable) type, onClass);
                }
            }

        }

        return null;
    }

    private Class<?> identifyReturnTypeFromClass(TypeVariable typeVariable, Class<?> onClass) {
        Type[] genericInterfaces = onClass.getGenericInterfaces();

        for (Type genericInterface : genericInterfaces) {

            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                parameterizedType.getActualTypeArguments();

                if (parameterizedType.getRawType() instanceof Class) {
                    Class rawType = (Class) parameterizedType.getRawType();

                    TypeVariable[] typeParameters = rawType.getTypeParameters();

                    for (int typeVarPosition = 0; typeVarPosition < typeParameters.length; typeVarPosition++) {
                        TypeVariable typeParameter = typeParameters[typeVarPosition];
                        if (typeVariable.equals(typeParameter)) {
                            Type typeVariableValue = parameterizedType.getActualTypeArguments()[typeVarPosition];
                            if (typeVariableValue instanceof Class) {
                                return (Class) typeVariableValue;
                            }
                        }
                    }
                }

            }
        }

        return null;
    }
}
