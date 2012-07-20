/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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










    /////////////////////////////
    /////////////////////////////
    ////////// O  L  D //////////
    /////////////////////////////
    /////////////////////////////




    /**
     * Identify the returned generic type value of the given method from the given class.
     *
     * @param method Method whose generic part of the returned type must be identified.
     * @param onClass Owner class from which the resolution of generic type value must be identified.
     * @return Generic type value if found, <code>null</code> otherwise.
     */
    public Class<?> identifyGenericReturnType(Method method, Class<?> onClass) {
        Type genericReturnType = method.getGenericReturnType();

        // if method has return type like List<Number>
        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;

            // number of type argument of List for example
            if (parameterizedType.getActualTypeArguments().length <= 0) {
                return null; // not supported yet, see #getActualTypeArguments javadoc
            }
            Type type = parameterizedType.getActualTypeArguments()[0];

            // if type argument is a simple class, for example Number in List<Number>
            if (type instanceof Class) {
                return (Class) type;
            }
            if (type instanceof TypeVariable) {
                throw new IllegalStateException();
//                return identifyReturnTypeFromClass((TypeVariable) type, onClass);
            }
            if (type instanceof ParameterizedType) {
                throw new IllegalStateException();
//                ParameterizedType subParameterizedType = (ParameterizedType) type;
//                return (Class<?>) subParameterizedType.getRawType();
            }
            return null;
        }

//        if (genericReturnType instanceof TypeVariable) {
//            TypeVariable typeVariable = (TypeVariable) genericReturnType;
//            return identifyReturnTypeFromClass(typeVariable, onClass);
//        }

        return null;
    }

    private Class<?> identifyReturnTypeFromClass(TypeVariable typeVariable, Class<?> onClass) {
        List<Type> genericInterfaces = new ArrayList<Type>(Arrays.asList(onClass.getGenericInterfaces()));
        genericInterfaces.add(onClass.getGenericSuperclass());

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

                            if (typeVariableValue instanceof TypeVariable) {
                                TypeVariable variableValue = (TypeVariable) typeVariableValue;

                                throw new IllegalStateException("type var :" + variableValue);
                            }

//                            if (typeVariableValue instanceof ParameterizedType) {
//                                return (Class<?>) ((ParameterizedType) typeVariableValue).getRawType();
//                            }
                        }
                    }
                }

            }
        }

        return null;
    }
}
