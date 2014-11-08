package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Modifier;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PartialMockAnswer implements Answer<Object> {

	private final Answer<?> answerForAbstractMethod;

	public PartialMockAnswer(Answer<?> answerForAbstractMethod) {
		this.answerForAbstractMethod = answerForAbstractMethod;
	}

	@Override public Object answer(InvocationOnMock invocation) throws Throwable {
		if (Modifier.isAbstract(invocation.getMethod().getModifiers())) {
			return answerForAbstractMethod.answer(invocation);
		} else {
			return invocation.callRealMethod();
		}
	}
}
