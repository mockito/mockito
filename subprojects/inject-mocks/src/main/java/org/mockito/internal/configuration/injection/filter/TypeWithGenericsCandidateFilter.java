/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.generic.GenericTypeMatch;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.internal.exceptions.Reporter.moreThanOneMockCandidate;

/**
 * This class acts as a replacement for {@link TypeBasedCandidateFilter} as it applies a more in-depth analysis
 * of generic type usage in context of {@link org.mockito.InjectMocks}.
 * The rules which injections are allowed and which are not are derived from the Java language.
 * If the corresponding assignment with <code>=</code> would be allowed, the injection is allowed, too.
 */
public class TypeWithGenericsCandidateFilter implements MockCandidateFilter {

    private final MockCandidateFilter next;

    public TypeWithGenericsCandidateFilter() {
        this.next = new NameBasedCandidateFilter(new TerminalMockCandidateFilter());
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
                MockCreationSettings<?> mockSettings = MockUtil.getMockSettings(mock);
                Type genericMockType = mockSettings.getGenericTypeToMock();
                Class<?> rawMockType = mockSettings.getTypeToMock();
                if (genericMockType != null && rawMockType != null) {
                    // be more specific if generic type information is available
                    GenericTypeMatch targetDeclarationTypeMatch =
                            GenericTypeMatch.ofField(injectMocksField);
                    // we need to populate generic type information from injectMockField to
                    // candidateFieldToBeInjected by stepping up the type hierarchy
                    // finding the place where it is declared
                    Optional<GenericTypeMatch> targetTypeMatch =
                            targetDeclarationTypeMatch.findDeclaredField(
                                    candidateFieldToBeInjected);
                    if (targetTypeMatch.isPresent()) {
                        // we lost the field declared with @Mock at this place but use the
                        // information provided by MockCreationSettings instead
                        GenericTypeMatch sourceTypeMatch =
                                GenericTypeMatch.ofGenericAndRawType(genericMockType, rawMockType);
                        // with generic type information collected, try to match mock with candidate
                        // field
                        if (targetTypeMatch.get().matches(sourceTypeMatch)) {
                            mockTypeMatches.add(mock);
                        }
                        // else filter out mock, as generic types don't match
                    }
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
