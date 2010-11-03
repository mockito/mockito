package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * This node returns an actual injecter which will be either :
 *
 * <ul>
 * <li>an {@link OngoingInjecter} that do nothing if a candidate couldn't be found</li>
 * <li>an {@link OngoingInjecter} that will try to inject the candidate trying first the property setter then if not possible try the field access</li>
 * </ul>
 */
public class FinalMockCandidateFilter implements MockCandidateFilter {
    public OngoingInjecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
        if(mocks.size() == 1) {
            final Object matchingMock = mocks.iterator().next();

            return new OngoingInjecter() {
                public boolean thenInject() {
                    try {
                        if (!new BeanPropertySetter(fieldInstance, field).set(matchingMock)) {
                            new FieldSetter(fieldInstance, field).set(matchingMock);
                        }
                    } catch (Exception e) {
                        throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
                    }
                    return true;
                }
            };
        }

        return new OngoingInjecter() {
            public boolean thenInject() {
                return false;
            }
        };

    }
}
