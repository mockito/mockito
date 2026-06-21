/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Field;

import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.MemberAccessor;

/**
 * Shared logic to turn the value currently held by a &#64;{@link org.mockito.Spy} field into a spy.
 */
public final class SpyAnnotationUtil {

    private SpyAnnotationUtil() {}

    /**
     * Turns the value currently held by the given field into a spy.
     *
     * <p>If the value is already a mock (for example because {@code MockitoAnnotations.openMocks}
     * was called more than once) it is simply reset. Otherwise, a spy of the value is created and
     * written back into the field.
     *
     * @param field the {@code @Spy} annotated field being processed
     * @param instance the current value held by the field, which must not be {@code null}
     * @param accessor the accessor used to write the spy back into the field
     * @param fieldOwner the object on which the field is declared
     * @throws IllegalAccessException if the spy cannot be written back into the field
     */
    public static void resetOrCreateSpy(
            Field field, Object instance, MemberAccessor accessor, Object fieldOwner)
            throws IllegalAccessException {
        if (MockUtil.isMock(instance)) {
            // instance has been spied earlier, e.g. when MockitoAnnotations.openMocks is called
            // more than once
            Mockito.reset(instance);
        } else {
            accessor.set(field, fieldOwner, createSpy(field, instance));
        }
    }

    /**
     * Creates a spy of the given instance, deriving the mock settings from the field.
     *
     * @param field the {@code @Spy} annotated field, used to derive the mock settings
     * @param instance the instance to spy on
     * @return the created spy
     */
    private static Object createSpy(Field field, Object instance) {
        MockSettings settings =
                withSettings()
                        .genericTypeToMock(field.getGenericType())
                        .spiedInstance(instance)
                        .defaultAnswer(CALLS_REAL_METHODS)
                        .name(field.getName());
        applyMockMaker(field, settings);
        return Mockito.mock(instance.getClass(), settings);
    }

    /**
     * Applies the {@link Spy#mockMaker()} of the field, if any, to the given settings so that a spy
     * can opt into a specific {@link org.mockito.plugins.MockMaker}.
     *
     * @param field the {@code @Spy} annotated field
     * @param settings the settings to apply the mock maker to; untouched if no mock maker specified
     */
    static void applyMockMaker(Field field, MockSettings settings) {
        String mockMaker = field.getAnnotation(Spy.class).mockMaker();
        if (!mockMaker.isEmpty()) {
            settings.mockMaker(mockMaker);
        }
    }
}
