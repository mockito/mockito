package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Collection;

public interface MockCandidateFilter {

    Injecter filterCandidate(
            Collection<Object> mocks,
            Field fieldToBeInjected,
            Object fieldInstance
    );

}