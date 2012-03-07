package org.mockito.stubbing.answers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class IdentityAnswersTest {
	@Test
	public void should_be_able_to_return_the_first_parameter() throws Throwable {
		assertThat(
				(String) IdentityAnswers.RETURN_FIRST_PARAMETER.answer(invocationWith(
						"A", "B")), is("A"));
	}

	@Test
	public void should_be_able_to_return_the_second_parameter()
			throws Throwable {
		assertThat(
				(String) IdentityAnswers.RETURN_SECOND_PARAMETER.answer(invocationWith(
						"A", "B", "C")), is("B"));
	}

	@Test
	public void should_be_able_to_return_the_last_parameter() throws Throwable {
		assertThat(
				(String) IdentityAnswers.RETURN_LAST_PARAMETER
						.answer(invocationWith("A")),
				is("A"));
		assertThat(
				(String) IdentityAnswers.RETURN_LAST_PARAMETER.answer(invocationWith(
						"A", "B")), is("B"));
	}

	@Test
	public void should_be_able_to_return_the_specified_parameter()
			throws Throwable {
		assertThat(
				(String) IdentityAnswers.RETURN_PARAMETER(0).answer(
						invocationWith("A", "B", "C")), is("A"));
		assertThat(
				(String) IdentityAnswers.RETURN_PARAMETER(1).answer(
						invocationWith("A", "B", "C")), is("B"));
	}

	private static InvocationOnMock invocationWith(final String... parameters) {
		@SuppressWarnings("serial")
		InvocationOnMock invocation = new InvocationOnMock() {

			@Override
			public Object getMock() {
				return null;
			}

			@Override
			public Method getMethod() {
				return null;
			}

			@Override
			public Object[] getArguments() {
				return parameters;
			}

			@Override
			public Object callRealMethod() throws Throwable {
				return null;
			}
		};
		return invocation;
	}
}
