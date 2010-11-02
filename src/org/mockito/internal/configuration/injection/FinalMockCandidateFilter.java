package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * This node returns an actual injecter which will be either :
 *
 * <ul>
 * <li>an {@link Injecter} that do nothing if a candidate couldn't be found</li>
 * <li>an {@link Injecter} that do will inject the final candidate to the field</li>
 * </ul>
 */
public class FinalMockCandidateFilter implements MockCandidateFilter {
    public Injecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
        if(mocks.size() == 1) {
            final Object matchingMock = mocks.iterator().next();

            return new Injecter() {
                public boolean thenInject() {
                    try {
                        new FieldSetter(fieldInstance, field).set(matchingMock);
                    } catch (Exception e) {
                        throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                    }
                    return true;
                }
            };
        }

        return new Injecter() {
            public boolean thenInject() {
                return false;
            }
        };

    }
}