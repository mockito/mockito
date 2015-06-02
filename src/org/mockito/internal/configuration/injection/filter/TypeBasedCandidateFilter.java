/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilter implements MockCandidateFilter {

    MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        List<Object> mockTypeMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }

        return next.filterCandidate(mockTypeMatches, field, fieldInstance);
    }
}
