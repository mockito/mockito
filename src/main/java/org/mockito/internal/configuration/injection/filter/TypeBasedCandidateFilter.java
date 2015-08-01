/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.configuration.injection.RealObject;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    MockCandidateFilter next;
    private final MockUtil mockUtil = new MockUtil();

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    public OngoingInjector filterCandidate(final Collection<Object> mocks,
                                           final Field candidateFieldToBeInjected,
                                           final List<Field> allRemainingCandidateFields,
                                           final Object injectee) {
        List<Object> mockTypeMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getType().isAssignableFrom(getInjectableClass(mock))) {
                mockTypeMatches.add(mock);
            }
        }

        return next.filterCandidate(mockTypeMatches, candidateFieldToBeInjected, allRemainingCandidateFields, injectee);
    }

    private Class<?> getInjectableClass(Object injectable) {
        if (injectable instanceof RealObject) {
            return ((RealObject) injectable).getValue().getClass();
        }

        return injectable.getClass();
    }
}
