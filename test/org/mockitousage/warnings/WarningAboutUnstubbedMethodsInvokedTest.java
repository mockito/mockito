/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.warnings;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.ExperimentalMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
@RunWith(ExperimentalMockitoJUnitRunner.class)
public class WarningAboutUnstubbedMethodsInvokedTest extends TestBase {

    public class SomeController {

        private final IMethods methods;

        public SomeController(IMethods methods) {
            this.methods = methods;
        }

        public void control(String what) {
            String value = methods.simpleMethod(what);
            if (!"foo".equals(value)) {
                throw new IllegalArgumentException("I control only foo");
            }
        }
    }

    @Mock IMethods mock;

    @Ignore
    @Test
    public void shouldFailButPrintAWarning() throws Throwable {
        SomeController controller = new SomeController(mock);
        controller.control("foo");
    }
}