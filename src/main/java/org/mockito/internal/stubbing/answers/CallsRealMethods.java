/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.Answers.RETURNS_DEFAULTS;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Optional Answer that adds partial mocking support
 * <p>
 * {@link Answer} can be used to define the return values of unstubbed invocations.
 * <p>
 * This implementation can be helpful when working with legacy code.
 * When this implementation is used, unstubbed methods will delegate to the real implementation.
 * This is a way to create a partial mock object that calls real methods by default.
 * <p>
 * As usual you are going to read <b>the partial mock warning</b>:
 * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
 * How does partial mock fit into this paradigm? Well, it just doesn't... 
 * Partial mock usually means that the complexity has been moved to a different method on the same object.
 * In most cases, this is not the way you want to design your application.
 * <p>
 * However, there are rare cases when partial mocks come handy: 
 * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
 * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
 * <p>
 */
public class CallsRealMethods implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 9057165148930624087L;

    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (Modifier.isAbstract(invocation.getMethod().getModifiers())) {
            return RETURNS_DEFAULTS.answer(invocation);
        }
        return invocation.callRealMethod();
    }
}
