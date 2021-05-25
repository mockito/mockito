/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeepStubbingWithJUnitRunnerTest {

    private final SomeClass someClass = new SomeClass();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Root root;

    @Test
    public void deep_stubs_dont_trigger_unnecessary_stubbing_exception() {
        // when
        someClass.someMethod(root);

        // then unnecessary stubbing exception is not thrown
    }

    public static class SomeClass {
        void someMethod(Root root) {
            root.getFoo().getBar();
        }
    }

    interface Root {
        Foo getFoo();
    }

    interface Foo {
        Bar getBar();
    }

    interface Bar {}
}
