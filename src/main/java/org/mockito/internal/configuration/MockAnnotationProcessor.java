/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.internal.util.StringUtil.join;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Supplier;

/**
 * Instantiates a mock on a field annotated by {@link Mock}
 */
public class MockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
    @Override
    public Object process(Mock annotation, Field field) {
        return processAnnotationForMock(
                annotation, field.getType(), field::getGenericType, field.getName());
    }

    public static Object processAnnotationForMock(
            Mock annotation, Class<?> type, Supplier<Type> genericType, String name) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) { // never null
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if ("".equals(annotation.name())) {
            mockSettings.name(name);
        } else {
            mockSettings.name(annotation.name());
        }
        if (annotation.serializable()) {
            mockSettings.serializable();
        }
        if (annotation.stubOnly()) {
            mockSettings.stubOnly();
        }
        if (annotation.lenient()) {
            mockSettings.lenient();
        }

        // see @Mock answer default value
        mockSettings.defaultAnswer(annotation.answer());

        if (type == MockedStatic.class) {
            return Mockito.mockStatic(
                    inferParameterizedType(
                            genericType.get(), name, MockedStatic.class.getSimpleName()),
                    mockSettings);
        } else if (type == MockedConstruction.class) {
            return Mockito.mockConstruction(
                    inferParameterizedType(
                            genericType.get(), name, MockedConstruction.class.getSimpleName()),
                    mockSettings);
        } else {
            return Mockito.mock(type, mockSettings);
        }
    }

    static Class<?> inferParameterizedType(Type type, String name, String sort) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (arguments.length == 1) {
                if (arguments[0] instanceof Class<?>) {
                    return (Class<?>) arguments[0];
                }
            }
        }
        throw new MockitoException(
                join(
                        "Mockito cannot infer a static mock from a raw type for " + name,
                        "",
                        "Instead of @Mock " + sort + " you need to specify a parameterized type",
                        "For example, if you would like to mock Sample.class, specify",
                        "",
                        "@Mock " + sort + "<Sample>",
                        "",
                        "as the type parameter. If the type is itself parameterized, it should be specified as raw type."));
    }
}
