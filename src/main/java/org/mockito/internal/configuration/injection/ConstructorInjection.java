/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;

import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
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

    public ConstructorInjection() { }

    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        try {
            SimpleArgumentResolver simpleArgumentResolver = new SimpleArgumentResolver(mockCandidates);
            FieldInitializationReport report = new FieldInitializer(fieldOwner, field, simpleArgumentResolver).initialize();

            return report.fieldWasInitializedUsingContructorArgs();
        } catch (MockitoException e) {
            if(e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw fieldInitialisationThrewException(field, realCause);
            }
            // other causes should be fine
            return false;
        }

    }

    /**
     * Returns mocks that match the argument type, if not possible assigns null.
     */
    static class SimpleArgumentResolver implements ConstructorArgumentResolver {
        final Set<Object> objects;

        public SimpleArgumentResolver(Set<Object> objects) {
            this.objects = objects;
        }

        public Object[] resolveTypeInstances(Class<?>... argTypes) {
            List<Object> argumentInstances = new ArrayList<Object>(argTypes.length);
            for (Class<?> argType : argTypes) {
                argumentInstances.add(objectThatIsAssignableFrom(argType));
            }
            return argumentInstances.toArray();
        }

        private Object objectThatIsAssignableFrom(Class<?> argType) {
            for (Object object : objects) {
                if(argType.isAssignableFrom(object.getClass())) return object;
            }
            return null;
        }
    }

}
