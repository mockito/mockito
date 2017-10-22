/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PrintingVerboseTypesWithArgumentsTest extends TestBase {

    class Boo {
        public void withLong(long x) {
        }

        public void withLongAndInt(long x, int y) {
        }
    }

    @Test
    public void should_not_report_argument_types_when_to_string_is_the_same() {
        //given
        Boo boo = mock(Boo.class);
        boo.withLong(100);

        try {
            //when
            verify(boo).withLong(eq(100));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertThat(e)
                .hasMessageContaining("withLong((Integer) 100);")
                .hasMessageContaining("withLong((Long) 100L);");
        }
    }

    @Test
    public void should_show_the_type_of_only_the_argument_that_doesnt_match() {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);

        try {
            //when
            verify(boo).withLongAndInt(eq(100), eq(200));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertThat(e)
                .hasMessageContaining("withLongAndInt((Integer) 100, 200)")
                .hasMessageContaining("withLongAndInt((Long) 100L, 200)");
        }
    }

    @Test
    public void should_show_the_type_of_the_mismatching_argument_when_output_descriptions_for_invocations_are_different() {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);

        try {
            //when
            verify(boo).withLongAndInt(eq(100), any(Integer.class));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            Assertions.assertThat(e.getMessage())
                      .contains("withLongAndInt(\n" +
                                        "    (Long) 100L,\n" +
                                        "    200\n" +
                                        ")")
                      .contains("withLongAndInt(\n" +
                                        "    (Integer) 100,\n" +
                                        "    <any java.lang.Integer>\n" +
                                        ")");
        }
    }

    @Test
    public void should_not_show_types_when_argument_value_is_different() {
        //given
        Boo boo = mock(Boo.class);
        boo.withLongAndInt(100, 200);

        try {
            //when
            verify(boo).withLongAndInt(eq(100L), eq(230));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertThat(e)
                .hasMessageContaining("withLongAndInt(100L, 200)")
                .hasMessageContaining("withLongAndInt(100L, 230)");
        }
    }

    class Foo {

        private final int x;

        public Foo(int x) {
            this.x = x;
        }

        public boolean equals(Object obj) {
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
