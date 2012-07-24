package org.mockito.internal.util.reflection;


import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Incubating
public abstract class MockitoGenericMetadata {

    /**
     * Represents actual type variables resolved for current class.
     */
    protected Map<TypeVariable, Type> contextualActualTypeParameters = new HashMap<TypeVariable, Type>();

    private MockitoGenericMetadata() {
    }


    protected void registerTypeVariablesOn(Type classType) {
        if (!(classType instanceof ParameterizedType)) { // null protected
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) classType;
        TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            TypeVariable typeParameter = typeParameters[i];
            Type actualTypeArgument = actualTypeArguments[i];

            contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            GenericTypeInfo.logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "' }");
        }
    }

    protected void registerTypeVariablesOn(Class<?> clazz) {
        TypeVariable[] typeParameters = clazz.getTypeParameters();
        for (TypeVariable typeParameter : typeParameters) {
            contextualActualTypeParameters.put(typeParameter, boundsOf(typeParameter));
            GenericTypeInfo.logger.log("For '" + clazz.getCanonicalName() + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + boundsOf(typeParameter) + "' }");
        }
    }

    private Type boundsOf(TypeVariable typeParameter) {
        return new BoundedType(typeParameter);
    }

    /**
     * @return Raw type of the current instance.
     */
    public abstract Class<?> rawType();

    /**
     * @return Actual type arguments matching the type variables of the raw type represented by this {@link MockitoGenericMetadata} instance.
     */
    public Map<TypeVariable, Type> actualTypeArguments() {
        LinkedHashMap<TypeVariable, Type> actualTypeArguments = new LinkedHashMap<TypeVariable, Type>();

        TypeVariable[] typeParameters = rawType().getTypeParameters();
        for (TypeVariable typeParameter : typeParameters) {

            Type actualType = getActualTypeArgumentFor(typeParameter);

            actualTypeArguments.put(typeParameter, actualType);
            GenericTypeInfo.logger.log("For '" + rawType().getCanonicalName() + "' returning explicit TypeVariable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualType +"' }");
        }

        return actualTypeArguments;
    }

    protected Type getActualTypeArgumentFor(TypeVariable typeParameter) {
        Type type = this.contextualActualTypeParameters.get(typeParameter);
        if (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type;
            return getActualTypeArgumentFor(typeVariable);
        }

        return type;
    }

    public MockitoGenericMetadata resolveParameterizedType(Type genericReturnType) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Resolve current method generic return type to a {@link MockitoGenericMetadata}.
     *
     * @param method Method to resolve the return type.
     * @return {@link MockitoGenericMetadata} representing this generic return type.
     */
    public MockitoGenericMetadata resolveGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        System.out.println("Method '" + method.toGenericString() + "' has return type : " + genericReturnType.getClass().getInterfaces()[0].getSimpleName() + " : " + genericReturnType);

        if (genericReturnType instanceof Class) {
            return new NotGenericReturnType(genericReturnType);
        }

        if (genericReturnType instanceof ParameterizedType) {
            ParameterizedType returnType = (ParameterizedType) genericReturnType;
            return new ParameterizedReturnType(this, method);
        }

        return new MethodGenericReturnTypeMockitoGenericMetadata(this, method);
    }

    /**
     * Create an new MockitoGenericMetadata from a {@link Type}.
     *
     * @param type The class from which the {@link MockitoGenericMetadata} should be built.
     * @return The new {@link MockitoGenericMetadata}.
     */
    public static MockitoGenericMetadata from(Type type) {
        if (type instanceof Class) {
            return new FromClassMockitoGenericMetadata((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return new FromParameterizedTypeMockitoGenericMetadata((ParameterizedType) type);
        }
        throw new MockitoException("Type meta-data for this Type (" + type.getClass().getCanonicalName() + ") is not supported : " + type);
    }


    private static class FromClassMockitoGenericMetadata extends MockitoGenericMetadata {
        private Class<?> clazz;

        public FromClassMockitoGenericMetadata(Class<?> clazz) {
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

        @Override
        public Class<?> rawType() {
            return clazz;
        }
    }



    private static class FromParameterizedTypeMockitoGenericMetadata extends MockitoGenericMetadata {
        private ParameterizedType parameterizedType;

        public FromParameterizedTypeMockitoGenericMetadata(ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            readActualTypeParameters();
        }

        private void readActualTypeParameters() {
            registerTypeVariablesOn(parameterizedType.getRawType());
            registerTypeVariablesOn(parameterizedType);
        }

        @Override
        public Class<?> rawType() {
            // ParameterizedType#getRawType() always return a class !
            return (Class<?>) parameterizedType.getRawType();
        }
    }



    private static class ParameterizedReturnType extends MockitoGenericMetadata {


        private final ParameterizedType parameterizedType;
        private final TypeVariable<Method>[] typeParameters;

        public ParameterizedReturnType(MockitoGenericMetadata source, Method method) {
            parameterizedType = (ParameterizedType) method.getGenericReturnType();
            typeParameters = method.getTypeParameters();
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            readTypeVariables();
        }

        private void readTypeVariables() {
            registerTypeVariablesOn(parameterizedType);
        }

        @Override
        public Class<?> rawType() {
            return (Class<?>) parameterizedType.getRawType();
        }
    }



    private static class MethodGenericReturnTypeMockitoGenericMetadata extends MockitoGenericMetadata {
        private final TypeVariable<Method>[] typeParameters;

        private final Type genericReturnType;

        public MethodGenericReturnTypeMockitoGenericMetadata(MockitoGenericMetadata source, Method method) {
            genericReturnType = method.getGenericReturnType();
            typeParameters = method.getTypeParameters();
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            resolveActualMethodTypeParameter();
        }

        private void resolveActualMethodTypeParameter() {
            if (genericReturnType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) genericReturnType;
                for (Type type : typeVariable.getBounds()) {
                    registerTypeVariablesOn(type);
                }
                registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
            }
            if (genericReturnType instanceof ParameterizedType) {
                registerTypeVariablesOn(genericReturnType);
            }

            // throw new MockitoException("genericReturnType is a '" + genericReturnType.getClass() + "' : " + genericReturnType);
        }

        @Override
        public Class<?> rawType() {
            if (genericReturnType instanceof TypeVariable) {
                Type actualTypeArgument = getActualTypeArgumentFor((TypeVariable) genericReturnType);
                return extractRawTypeOf(actualTypeArgument);
            }
            return extractRawTypeOf(genericReturnType);
        }

        private Class<?> extractRawTypeOf(Type type) {
            if (type instanceof Class) {
                return (Class<?>) type;
            }
            if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            }
            if (type instanceof BoundedType) {
                return extractRawTypeOf(((BoundedType) type).firstBound());
            }
            throw new MockitoException("Raw extraction not managed for : '" + type + "'");
        }

    }
    private static class NotGenericReturnType extends MockitoGenericMetadata {
        private final Class<?> returnType;

        public NotGenericReturnType(Type genericReturnType) {
            returnType = (Class<?>) genericReturnType;
        }

        @Override
        public Class<?> rawType() {
            return returnType;
        }
    }
}
