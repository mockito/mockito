/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.mockito.internal.matchers.*;

@SuppressWarnings("unchecked")
public class ExpectedInvocationTest {

    @Test
    public void shouldBeACitizenOfHashes() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().args("blah").toInvocation();
        
        Map map = new HashMap();
        map.put(invocation, "one");
        map.put(invocationTwo, "two");
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void shouldNotEqualIfNumberOfArgumentsDiffer() throws Exception {
        ExpectedInvocation withOneArg = new ExpectedInvocation(new InvocationBuilder().args("test").toInvocation(), null);
        ExpectedInvocation withTwoArgs = new ExpectedInvocation(new InvocationBuilder().args("test", 100).toInvocation(), null);

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }
    
    @Test
    public void shouldEqualWhenMatchersEqual() throws Exception {
        IArgumentMatcher m = new Equals(1);
        IArgumentMatcher mTwo = new Equals(2);
        ExpectedInvocation withMatchers = new ExpectedInvocation(new InvocationBuilder().toInvocation(), Arrays.asList(m));
        ExpectedInvocation withEqualMatchers = new ExpectedInvocation(new InvocationBuilder().toInvocation(), Arrays.asList(m));
        ExpectedInvocation withoutEqualMatchers = new ExpectedInvocation(new InvocationBuilder().toInvocation(), Arrays.asList(mTwo));
        
        assertTrue(withMatchers.equals(withEqualMatchers));
        assertFalse(withMatchers.equals(withoutEqualMatchers));
    }
    
    @Test
    public void shouldToStringWithMatchers() throws Exception {
        IArgumentMatcher m = NotNull.NOT_NULL;
        ExpectedInvocation notNull = new ExpectedInvocation(new InvocationBuilder().toInvocation(), Arrays.asList(m));
        IArgumentMatcher mTwo = new Equals('x');
        ExpectedInvocation equals = new ExpectedInvocation(new InvocationBuilder().toInvocation(), Arrays.asList(mTwo));

        assertEquals("Object.simpleMethod(notNull())", notNull.toString());
        assertEquals("Object.simpleMethod('x')", equals.toString());
    }
}
