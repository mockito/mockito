/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.IArgumentMatcher;
import org.mockito.internal.matchers.NotNull;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest extends RequiresValidState {

    @Test(expected=IllegalArgumentException.class)
    public void shouldScreamWhenMatchersNull() throws Exception {
        new InvocationMatcher(new InvocationBuilder().toInvocation(), null);
    }
    
    @Test
    public void shouldBeACitizenOfHashes() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().args("blah").toInvocation();
        
        Map map = new HashMap();
        map.put(new InvocationMatcher(invocation), "one");
        map.put(new InvocationMatcher(invocationTwo), "two");
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void shouldNotEqualIfNumberOfArgumentsDiffer() throws Exception {
        InvocationMatcher withOneArg = new InvocationMatcher(new InvocationBuilder().args("test").toInvocation());
        InvocationMatcher withTwoArgs = new InvocationMatcher(new InvocationBuilder().args("test", 100).toInvocation());

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }
    
    @Test
    public void shouldToStringWithMatchers() throws Exception {
        IArgumentMatcher m = NotNull.NOT_NULL;
        InvocationMatcher notNull = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(m));
        IArgumentMatcher mTwo = new Equals('x');
        InvocationMatcher equals = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(mTwo));

        assertEquals("Object.simpleMethod(notNull())", notNull.toString());
        assertEquals("Object.simpleMethod('x')", equals.toString());
    }
}
