/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.fieldscanner;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * Scan mocks, and prepare them if needed.
 */
public class MockScanner extends FieldScanner<Object> {

    private final MockUtil mockUtil = new MockUtil();

    private Object mockInstance;

    public MockScanner(Object testClassInstance, Class<?> clazz) {
        super(testClassInstance, clazz);
    }

    @Override
    protected boolean hasCorrectAnnotation(Field field) {
        this.mockInstance = new FieldReader(this.testClassInstance, field).read();

        return mockInstance != null
                && (isAnnotatedByMockOrSpy(field) || isMockOrSpy(mockInstance));
    }

    @Override
    protected Object getObjectToAdd(Field field) {
        if (isMockOrSpy(mockInstance)) {
            mockUtil.maybeRedefineMockName(mockInstance, field.getName());
        }

        return mockInstance;
    }

    private boolean isAnnotatedByMockOrSpy(Field field) {
        // When a @Spy is also @Injectmocks, it is a real object. Therefore it is treated as @Real.
        return (null != field.getAnnotation(Spy.class) && null == field.getAnnotation(InjectMocks.class))
                || null != field.getAnnotation(Mock.class)
                || null != field.getAnnotation(MockitoAnnotations.Mock.class);
    }

    private boolean isMockOrSpy(Object instance) {
        return mockUtil.isMock(instance)
                || mockUtil.isSpy(instance);
    }
}
