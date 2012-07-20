package org.mockito.internal.util.reflection;

import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.internal.util.MockitoLogger;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Allow easy access to generic type information
 *
 * TODO refactor with polymorphism in mind?
 */
@Incubating
public class GenericTypeInfo {

    public static MockitoLogger logger = new ConsoleMockitoLogger();

    /**
     * This is the type that we wish to get more generic information.
     */
    private final Type typeToSolve;

    /**
     * The source class from which the generic info information should be retrieved.
     */
    private final Class<?> onClass;

    /**
     * Represents resolved type variables on class.
     */
    private Map<TypeVariable, Type> typeVariables = new HashMap<TypeVariable, Type>();


    private GenericTypeInfo(Type typeToSolve, Class<?> onClass, Map<TypeVariable, Type> typeVariables) {
        this.typeToSolve = typeToSolve;
        this.onClass = onClass;
        this.typeVariables = typeVariables;
        readActualTypeParametersOnDeclaringClass();
    }

    /**
     * Returns actual type as a raw type.
     * @return Raw type
     */
    public Class<?> asRawType() {
        return extractRawTypeOf(typeToSolve);
    }

    private Class<?> extractRawTypeOf(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        if (type instanceof TypeVariableUpperBounds) {
            return extractRawTypeOf(((TypeVariableUpperBounds) type).firstBound());
        }
        if (type instanceof TypeVariable) {
            /*
            If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
            on the class definition, such as such as List<E>.
            */
            return extractRawTypeOf(typeVariables.get(type));
        }
        throw new MockitoException("Raw extraction not managed for : '" + type + "'");
    }

    /**
     * Return the type of the type to solve.
     *
     * <p>Different than raw type extraction as it can extract actual type instead of TypeVariable</p>.
     *
     * TODO Eventually create our own ParameterizedType, with resolved TypeVariables
     *
     * @return The actual type
     */
    private Type actualType() {
        return extractActualTypeOf(typeToSolve);
    }

    private Type extractActualTypeOf(Type type) {
        if (type instanceof TypeVariableUpperBounds) {
            return extractActualTypeOf(((TypeVariableUpperBounds) type).firstBound());
        }
        if (type instanceof TypeVariable) {
            /*
            If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
            on the class definition, such as such as List<E>.
            */
            return extractActualTypeOf(typeVariables.get(type));
        }
        return type; // irrelevant, we don't manage other types.
    }

    private void readActualTypeParametersOnDeclaringClass() {
        registerTypeVariablesOn(onClass);
        registerTypeVariablesOn(onClass.getGenericSuperclass());
        for (Type genericInterface : onClass.getGenericInterfaces()) {
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

            typeVariables.put(typeParameter, actualTypeArgument);
            logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(in '" + typeParameter.getGenericDeclaration() + "')" + "' : '" + actualTypeArgument + "' }");
        }
    }

    private void registerTypeVariablesOn(Class<?> clazz) {
        TypeVariable[] typeParameters = clazz.getTypeParameters();
        for (TypeVariable typeParameter : typeParameters) {
            typeVariables.put(typeParameter, boundsOf(typeParameter));
            logger.log("For '" + clazz.getCanonicalName() + "' found type variable : { '" + typeParameter + "(in '" + typeParameter.getGenericDeclaration() + "')" + "' : '" + boundsOf(typeParameter) + "' }");
        }
    }

    private Type boundsOf(TypeVariable typeParameter) {
        return new TypeVariableUpperBounds(typeParameter.getBounds());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(actualType());
        sb.append(' ').append(typeVariables);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericTypeInfo that = (GenericTypeInfo) o;

        return !(onClass != null ? !onClass.equals(that.onClass) : that.onClass != null) && typeToSolve.equals(that.typeToSolve);
    }

    @Override
    public int hashCode() {
        int result = typeToSolve.hashCode();
        result = 31 * result + (onClass != null ? onClass.hashCode() : 0);
        return result;
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
    public static class TypeVariableUpperBounds implements Type {
        private final Type firstBound;
        private final Type[] interfaceBounds;


        public TypeVariableUpperBounds(Type[] bounds) {
            this.firstBound = bounds[0]; // either a class or an interface, always present
            this.interfaceBounds = new Type[bounds.length - 1]; // JLS says only interfaces from here
            System.arraycopy(bounds, 1, interfaceBounds, 0, bounds.length - 1);
        }

        public Type firstBound() {
            return firstBound;
        }

        public Type[] interfaceBounds() {
            return interfaceBounds;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{firstBound=").append(firstBound);
            sb.append(", interfaceBounds=").append(Arrays.deepToString(interfaceBounds));
            sb.append('}');
            return sb.toString();
        }
    }



    @Incubating
    public static Builder on(Class<?> clazz) {
        Checks.checkNotNull(clazz, "clazz");
        return new Builder().onClass(clazz);
    }

    @Incubating
    public static Builder on(GenericTypeInfo genericTypeInfo) {
        Checks.checkNotNull(genericTypeInfo, "genericTypeInfo");
        return new Builder().onClass(genericTypeInfo.asRawType())
                .withTypeVariables(genericTypeInfo.typeVariables);
    }




    @Incubating
    public static class Builder {

        private Class<?> clazz;
        private Method method;
        private Map<TypeVariable, Type> typeVariables = new HashMap<TypeVariable, Type>();


        public Builder onClass(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder method(Method method) {
            Checks.checkNotNull(method, "method");
            this.method = method;
            return this;
        }

        public Builder methodName(String methodName) {
            Checks.checkNotNull(methodName, "methodName");
            this.method = pickFirstMatchingMethod(methodName, clazz);
            return this;
        }

        private Method pickFirstMatchingMethod(String methodName, Class<?> clazz) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().contains(methodName)) {
                    return method;
                }
            }
            throw new IllegalStateException("The method : '" + methodName + "' do not exist in '" + clazz.getSimpleName() + "'");
        }

        private Builder withTypeVariables(Map<TypeVariable, Type> typeVariables) {
            this.typeVariables = typeVariables;
            return this;
        }

        public GenericTypeInfo genericReturnTypeInfo() {
            Type genericReturnType = method.getGenericReturnType();
            return new GenericTypeInfo(genericReturnType, clazz, typeVariables);
        }
    }


}
