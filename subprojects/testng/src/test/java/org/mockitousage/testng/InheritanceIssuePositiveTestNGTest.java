/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.testng;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InheritanceIssuePositiveTestNGTest extends BaseClass {

    @InjectMocks
    OuterClass outerClass;

    @InjectMocks
    @Spy
    InnerClass innerClass;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldInjectNestedSpySuccessfully() {
        assertThat(outerClass.getInnerClass()).isNotNull();
    }

    private static class OuterClass {
        private InnerClass innerClass;

        public InnerClass getInnerClass() {
            return innerClass;
        }

        public void setInnerClass(InnerClass innerClass) {
            this.innerClass = innerClass;
        }
    }

    private static class InnerClass {
    }
}
