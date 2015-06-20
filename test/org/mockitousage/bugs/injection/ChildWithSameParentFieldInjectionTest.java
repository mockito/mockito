/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

// issue 289
@RunWith(MockitoJUnitRunner.class)
public class ChildWithSameParentFieldInjectionTest {
    @InjectMocks
    private System system;

    @Mock
    private SomeService someService;

    @Test
    public void parent_field_is_not_null() {
        assertNotNull(((AbstractSystem) system).someService);
    }

    @Test
    public void child_field_is_not_null() {
        assertNotNull(system.someService);
    }

    public static class System extends AbstractSystem {
        private SomeService someService;

        public void doSomethingElse() {
            someService.doSomething();
        }
    }

    public static class AbstractSystem {
        private SomeService someService;

        public void doSomething() {
            someService.doSomething();
        }
    }

    public static class SomeService {
        public void doSomething() {
        }
    }
}
