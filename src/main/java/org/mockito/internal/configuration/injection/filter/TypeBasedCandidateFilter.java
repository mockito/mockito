/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.mockito.internal.util.MockUtil;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    protected boolean isCompatibleTypes(Type typeToMock, Type mockType) {
        if (typeToMock instanceof ParameterizedType && mockType instanceof ParameterizedType) {
            // ParameterizedType.equals() is documented as:
            // "Instances of classes that implement this interface must implement
            // an equals() method that equates any two instances that share the
            // same generic type declaration and have equal type parameters."
            // Unfortunately, e.g. Wildcard parameter "?" doesn't equal java.lang.String
            if (typeToMock.equals(mockType)) {
                return true;
            } else {
                ParameterizedType genericTypeToMock = (ParameterizedType) typeToMock;
                ParameterizedType genericMockType = (ParameterizedType) mockType;
                Type[] actualTypeArguments = genericTypeToMock.getActualTypeArguments();
                Type[] actualTypeArguments2 = genericMockType.getActualTypeArguments();
                // getRawType() says "the Type object representing the class or interface that
                // declares this type",
                // no clue why that's a Type rather than a Class as return type anyway
                Class rawType = (Class) genericTypeToMock.getRawType();
                Class rawType2 = (Class) genericMockType.getRawType();
                if (Objects.equals(genericTypeToMock.getOwnerType(), genericMockType.getOwnerType())
                        // e.g. Tree and TreeSet
                        && rawType.isAssignableFrom(rawType2)
                        && actualTypeArguments.length == actualTypeArguments2.length) {
                    // descend into recursion on type arguments
                    boolean isCompatible = true;
                    for (int i = 0; i < actualTypeArguments.length; i++) {
                        isCompatible =
                                isCompatible
                                        && isCompatibleTypes(
                                                actualTypeArguments[i], actualTypeArguments2[i]);
                    }
                    return isCompatible;
                } else {
                    // owner type, raw type or type arguments length don't match
                    return false;
                }
            }
        } else if (typeToMock instanceof WildcardType) {
            WildcardType wildcardTypeToMock = (WildcardType) typeToMock;
            Type[] upperBounds = wildcardTypeToMock.getUpperBounds();
            return Arrays.stream(upperBounds).anyMatch(t -> isCompatibleTypes(t, mockType));
        } else if (typeToMock instanceof Class && mockType instanceof Class) {
            return ((Class) typeToMock).isAssignableFrom((Class) mockType);
        } // no need to check for GenericArrayType, as Mockito cannot mock this anyway
        return false;
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee) {
        List<Object> mockTypeMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getType().isAssignableFrom(mock.getClass())) {
                Type genericMockType = MockUtil.getMockSettings(mock).getGenericTypeToMock();
                Type genericType = candidateFieldToBeInjected.getGenericType();
                boolean bothHaveGenericTypeInfo = genericType != null && genericMockType != null;
                // be more specific if generic type information is available
                if (!bothHaveGenericTypeInfo || isCompatibleTypes(genericType, genericMockType)) {
                    mockTypeMatches.add(mock);
                } // else filter out mock, as generic types don't match
            }
        }

        return next.filterCandidate(
                mockTypeMatches, candidateFieldToBeInjected, allRemainingCandidateFields, injectee);
    }
}
