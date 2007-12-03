/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.mockito.internal.matchers.*;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class ExpectedInvocationTest extends RequiresValidState {

    @Test(expected=IllegalArgumentException.class)
    public void shouldScreamWhenMatchersNull() throws Exception {
        new ExpectedInvocation(new InvocationBuilder().toInvocation(), null);
    }
    
    @Test
    public void shouldBeACitizenOfHashes() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().args("blah").toInvocation();
        
        Map map = new HashMap();
        map.put(new ExpectedInvocation(invocation), "one");
        map.put(new ExpectedInvocation(invocationTwo), "two");
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void shouldNotEqualIfNumberOfArgumentsDiffer() throws Exception {
        ExpectedInvocation withOneArg = new ExpectedInvocation(new InvocationBuilder().args("test").toInvocation());
        ExpectedInvocation withTwoArgs = new ExpectedInvocation(new InvocationBuilder().args("test", 100).toInvocation());

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }
    
    @Test
    public void shouldToStringWithMatchers() throws Exception {
        IArgumentMatcher m = NotNull.NOT_NULL;
        ExpectedInvocation notNull = new ExpectedInvocation(new InvocationBuilder().toInvocation(), asList(m));
        IArgumentMatcher mTwo = new Equals('x');
        ExpectedInvocation equals = new ExpectedInvocation(new InvocationBuilder().toInvocation(), asList(mTwo));

        assertEquals("Object.simpleMethod(notNull())", notNull.toString());
        assertEquals("Object.simpleMethod('x')", equals.toString());
    }
}
