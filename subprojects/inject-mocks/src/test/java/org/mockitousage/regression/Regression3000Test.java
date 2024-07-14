/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <a href="https://github.com/mockito/mockito/issues/3000">Issue #3000</a>
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings({"unused", "rawtypes"})
public class Regression3000Test {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Dependent {}

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Inject {}

    public interface Instance<T> {
        T get();
    }

    public interface ObjectMapper {}

    public interface EntityManager {}

    public interface Storable<T> {}

    public interface StorageMessage {}

    public interface AbstractEntityController<T, U> {}

    public static class MessageControllerBase<T extends StorageMessage, U extends Storable> {}

    @Dependent
    public static class StorageMessageBaseController
            extends MessageControllerBase<StorageMessage, Storable> {

        @Inject Instance<AbstractEntityController<? extends Storable, ?>> controllerInstance;
        @Inject Instance<Storable<?>> storableInstance;
        @Inject ObjectMapper objectMapper;
    }

    @Mock Instance<Storable<?>> storableInstance;
    @Mock EntityManager em;
    @Spy @InjectMocks StorageMessageBaseController testee;

    @Test
    public void testNoArrayIndexOutOfBoundsExceptionAndMockInjected() {
        assertNotNull(testee);
        assertNotNull(testee.storableInstance);
        assertEquals(storableInstance, testee.storableInstance);
    }
}