/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InheritanceIssuePositiveJUnitTest {

    @InjectMocks
    OuterClass outerClass;

    @InjectMocks
    @Spy
    InnerClass innerClass;

    @Before
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
