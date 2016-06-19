/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.InvocationOnMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ReturnsArgumentAtTest {
    @Test
    public void should_be_able_to_return_the_first_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(0).answer(invocationWith("A", "B"))).isEqualTo("A");
    }

    @Test
    public void should_be_able_to_return_the_second_parameter()
            throws Throwable {
        assertThat(new ReturnsArgumentAt(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
    }

    @Test
    public void should_be_able_to_return_the_last_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(-1).answer(invocationWith("A"))).isEqualTo("A");
        assertThat(new ReturnsArgumentAt(-1).answer(invocationWith("A", "B"))).isEqualTo("B");
    }

    @Test
    public void should_be_able_to_return_the_specified_parameter() throws Throwable {
        assertThat(new ReturnsArgumentAt(0).answer(invocationWith("A", "B", "C"))).isEqualTo("A");
        assertThat(new ReturnsArgumentAt(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
        assertThat(new ReturnsArgumentAt(2).answer(invocationWith("A", "B", "C"))).isEqualTo("C");
    }

    @Test
    public void should_raise_an_exception_if_index_is_not_in_allowed_range_at_creation_time() throws Throwable {
        try {
            new ReturnsArgumentAt(-30);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("argument index")
                    .containsIgnoringCase("positive number")
                    .contains("1")
                    .containsIgnoringCase("last argument");
        }
    }

    private static InvocationOnMock invocationWith(Object... parameters) {
        return new InvocationBuilder().method("varargsReturningString").argTypes(Object[].class)
             .args(parameters).toInvocation();
    }

}
