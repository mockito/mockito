/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.internal.invocation.realmethod;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.realmethod.CleanTraceRealMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockitoutil.TestBase;

public class CleanTraceRealMethodTest extends TestBase {

    @Before
    public void keepStackTracesClean() {
        makeStackTracesClean();
    }
    
    class Foo {
        public String throwSomething() {
            throw new RuntimeException();
        }
    }
    
    @Test
    public void shouldRemoveMockitoInternalsFromStackTraceWhenRealMethodThrows() throws Throwable {
        //given
        CleanTraceRealMethod realMethod = new CleanTraceRealMethod(new RealMethod() {
            public Object invoke(Object target, Object[] arguments) throws Throwable {
                return new Foo().throwSomething();
            }});
        
        //when
        try {
            realMethod.invoke(null, null);
            fail();
        //then
        } catch (Exception e) {
            assertThat(e, hasMethodInStackTraceAt(0, "throwSomething"));
            assertThat(e, hasMethodInStackTraceAt(1, "invoke"));
            assertThat(e, hasMethodInStackTraceAt(2, "shouldRemoveMockitoInternalsFromStackTraceWhenRealMethodThrows"));
        }
    }
}