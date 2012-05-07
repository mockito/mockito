/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilter implements MockCandidateFilter {
    private final MockCandidateFilter next;
    private final MockUtil mockUtil = new MockUtil();

    public NameBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        List<Object> mockNameMatches = new ArrayList<Object>();
        if(mocks.size() > 1) {
            for (Object mock : mocks) {
                if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
                    mockNameMatches.add(mock);
                }
            }
            return next.filterCandidate(mockNameMatches, field, fieldInstance);
        }
        return next.filterCandidate(mocks, field, fieldInstance);
    }
}
