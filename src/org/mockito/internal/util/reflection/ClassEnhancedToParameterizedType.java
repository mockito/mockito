package org.mockito.internal.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
* Represents a class augmented to a Parameterized Type
*/
class ClassEnhancedToParameterizedType implements ParameterizedType {
    private Class<?> clazz;

    /**
     * Represents actual type variables resolved for current class.
     */
    private Map<TypeVariable, Type> allActualTypeParameters = new HashMap<TypeVariable, Type>();

    ClassEnhancedToParameterizedType(Class<?> clazz) {
        this.clazz = clazz;
        readActualTypeParametersOnDeclaringClass();
    }

    private void readActualTypeParametersOnDeclaringClass() {
        registerTypeVariablesOn(clazz);
        registerTypeVariablesOn(clazz.getGenericSuperclass());
        for (Type genericInterface : clazz.getGenericInterfaces()) {
            registerTypeVariablesOn(genericInterface);
        }
    }

    private void registerTypeVariablesOn(Type classType) {
        if (!(classType instanceof ParameterizedType)) {
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) classType;
        TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            TypeVariable typeParameter = typeParameters[i];

            allActualTypeParameters.put(typeParameter, actualTypeArgument);
            GenericTypeInfo.logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(in '" + typeParameter.getGenericDeclaration() + "')" + "' : '" + actualTypeArgument + "' }");
        }
    }

    private void registerTypeVariablesOn(Class<?> clazz) {
        TypeVariable[] typeParameters = clazz.getTypeParameters();
        for (TypeVariable typeParameter : typeParameters) {
            allActualTypeParameters.put(typeParameter, boundsOf(typeParameter));
            GenericTypeInfo.logger.log("For '" + clazz.getCanonicalName() + "' found type variable : { '" + typeParameter + "(in '" + typeParameter.getGenericDeclaration() + "')" + "' : '" + boundsOf(typeParameter) + "' }");
        }
    }

    private Type boundsOf(TypeVariable typeParameter) {
        return new GenericTypeInfo.TypeVariableUpperBounds(typeParameter);
    }



    public Type[] getActualTypeArguments() {


        return new Type[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Type getRawType() {
        return clazz;
    }

    public Type getOwnerType() {
        // TODO
        return null;
    }
}
