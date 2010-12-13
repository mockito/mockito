/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.util.Set;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.AccessibilityChanger;
import org.mockito.internal.util.reflection.ConstructorInitializer;
import org.mockito.internal.util.reflection.FieldInitializer;

/**
 * Initializes mock/spies dependencies for fields annotated with
 * &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public class DefaultInjectionEngine {

	private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(
			new NameBasedCandidateFilter(new FinalMockCandidateFilter()));

	// for each tested
	// - for each field of tested
	// - find mock candidate by type
	// - if more than *one* find mock candidate on name
	// - if one mock candidate then set mock
	// - else don't fail, user will then provide dependencies
	public void injectMocksOnFields(Set<Field> testClassFields,
			Set<Object> mocks, Object testClass) {
		for (Field field : testClassFields) {
			if (hasDefaultConstructor(field, testClass) || alreadyInitialized(field, testClass)) {
				setterInject(mocks, testClass, field);
			} else {
				constructorInject(mocks, testClass, field);
			}
		}
	}

	private void constructorInject(Set<Object> mocks, Object testClass,
			Field field) {
		new ConstructorInitializer(field, testClass).initialize(mocks);
	}

	private void setterInject(Set<Object> mocks, Object testClass, Field field) {
		try {
			Object fieldInstance = new FieldInitializer(testClass, field)
					.initialize();
			Class<?> fieldClass = fieldInstance.getClass();
			// for each field in the class hierarchy
			while (fieldClass != Object.class) {
				injectMockCandidate(fieldClass, mocks, fieldInstance);
				fieldClass = fieldClass.getSuperclass();
			}
		} catch (Exception e) {
			new Reporter().cannotInitializeForInjectMocksAnnotation(
					field.getName(), e);
		}

	}

	private boolean hasDefaultConstructor(Field field, Object testClass) {
		try {
			field.getType().getDeclaredConstructor();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean alreadyInitialized(Field field, Object testClass) {
		final AccessibilityChanger changer = new AccessibilityChanger();
		changer.enableAccess(field);
		try {
			return field.get(testClass) != null;
		} catch (Exception e) {
			return false;
		} finally {
			changer.safelyDisableAccess(field);
		}
	}

	private void injectMockCandidate(Class<?> awaitingInjectionClazz,
			Set<Object> mocks, Object fieldInstance) {
		for (Field field : awaitingInjectionClazz.getDeclaredFields()) {
			mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)
					.thenInject();
		}
	}

}
