/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;

import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationUtil;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.reflection.FieldReader;
import org.mockito.plugins.MemberAccessor;

/**
 * Handler for field annotated with &#64;InjectMocks and &#64;Spy.
 *
 * <p>
 * The handler assumes that field initialization AND injection already happened.
 * So if the field is still null, then nothing will happen there.
 * </p>
 */
public class SpyOnInjectedFieldsHandler extends MockInjectionStrategy {

    private final MemberAccessor accessor = Plugins.getMemberAccessor();

    @Override
    protected boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        FieldReader fieldReader = new FieldReader(fieldOwner, field);

        if (!fieldReader.isNull() && field.isAnnotationPresent(Spy.class)) {
            try {
                SpyAnnotationUtil.resetOrCreateSpy(field, fieldReader.read(), accessor, fieldOwner);
            } catch (Exception e) {
                throw new MockitoException("Problems initiating spied field " + field.getName(), e);
            }
        }

        return false;
    }
}
