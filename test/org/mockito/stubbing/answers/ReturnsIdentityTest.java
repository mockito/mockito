package org.mockito.stubbing.answers;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class ReturnsIdentityTest {
	@Test
	public void should_be_able_to_return_the_first_parameter() throws Throwable {
		assertThat(new ReturnsIdentity(0).answer(invocationWith("A", "B"))).isEqualTo("A");
	}

	@Test
	public void should_be_able_to_return_the_second_parameter()
			throws Throwable {
		assertThat(new ReturnsIdentity(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
	}

	@Test
	public void should_be_able_to_return_the_last_parameter() throws Throwable {
		assertThat(new ReturnsIdentity(-1).answer(invocationWith("A"))).isEqualTo("A");
		assertThat(new ReturnsIdentity(-1).answer(invocationWith("A", "B"))).isEqualTo("B");
	}

	@Test
	public void should_be_able_to_return_the_specified_parameter() throws Throwable {
		assertThat(new ReturnsIdentity(0).answer(invocationWith("A", "B", "C"))).isEqualTo("A");
		assertThat(new ReturnsIdentity(1).answer(invocationWith("A", "B", "C"))).isEqualTo("B");
		assertThat(new ReturnsIdentity(2).answer(invocationWith("A", "B", "C"))).isEqualTo("C");
	}

	@Test
	public void should_raise_an_exception_if_index_is_not_in_allowed_range_at_creation_time() throws Throwable {
        try {
            new ReturnsIdentity(-30);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("argument index")
                    .containsIgnoringCase("positive number")
                    .contains("1")
                    .containsIgnoringCase("last argument");
        }
    }

	@Test
	public void should_raise_an_exception_if_index_is_not_in_range_for_one_arg_invocation() throws Throwable {
        try {
            new ReturnsIdentity(30).answer(new InvocationBuilder().method("oneArg").arg("A").toInvocation());
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("invalid argument index")
                    .containsIgnoringCase("iMethods.oneArg")
                    .containsIgnoringCase("[0] String")
                    .containsIgnoringCase("position")
                    .contains("30");
        }
    }

	@Test
	public void should_raise_an_exception_if_index_is_not_in_range_for_no_arg_invocation() throws Throwable {
        try {
            new ReturnsIdentity(ReturnsIdentity.LAST_ARGUMENT).answer(new InvocationBuilder().method("simpleMethod").toInvocation());
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("invalid argument index")
                    .containsIgnoringCase("iMethods.simpleMethod")
                    .containsIgnoringCase("no arguments")
                    .containsIgnoringCase("last parameter wanted");
        }
    }

	private static InvocationOnMock invocationWith(final String... parameters) {
		@SuppressWarnings("serial")
		InvocationOnMock invocation = new InvocationOnMock() {

			public Object getMock() {
				return null;
			}

			public Method getMethod() {
				return null;
			}

			public Object[] getArguments() {
				return parameters;
			}

			public Object callRealMethod() throws Throwable {
				return null;
			}
		};
		return invocation;
	}
}
