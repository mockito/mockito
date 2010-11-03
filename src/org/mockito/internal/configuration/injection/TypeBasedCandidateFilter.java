package org.mockito.internal.configuration.injection;

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