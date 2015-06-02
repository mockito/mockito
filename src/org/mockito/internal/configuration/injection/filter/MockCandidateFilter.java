/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public interface MockCandidateFilter {

    OngoingInjecter filterCandidate(
            Collection<Object> mocks,
            Field fieldToBeInjected,
            List<Field> fields, Object instance
    );

}
