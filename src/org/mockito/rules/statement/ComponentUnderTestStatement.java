package org.mockito.rules.statement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.runners.model.Statement;
import org.mockito.rules.MockEngine;
import org.mockito.rules.ObjectCreationEngine;
import org.mockito.rules.annotations.ComponentUnderTest;

public final class ComponentUnderTestStatement extends Statement {

    private static final Class<ComponentUnderTest> ANNOTATION_FOR_TESTED_COMPONENT = ComponentUnderTest.class;

    private final Statement baseStatement;
    private final Object testClass;
    private final Class<? extends Annotation>[] annotationsToMock;
    private final MockEngine mockEngine;
    private final ObjectCreationEngine creationEngine;

    public ComponentUnderTestStatement(final Statement statement, final Object testClass,
            final Class<? extends Annotation>[] annotationsToMock, final MockEngine mockEngine,
            final ObjectCreationEngine creationEngine) {
        this.baseStatement = statement;
        this.testClass = testClass;
        this.annotationsToMock = annotationsToMock;
        this.mockEngine = mockEngine;
        this.creationEngine = creationEngine;
    }

    @Override
    public void evaluate() throws Throwable {

        injectMocks(testClass);

        baseStatement.evaluate();
    }

    private Iterable<Field> getDependencies(final Class<?> clazz) {

        final List<Field> dependencies = new LinkedList<Field>();

        final Field[] allFields = clazz.getDeclaredFields();

        for (final Field field : allFields) {
            if (shouldMock(field)) {
                dependencies.add(field);
            }
        }

        return dependencies;
    }

    private void injectMocks(final Object testClass) throws IllegalArgumentException, IllegalAccessException {
        final Field[] declaredFields = testClass.getClass().getDeclaredFields();

        for (final Field field : declaredFields) {

            field.setAccessible(true);

            if (shouldInjectMock(field)) {
                injectMocksInfoField(field, testClass);
            }
        }
    }

    private void injectMocksInfoField(final Field componentUnderTest, final Object testClass)
            throws IllegalArgumentException, IllegalAccessException {

        if (componentUnderTest.get(testClass) == null) {
            tryCreate(componentUnderTest, testClass);
        }

        mockDependenciesForComponent(componentUnderTest.get(testClass));
    }

    private void mockAll(final Object fieldValue, final Iterable<Field> dependencies) throws IllegalAccessException {
        for (final Field dependency : dependencies) {
            dependency.setAccessible(true);
            dependency.set(fieldValue, mockEngine.mock(dependency.getType()));
        }
    }

    private void mockDependenciesForComponent(final Object componentUnderTestValue) throws IllegalAccessException {

        Class<? extends Object> clazz = componentUnderTestValue.getClass();
        do {
            final Iterable<Field> dependnecies = getDependencies(clazz);
            mockAll(componentUnderTestValue, dependnecies);

            clazz = clazz.getSuperclass();

        } while (clazz != Object.class); // Enum?
    }

    private boolean shouldInjectMock(final Field field) {
        for (final Annotation foundAnnotation : field.getDeclaredAnnotations()) {
            if (foundAnnotation.annotationType().equals(ANNOTATION_FOR_TESTED_COMPONENT)) {
                return true;
            }
        }

        return false;
    }

    private boolean shouldMock(final Field field) {
        for (final Class<?> expectedAnnotation : annotationsToMock) {
            for (final Annotation foundAnnotation : field.getDeclaredAnnotations()) {
                if (expectedAnnotation.equals(foundAnnotation.annotationType())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void tryCreate(final Field componentUnderTest, final Object testClass) throws IllegalArgumentException,
            IllegalAccessException {
        componentUnderTest.set(testClass, creationEngine.create(componentUnderTest.getType()));
    }
}
