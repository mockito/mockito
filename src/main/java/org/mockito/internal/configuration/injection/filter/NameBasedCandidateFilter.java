/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.util.MockUtil.getMockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilter implements MockCandidateFilter {
    private final MockCandidateFilter next;

    public NameBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    @Override
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee,
            final Field injectMocksField) {
        if (mocks.size() == 1
                && anotherCandidateMatchesMockName(
                        mocks, candidateFieldToBeInjected, allRemainingCandidateFields)) {
            return OngoingInjector.nop;
        }

        return next.filterCandidate(
                tooMany(mocks) ? selectMatchingName(mocks, candidateFieldToBeInjected) : mocks,
                candidateFieldToBeInjected,
                allRemainingCandidateFields,
                injectee,
                injectMocksField);
    }

    private boolean tooMany(Collection<Object> mocks) {
        return mocks.size() > 1;
    }

    private List<Object> selectMatchingName(
            Collection<Object> mocks, Field candidateFieldToBeInjected) {
        List<Object> mockNameMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getName().equals(getMockName(mock).toString())) {
                mockNameMatches.add(mock);
            }
        }
        return mockNameMatches;
    }

    /*
     * In this case we have to check whether we have conflicting naming
     * fields. E.g. 2 fields of the same type, but we have to make sure
     * we match on the correct name.
     *
     * Therefore we have to go through all other fields and make sure
     * whenever we find a field that does match its name with the mock
     * name, we should take that field instead.
     */
    private boolean isSameTypeAndName(
        Field fieldA, Field fieldB, String mockName) {
        return fieldA.getType().equals(fieldB.getType()) &&
            fieldA.getName().equals(mockName);
    }

    private boolean isCandidateMatch(
        Field candidateFieldToBeInjected, Field otherCandidateField, String mockName) {
        return !otherCandidateField.equals(candidateFieldToBeInjected) &&
            isSameTypeAndName(otherCandidateField, candidateFieldToBeInjected, mockName);
    }

    private boolean anotherCandidateMatchesMockName(
        final Collection<Object> mocks,
        final Field candidateFieldToBeInjected,
        final List<Field> allRemainingCandidateFields) {
        String mockName = getMockName(mocks.iterator().next()).toString();

        for (Field otherCandidateField : allRemainingCandidateFields) {
            if (isCandidateMatch(candidateFieldToBeInjected, otherCandidateField, mockName)) {
                return true;
            }
        }
        return false;
    }


}
