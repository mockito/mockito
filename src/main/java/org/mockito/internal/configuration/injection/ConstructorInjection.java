/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
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
 * <p>
 * The algorithm tries to create anonymous mocks if an argument type is missing.
 * If not possible the algorithm abandons resolution.
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
     * Returns mocks that match the argument type, if not possible tries to assign an anonymous mock or defaults to null.
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
            return tryMocking(argType);
        }

        private Object tryMocking(Class<?> argType) {
            try {
                return MockUtil.createMock(Mockito.withSettings().build(argType));
            } catch (MockitoException exception) {
                return null;
            }
        }
    }

}
