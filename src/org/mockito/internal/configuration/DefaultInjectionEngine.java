/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldSetter;

/**
 * Initializes mock/spies dependencies for fields annotated with &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public class DefaultInjectionEngine {
	
    private final MockUtil mockUtil = new MockUtil();

    // for each tested
    // - for each field of tested
    //   - find mock candidate by type
    //   - if more than *one* find mock candidate on name
    //   - if one mock candidate then set mock
    //   - else don't fail, user will then provide dependencies
	public void injectMocksOnFields(Set<Field> testClassFields, Set<Object> mocks, Object testClass) {
        for (Field field : testClassFields) {
            Object fieldInstance = null;
            boolean wasAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                fieldInstance = field.get(testClass);
            } catch (IllegalAccessException e) {
                throw new MockitoException("Problems injecting dependencies in " + field.getName(), e);
            } finally {
                field.setAccessible(wasAccessible);
            }

            // for each field in the class hierarchy
            Class<?> fieldClass = fieldInstance.getClass();
            while (fieldClass != Object.class) {
                injectMockCandidate(fieldClass, mocks, fieldInstance);
                fieldClass = fieldClass.getSuperclass();
            }
        }
    }

    private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
        // TODO refactor using a CoR, maybe configured with config.
        for(Field field : awaitingInjectionClazz.getDeclaredFields()) {
            List<Object> mockCandidates = selectMockCondidatesOnType(mocks, field.getType());
            if(mockCandidates.size() > 1) {
                mockCandidates = selectMockCandidateOnName(mockCandidates, field.getName());
            }
            if(mockCandidates.size() == 1) {
                inject(field, fieldInstance, mockCandidates.get(0));
            } else {
                // don't fail, the user need to provide other dependencies
            }
        }
    }

    private void inject(Field field, Object fieldInstance, Object matchingMock) {        
        try {
            new FieldSetter(fieldInstance, field).set(matchingMock);            
        } catch (Exception e) {
            throw new MockitoException("Problems injecting dependency in " + field.getName(), e);
        }
    }

    private List<Object> selectMockCandidateOnName(Collection<Object> mocks, String fieldName) {
        List<Object> mockNameMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if(fieldName.equals(mockUtil.getMockName(mock).toString())) {
                mockNameMatches.add(mock);
            }
        }
		return mockNameMatches;
    }

    private List<Object> selectMockCondidatesOnType(Collection<Object> mocks, Class<?> fieldClass) {
        List<Object> mockTypeMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if(fieldClass.isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }
        return mockTypeMatches;
    }
}