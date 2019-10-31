/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NestedParallelTest {

    @Mock
    private SomeService someService;

    @InjectMocks
    private AnotherService anotherService;

    @Nested
    class NestedTest {

        @Test
        void test() {
            perform();
        }

        private void perform() {
            // when
            anotherService.callSomeService();

            // then
            Mockito.verify(someService).doSomething();
        }

        @Test
        void test2() {
            perform();
        }

        @Test
        void test3() {
            perform();
        }

        @Test
        void test4() {
            perform();
        }

        @Test
        void test5() {
            perform();
        }

        @Test
        void test6() {
            perform();
        }

        @Test
        void test7() {
            perform();
        }
    }

    public static class AnotherService {
        private final SomeService someService;

        public AnotherService(final SomeService someService) {
            this.someService = someService;
        }

        void callSomeService() {
            someService.doSomething();
        }
    }

    static class SomeService {

        void doSomething() {
        }
    }
}
