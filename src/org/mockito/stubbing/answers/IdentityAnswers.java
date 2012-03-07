package org.mockito.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public enum IdentityAnswers implements Answer<Object> {
	RETURN_FIRST_PARAMETER(RETURN_PARAMETER(0)), //
	RETURN_SECOND_PARAMETER(RETURN_PARAMETER(1)), //
	RETURN_LAST_PARAMETER(RETURN_PARAMETER(-1));

	public static <T> Answer<T> RETURN_PARAMETER(final int index) {
		return new Answer<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T answer(InvocationOnMock invocation) throws Throwable {
				int actualIndex = (index == -1) ? invocation.getArguments().length - 1
						: index;
				return (T) invocation.getArguments()[actualIndex];
			}
		};
	}

	private final Answer<Object> answer;

	private IdentityAnswers(Answer<Object> answer) {
		this.answer = answer;
	}

	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		return answer.answer(invocation);
	}

}
