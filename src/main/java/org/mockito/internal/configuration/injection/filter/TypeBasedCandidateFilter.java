/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockito.internal.util.MockUtil;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
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
                Type genericTypeToMock = MockUtil.getMockSettings(mock).getGenericTypeToMock();
                Type genericType = candidateFieldToBeInjected.getGenericType();
                // be more specific if generic type information is available
                if (genericTypeToMock != null || genericType != null) {
                    // would rather like to use Type.getTypeName(), but that doesn't exist in
                    // Android SDK 26 which Mockito aims to maintain compatibility with.
                    // Type.getTypeName() is documented to simply call Type.toString(), so use that
                    // instead
                    if (genericTypeToMock != null
                            && genericType != null
                            && genericTypeToMock.toString().equals(genericType.toString())) {
                        mockTypeMatches.add(mock);
                    } // else: filter out mock, as generic types don't match
                } else {
                    mockTypeMatches.add(mock);
                }
            }
        }

        return next.filterCandidate(
                mockTypeMatches, candidateFieldToBeInjected, allRemainingCandidateFields, injectee);
    }
}
