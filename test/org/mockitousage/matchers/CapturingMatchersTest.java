/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")  
public class CapturingMatchersTest extends TestBase {
    
    private final class ArgumentCaptor<T> extends ArgumentMatcher<T> {
        private T argument;

        @Override
        public boolean matches(Object argument) {
            this.argument = (T) argument;
            return true;
        }
        
        public T getArgument() {
            return argument;
        }
    }

    @Mock private IMethods mock;

    @Test
    public void shouldAllowCapturingArguments() throws Exception {
        Object object = new Object();
        mock.simpleMethod(object);
        
        ArgumentCaptor captor = new ArgumentCaptor();
        verify(mock).simpleMethod(capturedArg(captor));
        
        assertSame(object, captor.getArgument());
    }

    private Object capturedArg(ArgumentCaptor captor) {
        return argThat(captor);
    }
}