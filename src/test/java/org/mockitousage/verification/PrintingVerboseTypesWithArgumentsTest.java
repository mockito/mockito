/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PrintingVerboseTypesWithArgumentsTest extends TestBase {

    class Boo {
        public void withLong(long x) {
        }

        public void withLongAndInt(long x, int y) {
        }
    }

    class Foo {

        private final int x;

        public Foo(int x) {
            this.x = x;
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;

            return x == ((Foo) obj).x;
        }

        public int hashCode() {
            return 1;
        }

        public String toString() {
            return "foo";
        }
    }

    @Test
    public void should_not_show_types_when_types_are_the_same_even_if_to_string_gives_the_same_result() {
        //given
        IMethods mock = mock(IMethods.class);
        mock.simpleMethod(new Foo(10));

        try {
            //when
            verify(mock).simpleMethod(new Foo(20));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertThat(e).hasMessageContaining("simpleMethod(foo)");
        }
    }
}
