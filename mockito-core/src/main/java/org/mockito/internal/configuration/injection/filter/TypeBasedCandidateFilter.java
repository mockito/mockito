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
import java.util.List;
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
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getType().isAssignableFrom(mock.getClass())) {
                Type mockType = MockUtil.getMockSettings(mock).getGenericTypeToMock();
                Type typeToMock = candidateFieldToBeInjected.getGenericType();
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
}
