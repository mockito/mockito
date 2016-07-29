/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.scanner;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;
import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * Scan mocks, and prepare them if needed.
 */
public class MockScanner {
    private final Object instance;
    private final Class<?> clazz;

    /**
     * Creates a MockScanner.
     *
     * @param instance The test instance
     * @param clazz    The class in the type hierarchy of this instance.
     */
    public MockScanner(Object instance, Class<?> clazz) {
        this.instance = instance;
        this.clazz = clazz;
    }

    /**
     * Add the scanned and prepared mock instance to the given collection.
     *
     * <p>
     * The preparation of mocks consists only in defining a MockName if not already set.
     * </p>
     *
     * @param mocks Set of mocks
     */
    public void addPreparedMocks(Set<Object> mocks) {
        mocks.addAll(scan());
    }

    /**
     * Scan and prepare mocks for the given <code>testClassInstance</code> and <code>clazz</code> in the type hierarchy.
     *
     * @return A prepared set of mock
     */
    private Set<Object> scan() {
        Set<Object> mocks = newMockSafeHashSet();
        for (Field field : clazz.getDeclaredFields()) {
            // mock or spies only
            FieldReader fieldReader = new FieldReader(instance, field);

            Object mockInstance = preparedMock(fieldReader.read(), field);
            if (mockInstance != null) {
                mocks.add(mockInstance);
            }
        }
        return mocks;
    }

    private Object preparedMock(Object instance, Field field) {
        if (isAnnotatedByMockOrSpy(field)) {
            return instance;
        } 
        if (isMockOrSpy(instance)) {
            MockUtil.maybeRedefineMockName(instance, field.getName());
            return instance;
        }
        return null;
    }

    private boolean isAnnotatedByMockOrSpy(Field field) {
        return field.isAnnotationPresent(Spy.class) || field.isAnnotationPresent(Mock.class);
    }

    private boolean isMockOrSpy(Object instance) {
        return MockUtil.isMock(instance)
                || MockUtil.isSpy(instance);
    }
}
