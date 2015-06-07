/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.reflect.Field;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.GenericMaster;

/**
 * Instantiate {@link ArgumentCaptor} a field annotated by &#64;Captor.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CaptorAnnotationProcessor implements FieldAnnotationProcessor<Captor> {
    public Object process(final Captor annotation, final Field field) {
        final Class<?> type = field.getType();
        if (!ArgumentCaptor.class.isAssignableFrom(type)) {
            throw new MockitoException("@Captor field must be of the type ArgumentCaptor.\n" + "Field: '"
               + field.getName() + "' has wrong type\n"
               + "For info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
        }
        final Class cls = new GenericMaster().getGenericType(field);
        return ArgumentCaptor.forClass(cls);
    }
}
