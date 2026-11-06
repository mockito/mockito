/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.exceptions.Reporter.moreThanOneMockCandidate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.mockito.internal.util.MockUtil;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    protected boolean isCompatibleTypes(Type typeToMock, Type mockType, Field injectMocksField) {
        boolean result = false;
        if (typeToMock instanceof ParameterizedType) {
            if (mockType instanceof ParameterizedType) {
                // ParameterizedType.equals() is documented as:
                // "Instances of classes that implement this interface must implement
                // an equals() method that equates any two instances that share the
                // same generic type declaration and have equal type parameters."
                // Unfortunately, e.g. Wildcard parameter "?" doesn't equal java.lang.String,
                // and e.g. Set doesn't equal TreeSet, so roll our own comparison if
                // ParameterizedTypeImpl.equals() returns false
                if (typeToMock.equals(mockType)) {
                    result = true;
                } else {
                    ParameterizedType genericTypeToMock = (ParameterizedType) typeToMock;
                    ParameterizedType genericMockType = (ParameterizedType) mockType;
                    Type[] actualTypeArguments = genericTypeToMock.getActualTypeArguments();
                    Type[] actualTypeArguments2 = genericMockType.getActualTypeArguments();
                    if (actualTypeArguments.length == actualTypeArguments2.length) {
                        // Recurse on type parameters, so we properly test whether e.g. Wildcard
                        // bounds have a match
                        result =
                                recurseOnTypeArguments(
                                        injectMocksField,
                                        actualTypeArguments,
                                        actualTypeArguments2);
                    } else {
                        // the two ParameterizedTypes cannot match because they have unequal
                        // number of type arguments
                        result = false;
                    }
                }
            } else {
                // mockType is a non-parameterized Class, i.e. a concrete class.
                // so walk concrete class' type hierarchy
                Class<?> concreteMockClass = (Class<?>) mockType;
                Stream<Type> mockSuperTypes = getSuperTypes(concreteMockClass);
                result =
                        mockSuperTypes.anyMatch(
                                mockSuperType ->
                                        isCompatibleTypes(
                                                typeToMock, mockSuperType, injectMocksField));
            }
        } else if (typeToMock instanceof WildcardType) {
            WildcardType wildcardTypeToMock = (WildcardType) typeToMock;
            Type[] upperBounds = wildcardTypeToMock.getUpperBounds();
            result =
                    Arrays.stream(upperBounds)
                            .anyMatch(t -> isCompatibleTypes(t, mockType, injectMocksField));
        } else if (typeToMock instanceof Class && mockType instanceof Class) {
            result = ((Class<?>) typeToMock).isAssignableFrom((Class<?>) mockType);
        } // no need to check for GenericArrayType, as Mockito cannot mock this anyway

        return result;
    }

    private Stream<Type> getSuperTypes(Class<?> concreteMockClass) {
        Stream<Type> mockInterfaces = Arrays.stream(concreteMockClass.getGenericInterfaces());
        Type genericSuperclass = concreteMockClass.getGenericSuperclass();
        // for java.lang.Object, genericSuperclass is null
        if (genericSuperclass != null) {
            Stream<Type> mockSuperTypes =
                    Stream.concat(mockInterfaces, Stream.of(genericSuperclass));
            return mockSuperTypes;
        } else {
            return mockInterfaces;
        }
    }

    private boolean recurseOnTypeArguments(
            Field injectMocksField, Type[] actualTypeArguments, Type[] actualTypeArguments2) {
        boolean isCompatible = true;
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            Type actualTypeArgument2 = actualTypeArguments2[i];
            if (actualTypeArgument instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                // this is a TypeVariable declared by the class under test that turned
                // up in one of its fields,
                // e.g. class ClassUnderTest<T1, T2> { List<T1> tList; Set<T2> tSet}
                // The TypeVariable`s actual type is declared by the field containing
                // the object under test, i.e. the field annotated with @InjectMocks
                // e.g. @InjectMocks ClassUnderTest<String, Integer> underTest = ..

                Type genericType = injectMocksField.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    Type[] injectMocksFieldTypeParameters =
                            ((ParameterizedType) genericType).getActualTypeArguments();
                    // Find index of given TypeVariable where it was defined, e.g. 0 for T1 in
                    // ClassUnderTest<T1, T2>
                    // (we're always able to find it, otherwise test class wouldn't have compiled))
                    TypeVariable<?>[] genericTypeParameters =
                            injectMocksField.getType().getTypeParameters();
                    int variableIndex = -1;
                    for (int i2 = 0; i2 < genericTypeParameters.length; i2++) {
                        if (genericTypeParameters[i2].equals(typeVariable)) {
                            variableIndex = i2;
                            break;
                        }
                    }
                    // now test whether actual type for the type variable is compatible, e.g. for
                    //   class ClassUnderTest<T1, T2> {..}
                    // T1 would be the String in
                    //   ClassUnderTest<String, Integer> underTest = ..
                    isCompatible &=
                            isCompatibleTypes(
                                    injectMocksFieldTypeParameters[variableIndex],
                                    actualTypeArgument2,
                                    injectMocksField);
                } else {
                    // must be a concrete class, recurse on super types that may have type
                    // parameters
                    isCompatible &=
                            getSuperTypes((Class<?>) genericType)
                                    .anyMatch(
                                            superType ->
                                                    isCompatibleTypes(
                                                            superType,
                                                            actualTypeArgument2,
                                                            injectMocksField));
                }
            } else {
                isCompatible &=
                        isCompatibleTypes(
                                actualTypeArgument, actualTypeArgument2, injectMocksField);
            }
        }
        return isCompatible;
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee,
            final Field injectMocksField) {
        List<Object> mockTypeMatches = new ArrayList<>();
        // resolve type variables of superclass fields, e.g. for
        //   class GenericType<T> { T field; }
        //   class TypeImpl extends GenericType<InputStream> {}
        // the field's type variable T is resolved to InputStream, based on the actual type
        // arguments the class of the object under test supplies to its generic superclass
        Type candidateFieldType = resolveCandidateFieldType(candidateFieldToBeInjected, injectee);
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getType().isAssignableFrom(mock.getClass())) {
                Type mockType = MockUtil.getMockSettings(mock).getGenericTypeToMock();
                Type typeToMock = candidateFieldType;
                boolean bothHaveTypeInfo = typeToMock != null && mockType != null;
                if (bothHaveTypeInfo) {
                    // be more specific if generic type information is available
                    if (isCompatibleTypes(typeToMock, mockType, injectMocksField)) {
                        mockTypeMatches.add(mock);
                    } // else filter out mock, as generic types don't match
                } else {
                    // field is assignable from mock class, but no generic type information
                    // is available (can happen with programmatically created Mocks where no
                    // genericTypeToMock was supplied)
                    mockTypeMatches.add(mock);
                }
            } // else filter out mock
            // BTW mocks may contain Spy objects with their original class (seemingly before
            // being wrapped), and MockUtil.getMockSettings() throws exception for those
        }

        boolean wasMultipleMatches = mockTypeMatches.size() > 1;

        OngoingInjector result =
                next.filterCandidate(
                        mockTypeMatches,
                        candidateFieldToBeInjected,
                        allRemainingCandidateFields,
                        injectee,
                        injectMocksField);

        if (wasMultipleMatches) {
            // we had found multiple mocks matching by type, see whether following filters
            // were able to reduce this to single match (e.g. by filtering for matching field names)
            if (result == OngoingInjector.nop) {
                // nope, following filters cannot reduce this to a single match
                throw moreThanOneMockCandidate(candidateFieldToBeInjected, mocks);
            }
        }
        return result;
    }

    /**
     * Resolves the type variables contained in the candidate field's generic type against the
     * actual type arguments that the class of the object under test (the "injectee") supplies to
     * its generic superclasses. Fields declared by a generic superclass, e.g.
     *
     * <pre>
     * class GenericType&lt;T&gt; { T field; List&lt;T&gt; listField; }
     * class TypeImpl extends GenericType&lt;InputStream&gt; {}
     * </pre>
     *
     * have their type variable T replaced by InputStream, so that the field is recognized as
     * being of type InputStream (resp. {@code List<InputStream>}). Type variables that cannot be
     * resolved this way (e.g. type variables declared by the class under test itself, or mappings
     * that only exist if the {@code @InjectMocks} field declares a parameterized type) are left
     * untouched, they keep being handled by {@link #recurseOnTypeArguments}.
     */
    private Type resolveCandidateFieldType(Field candidateFieldToBeInjected, Object injectee) {
        Type candidateFieldType = candidateFieldToBeInjected.getGenericType();
        if (injectee == null) {
            return candidateFieldType;
        }
        Map<TypeVariable<?>, Type> actualTypeArguments =
                collectActualTypeArgumentsOfSuperclasses(injectee.getClass());
        if (actualTypeArguments.isEmpty()) {
            return candidateFieldType;
        }
        return resolveTypeVariables(candidateFieldType, actualTypeArguments, new HashSet<>());
    }

    /**
     * Maps the type variables declared by every generic superclass in the given class' hierarchy
     * to the actual type argument supplied for it, e.g. for
     * {@code class TypeImpl extends GenericType<InputStream>} this maps GenericType's type
     * variable T to InputStream.
     */
    private Map<TypeVariable<?>, Type> collectActualTypeArgumentsOfSuperclasses(Class<?> clazz) {
        Map<TypeVariable<?>, Type> actualTypeArguments = new HashMap<>();
        for (Class<?> current = clazz;
                current != null && current != Object.class;
                current = current.getSuperclass()) {
            Type genericSuperclass = current.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedSuperclass = (ParameterizedType) genericSuperclass;
                TypeVariable<?>[] typeVariables =
                        ((Class<?>) parameterizedSuperclass.getRawType()).getTypeParameters();
                Type[] actualArguments = parameterizedSuperclass.getActualTypeArguments();
                for (int i = 0; i < typeVariables.length && i < actualArguments.length; i++) {
                    actualTypeArguments.putIfAbsent(typeVariables[i], actualArguments[i]);
                }
            }
        }
        return actualTypeArguments;
    }

    /**
     * Replaces type variables by their actual type argument, recursing so that a type variable
     * mapped to another type variable is resolved transitively (e.g.
     * {@code class Middle<U> extends GenericType<U>} with
     * {@code class Leaf extends Middle<InputStream>} maps GenericType's T to InputStream via
     * Middle's U). Unresolvable type variables are left as they are. The {@code resolutionPath}
     * guards against cyclic mappings like {@code class Leaf<T> extends GenericType<T>}, which are
     * left unresolved as well.
     */
    private Type resolveTypeVariables(
            Type type,
            Map<TypeVariable<?>, Type> actualTypeArguments,
            Set<TypeVariable<?>> resolutionPath) {
        if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            Type actualType = actualTypeArguments.get(typeVariable);
            if (actualType == null || !resolutionPath.add(typeVariable)) {
                // unresolvable or cyclic type variable, leave it as is
                return type;
            }
            try {
                return resolveTypeVariables(actualType, actualTypeArguments, resolutionPath);
            } finally {
                resolutionPath.remove(typeVariable);
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualArguments = parameterizedType.getActualTypeArguments();
            Type[] resolvedArguments = new Type[actualArguments.length];
            boolean changed = false;
            for (int i = 0; i < actualArguments.length; i++) {
                resolvedArguments[i] =
                        resolveTypeVariables(
                                actualArguments[i], actualTypeArguments, resolutionPath);
                changed |= resolvedArguments[i] != actualArguments[i];
            }
            return changed
                    ? new ResolvedParameterizedType(parameterizedType, resolvedArguments)
                    : type;
        }
        return type;
    }

    /**
     * A parameterized type whose type arguments have been resolved, e.g. {@code List<T>} with
     * T resolved to InputStream becomes {@code List<InputStream>}. Implements the
     * {@link ParameterizedType#equals()} contract so that it compares equal to other
     * ParameterizedTypes sharing the same generic type declaration and type arguments.
     */
    private static final class ResolvedParameterizedType implements ParameterizedType {

        private final Type ownerType;
        private final Class<?> rawType;
        private final Type[] actualTypeArguments;

        private ResolvedParameterizedType(ParameterizedType source, Type[] resolvedArguments) {
            this.ownerType = source.getOwnerType();
            this.rawType = (Class<?>) source.getRawType();
            this.actualTypeArguments = resolvedArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType that = (ParameterizedType) other;
            return Objects.equals(ownerType, that.getOwnerType())
                    && rawType.equals(that.getRawType())
                    && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
        }

        @Override
        public int hashCode() {
            return Objects.hash(ownerType, rawType, Arrays.hashCode(actualTypeArguments));
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (ownerType != null) {
                result.append(ownerType.getTypeName()).append('.');
            }
            result.append(rawType.getName());
            if (actualTypeArguments.length > 0) {
                result.append('<');
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    if (i > 0) {
                        result.append(", ");
                    }
                    result.append(actualTypeArguments[i].getTypeName());
                }
                result.append('>');
            }
            return result.toString();
        }
    }
}
