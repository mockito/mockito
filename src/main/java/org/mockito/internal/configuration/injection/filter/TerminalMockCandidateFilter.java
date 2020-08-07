/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import static org.mockito.internal.exceptions.Reporter.cannotInjectDependency;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.plugins.MemberAccessor;

/**
 * This node returns an actual injecter which will be either :
 *
 * <ul>
 * <li>an {@link OngoingInjector} that do nothing if a candidate couldn't be found</li>
 * <li>an {@link OngoingInjector} that will try to inject the candidate trying first the property setter then if not possible try the field access</li>
 * </ul>
 */
public class TerminalMockCandidateFilter implements MockCandidateFilter {
    public OngoingInjector filterCandidate(
            final Collection<Object> mocks,
            final Field candidateFieldToBeInjected,
            final List<Field> allRemainingCandidateFields,
            final Object injectee) {
        if (mocks.size() == 1) {
            final Object matchingMock = mocks.iterator().next();

            MemberAccessor accessor = Plugins.getMemberAccessor();
            return () -> {
                try {
                    if (!new BeanPropertySetter(injectee, candidateFieldToBeInjected)
                            .set(matchingMock)) {
                        accessor.set(candidateFieldToBeInjected, injectee, matchingMock);
                    }
                } catch (RuntimeException | IllegalAccessException e) {
                    throw cannotInjectDependency(candidateFieldToBeInjected, matchingMock, e);
                }
                return matchingMock;
            };
        }

        return OngoingInjector.nop;
    }
}
