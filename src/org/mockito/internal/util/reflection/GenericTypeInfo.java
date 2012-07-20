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
    private final Type clazz;

    /**
     * Represents resolved type variables on class.
     */
    private Map<TypeVariable, Type> typeVariables = new HashMap<TypeVariable, Type>();


    private GenericTypeInfo(Type typeToSolve, Class<?> sourceType, Map<TypeVariable, Type> typeVariables) {
        this.typeToSolve = typeToSolve;
        this.clazz = sourceType;
        this.typeVariables = typeVariables;
        throw new UnsupportedOperationException("code under (re)factoring");
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

        return !(clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) && typeToSolve.equals(that.typeToSolve);
    }

    @Override
    public int hashCode() {
        int result = typeToSolve.hashCode();
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
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
        private TypeVariable typeVariable;


        public TypeVariableUpperBounds(TypeVariable typeVariable) {
            this.typeVariable = typeVariable;
        }

        public Type firstBound() {
            return typeVariable.getBounds()[0]; // either a class or an interface, always present
        }

        public Type[] interfaceBounds() {
            // JLS says only interfaces from here (typeVar extends AClass_0 & I_1 & I_2 & etc)
            Type[] interfaceBounds = new Type[typeVariable.getBounds().length - 1];
            System.arraycopy(typeVariable.getBounds(), 1, interfaceBounds, 0, typeVariable.getBounds().length - 1);
            return interfaceBounds;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{firstBound=").append(firstBound());
            sb.append(", interfaceBounds=").append(Arrays.deepToString(interfaceBounds()));
            sb.append('}');
            return sb.toString();
        }
    }


    public static ParameterizedType asParameterizedType(Class<?> clazz) {
        return new ClassEnhancedToParameterizedType(clazz);
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
