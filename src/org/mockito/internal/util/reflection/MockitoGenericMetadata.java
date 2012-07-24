package org.mockito.internal.util.reflection;


import org.mockito.Incubating;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static org.mockito.Mockito.withSettings;

@Incubating
public abstract class MockitoGenericMetadata {

    public static MockitoLogger logger = new ConsoleMockitoLogger();

    /**
     * Represents actual type variables resolved for current class.
     */
    protected Map<TypeVariable, Type> contextualActualTypeParameters = new HashMap<TypeVariable, Type>();


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
            logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "(" + System.identityHashCode(typeParameter) + ")" + "' }");
        }
    }

    protected void registerTypeParametersOn(TypeVariable[] typeParameters) {
        for (TypeVariable typeParameter : typeParameters) {
            contextualActualTypeParameters.put(typeParameter, boundsOf(typeParameter));
            logger.log("For '" + typeParameter.getGenericDeclaration() + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + boundsOf(typeParameter) + "' }");
        }
    }

    private Type boundsOf(TypeVariable typeParameter) {
        if (typeParameter.getBounds()[0] instanceof TypeVariable) {
            return boundsOf((TypeVariable) typeParameter.getBounds()[0]);
        }
        return new BoundedType(typeParameter);
    }



    /**
     * @return Raw type of the current instance.
     */
    public abstract Class<?> rawType();



    /**
     * @return Returns extra interfaces if relevant, otherwise empty List.
     */
    public List<Type> extraInterfaces() {
        return Collections.emptyList();
    }



    /**
     * @return Actual type arguments matching the type variables of the raw type represented by this {@link MockitoGenericMetadata} instance.
     */
    public Map<TypeVariable, Type> actualTypeArguments() {
        TypeVariable[] typeParameters = rawType().getTypeParameters();
        LinkedHashMap<TypeVariable, Type> actualTypeArguments = new LinkedHashMap<TypeVariable, Type>();

        for (TypeVariable typeParameter : typeParameters) {

            Type actualType = getActualTypeArgumentFor(typeParameter);

            actualTypeArguments.put(typeParameter, actualType);
            logger.log("For '" + rawType().getCanonicalName() + "' returning explicit TypeVariable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualType +"' }");
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



    /**
     * Creates a mock using the Generics Metadata represented by this instance.
     *
     * @param answer The answer to use in mock settings.
     * @return The mock.
     */
    public Object toMock(Answer answer) {
        return Mockito.mock(
                rawType(),
                ((MockSettingsImpl) withSettings().defaultAnswer(answer)).parameterizedInfo(this)
        );
    }



    /**
     * Resolve current method generic return type to a {@link MockitoGenericMetadata}.
     *
     * @param method Method to resolve the return type.
     * @return {@link MockitoGenericMetadata} representing this generic return type.
     */
    public MockitoGenericMetadata resolveGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        logger.log("Method '" + method.toGenericString() + "' has return type : " + genericReturnType.getClass().getInterfaces()[0].getSimpleName() + " : " + genericReturnType);

        if (genericReturnType instanceof Class) {
            return new NotGenericReturnType(genericReturnType);
        }
        if (genericReturnType instanceof ParameterizedType) {
            return new ParameterizedReturnType(this, method.getTypeParameters(), (ParameterizedType) method.getGenericReturnType());
        }
        if (genericReturnType instanceof TypeVariable) {
            return new TypeVariableReturnType(this, method.getTypeParameters(), (TypeVariable) genericReturnType);
        }
        if (genericReturnType instanceof BoundedType) {
            return new TypeVariableReturnType(this, method.getTypeParameters(), ((BoundedType) genericReturnType).typeVariable());
        }

        throw new IllegalStateException("ouch");
    }

    /**
     * Create an new MockitoGenericMetadata from a {@link Type}.
     *
     * <p>
     *     Supports only {@link Class} and {@link ParameterizedType}, otherwise throw a {@link MockitoException}.
     * </p>
     *
     * @param type The class from which the {@link MockitoGenericMetadata} should be built.
     * @return The new {@link MockitoGenericMetadata}.
     * @throws MockitoException Raised if type is not a {@link Class} or a {@link ParameterizedType}.
     */
    public static MockitoGenericMetadata from(Type type) {
        Checks.checkNotNull(type, "type");
        if (type instanceof Class) {
            return new FromClassMockitoGenericMetadata((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return new FromParameterizedTypeMockitoGenericMetadata((ParameterizedType) type);
        }

        throw new MockitoException("Type meta-data for this Type (" + type.getClass().getCanonicalName() + ") is not supported : " + type);
    }


    /**
     * Metadata for source {@link Class}
     */
    private static class FromClassMockitoGenericMetadata extends MockitoGenericMetadata {
        private Class<?> clazz;

        public FromClassMockitoGenericMetadata(Class<?> clazz) {
            this.clazz = clazz;
            readActualTypeParametersOnDeclaringClass();
        }

        private void readActualTypeParametersOnDeclaringClass() {
            registerTypeParametersOn(clazz.getTypeParameters());
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


    /**
     * Metadata for source {@link ParameterizedType}.
     * Don't work with ParameterizedType returned in {@link Method#getGenericReturnType()}.
     */
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


    /**
     * Metadata specific to {@link ParameterizedType} generic return types.
     */
    private static class ParameterizedReturnType extends MockitoGenericMetadata {
        private final ParameterizedType parameterizedType;
        private final TypeVariable[] typeParameters;

        public ParameterizedReturnType(MockitoGenericMetadata source, TypeVariable[] typeParameters, ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            this.typeParameters = typeParameters;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(typeParameters);
        }

        private void readTypeVariables() {
            registerTypeVariablesOn(parameterizedType);
        }

        @Override
        public Class<?> rawType() {
            return (Class<?>) parameterizedType.getRawType();
        }

    }


    /**
     * Metadata specific to {@link TypeVariable} generic return type.
     */
    private static class TypeVariableReturnType extends MockitoGenericMetadata {
        private final TypeVariable typeVariable;
        private final TypeVariable[] typeParameters;
        private Class<?> rawType;



        public TypeVariableReturnType(MockitoGenericMetadata source, TypeVariable[] typeParameters, TypeVariable typeVariable) {
            this.typeParameters = typeParameters;
            this.typeVariable = typeVariable;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(typeParameters);
        }

        private void readTypeVariables() {
            for (Type type : typeVariable.getBounds()) {
                registerTypeVariablesOn(type);
            }
            registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
        }

        @Override
        public Class<?> rawType() {
            if (rawType == null) {
                rawType = extractRawTypeOf(typeVariable);
            }
            return rawType;
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
            if (type instanceof TypeVariable) {
                /*
                If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
                on the class definition, such as such as List<E>.
                */
                return extractRawTypeOf(contextualActualTypeParameters.get(type));
            }
            throw new MockitoException("Raw extraction not supported for : '" + type + "'");
        }

        @Override
        public List<Type> extraInterfaces() {
            Type type = extractActualBoundedTypeOf(typeVariable);
            if (type instanceof BoundedType) {
                return Arrays.asList(((BoundedType) type).interfaceBounds());
            }
            if (type instanceof ParameterizedType) {
                return Collections.singletonList(type);
            }
            if (type instanceof Class) {
                return Collections.emptyList();
            }
            throw new MockitoException("Cannot extract extra-interfaces from '" + typeVariable + "' : '" + type + "'");
        }

        private Class<?>[] rawExtraInterfaces() {
            List<Type> extraInterfaces = extraInterfaces();
            List<Class<?>> rawExtraInterfaces = new ArrayList<Class<?>>();
            for (Type extraInterface : extraInterfaces) {
                Class<?> rawInterface = extractRawTypeOf(extraInterface);
                // avoid interface collision with actual raw type (with typevariables, resolution ca be quite aggressive)
                if(!rawType().equals(rawInterface)) {
                    rawExtraInterfaces.add(rawInterface);
                }
            }
            return rawExtraInterfaces.toArray(new Class[rawExtraInterfaces.size()]);
        }

        private Type extractActualBoundedTypeOf(Type type) {
            if (type instanceof TypeVariable) {
                /*
                If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
                on the class definition, such as such as List<E>.
                */
                return extractActualBoundedTypeOf(contextualActualTypeParameters.get(type));
            }
            if (type instanceof BoundedType) {
                Type actualFirstBound = extractActualBoundedTypeOf(((BoundedType) type).firstBound());
                if (!(actualFirstBound instanceof BoundedType)) {
                    return type; // avoid going one step further, ie avoid : O(TypeVar) -> K(TypeVar) -> Some ParamType
                }
                return actualFirstBound;
            }
            return type; // irrelevant, we don't manage other types.
        }

        public Object toMock(Answer answer) {
            Class<?>[] rawExtraInterfaces = rawExtraInterfaces();
            if (rawExtraInterfaces.length <= 0) {
                return super.toMock(answer);
            }

            return Mockito.mock(
                    rawType(),
                    ((MockSettingsImpl) withSettings()
                            .defaultAnswer(answer)
                            .extraInterfaces(rawExtraInterfaces))
                            .parameterizedInfo(this)
            );
        }
    }



    /**
     * Metadata specific to {@link Class} return type.
     */
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

    /**
     * Type representing bounds of a type variable, allows to keep all bounds information.
     *
     * <p>It uses the first bound in the array, as this array is never null and always contains at least
     * one element (Object is always here if no bounds are declared).</p>
     *
     * <p>If upper bounds are declared with SomeClass and additional interfaces, then firstBound will be SomeClass and
     * interfacesBound will be an array of the additional interfaces.
     *
     * i.e. <code>SomeClass</code>.
     * <pre class="code"><code class="java">
     *     interface UpperBoundedTypeWithClass<E extends Comparable<E> & Cloneable> {
     *         E get();
     *     }
     *     // will return Comparable type
     * </code></pre>
     * </p>
     */
    public static class BoundedType implements Type {
        private TypeVariable typeVariable;


        public BoundedType(TypeVariable typeVariable) {
            this.typeVariable = typeVariable;
        }

        /**
         * @return either a class or an interface (parameterized or not), if no bounds declared Object is returned.
         */
        public Type firstBound() {
            return typeVariable.getBounds()[0]; //
        }

        /**
         * On a Type Variable (typeVar extends AClass_0 & I_1 & I_2 & etc), will return an array
         * containing I_1 and I_2.
         *
         * @return other bounds for this type, these bounds can only be only interfaces as the JLS says,
         * empty array if no other bound declared.
         */
        public Type[] interfaceBounds() {
            Type[] interfaceBounds = new Type[typeVariable.getBounds().length - 1];
            System.arraycopy(typeVariable.getBounds(), 1, interfaceBounds, 0, typeVariable.getBounds().length - 1);
            return interfaceBounds;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return typeVariable.equals(((BoundedType) o).typeVariable);

        }

        @Override
        public int hashCode() {
            return typeVariable.hashCode();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{firstBound=").append(firstBound());
            sb.append(", interfaceBounds=").append(Arrays.deepToString(interfaceBounds()));
            sb.append('}');
            return sb.toString();
        }

        public TypeVariable typeVariable() {
            return typeVariable;
        }
    }
}


