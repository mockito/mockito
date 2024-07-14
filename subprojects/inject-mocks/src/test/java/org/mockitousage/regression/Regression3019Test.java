/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify regression https://github.com/mockito/mockito/issues/3019 is fixed.
 */
@ExtendWith(MockitoExtension.class)
public class Regression3019Test {

    @Mock private ParameterizedInjectedObject<Something> injected;

    @InjectMocks private EntryPoint subject;

    @Test
    public void testSuccessfullyInjected() {
        assertNotNull(injected);
        assertTrue(MockUtil.isMock(injected));
        assertNotNull(subject);
        assertNotNull(subject.object);
        // test it does not throw NPE
        subject.init();
        Mockito.verify(injected).init();
    }

    static class Something {}

    static class ParameterizedInjectedObject<T extends Something> {
        public void init() {}
    }

    static class AbstractGenericClass<T extends Something> {

        ParameterizedInjectedObject<T> object;

        public void init() {
            object.init();
        }
    }

    static class EntryPoint extends AbstractGenericClass<Something> {

        @Override
        public void init() {
            super.init();
            // do other things ...
        }
    }
}
