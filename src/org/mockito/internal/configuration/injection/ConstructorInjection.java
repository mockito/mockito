package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Injection strategy based on constructor.
 *
 * <p>
 * The strategy will search for the constructor with most parameters
 * and try to resolve mocks by type.
 * </p>
 *
 * <blockquote>
 * TODO on missing mock type, shall it abandon or create "noname" mocks.
 * TODO and what if the arg type is not mockable.
 * </blockquote>
 *
 * <p>
 * For now the algorithm tries to create anonymous mocks if an argument type is missing.
 * If not possible the algorithm abandon resolution.
 * </p>
 */
public class ConstructorInjection extends MockInjectionStrategy {

    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {

        // new FieldConstructorInitializer(field, fieldOwner).initialize(mockCandidates);

        return false;
    }

}
