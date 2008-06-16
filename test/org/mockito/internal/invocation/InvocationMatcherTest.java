/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.*;
import static org.mockito.util.ExtraMatchers.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.NotNull;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest extends TestBase {

    private InvocationMatcher simpleMethod;
    @Mock private IMethods mock; 
    
    @Before
    public void setup() {
        simpleMethod = new InvocationBuilder().mock(mock).simpleMethod().toInvocationMatcher();
    }

    public void shouldBuildEqualsMatchersWhenNullPassed() throws Exception {
        InvocationMatcher m = new InvocationMatcher(new InvocationBuilder().args("foo").toInvocation(), null);
        assertThat(m.getMatchers(), hasExactlyInOrder(new Equals("foo")));
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
        CanPrintInMultilines withOneArg = new InvocationMatcher(new InvocationBuilder().args("test").toInvocation());
        CanPrintInMultilines withTwoArgs = new InvocationMatcher(new InvocationBuilder().args("test", 100).toInvocation());

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }
    
    @Test
    public void shouldToStringWithMatchers() throws Exception {
        Matcher m = NotNull.NOT_NULL;
        InvocationMatcher notNull = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(m));
        Matcher mTwo = new Equals('x');
        InvocationMatcher equals = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(mTwo));

        assertThat(notNull.toString(), contains("simpleMethod(notNull())"));
        assertThat(equals.toString(), contains("simpleMethod('x')"));
    }
    
    @Test
    public void shouldKnowIfIsSimilarTo() throws Exception {
        Invocation same = new InvocationBuilder().mock(mock).simpleMethod().toInvocation();
        assertTrue(simpleMethod.hasSimilarMethod(same));
        
        Invocation different = new InvocationBuilder().mock(mock).differentMethod().toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(different));
    }
    
    @Test
    public void shouldNotBeSimilarToVerifiedInvocation() throws Exception {
        Invocation verified = new InvocationBuilder().simpleMethod().verified().toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(verified));
    }
       
    @Test
    public void shouldNotBeSimilarIfMocksAreDifferent() throws Exception {
        Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        assertFalse(simpleMethod.hasSimilarMethod(onDifferentMock));
    }    
    
    @Test
    public void shouldNotBeSimilarIfIsOverloadedButUsedWithTheSameArg() throws Exception {
        Method method = IMethods.class.getMethod("simpleMethod", String.class);
        Method overloadedMethod = IMethods.class.getMethod("simpleMethod", Object.class);
        
        String sameArg = "test";
        
        InvocationMatcher invocation = new InvocationBuilder().method(method).arg(sameArg).toInvocationMatcher();
        Invocation overloadedInvocation = new InvocationBuilder().method(overloadedMethod).arg(sameArg).toInvocation();
        
        assertFalse(invocation.hasSimilarMethod(overloadedInvocation));
    } 
    
    @Test
    public void shouldBeSimilarIfIsOverloadedButUsedWithDifferentArg() throws Exception {
        Method method = IMethods.class.getMethod("simpleMethod", String.class);
        Method overloadedMethod = IMethods.class.getMethod("simpleMethod", Object.class);
        
        InvocationMatcher invocation = new InvocationBuilder().mock(mock).method(method).arg("foo").toInvocationMatcher();
        Invocation overloadedInvocation = new InvocationBuilder().mock(mock).method(overloadedMethod).arg("bar").toInvocation();
        
        assertTrue(invocation.hasSimilarMethod(overloadedInvocation));
    }
}