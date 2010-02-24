/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.AnyVararg;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest extends TestBase {

    private InvocationMatcher simpleMethod;
    @Mock private IMethods mock; 
    
    @Before
    public void setup() {
        simpleMethod = new InvocationBuilder().mock(mock).simpleMethod().toInvocationMatcher();
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
        PrintingFriendlyInvocation withOneArg = new InvocationMatcher(new InvocationBuilder().args("test").toInvocation());
        PrintingFriendlyInvocation withTwoArgs = new InvocationMatcher(new InvocationBuilder().args("test", 100).toInvocation());

        assertFalse(withOneArg.equals(null));
        assertFalse(withOneArg.equals(withTwoArgs));
    }
    
    @Test
    public void shouldToStringWithMatchers() throws Exception {
        Matcher m = NotNull.NOT_NULL;
        InvocationMatcher notNull = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(m));
        Matcher mTwo = new Equals('x');
        InvocationMatcher equals = new InvocationMatcher(new InvocationBuilder().toInvocation(), asList(mTwo));

        assertContains("simpleMethod(notNull())", notNull.toString());
        assertContains("simpleMethod('x')", equals.toString());
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
    
    @Test
    public void shouldCaptureArgumentsFromInvocation() throws Exception {
        //given
        Invocation invocation = new InvocationBuilder().args("1", 100).toInvocation();
        CapturingMatcher capturingMatcher = new CapturingMatcher();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new Equals("1"), capturingMatcher));
        
        //when
        invocationMatcher.captureArgumentsFrom(invocation);
        
        //then
        assertEquals(1, capturingMatcher.getAllValues().size());
        assertEquals(100, capturingMatcher.getLastValue());
    }

    @Test
    public void shouldMatchVarargsUsingAnyVarargs() throws Exception {
        //given
        mock.varargs("1", "2");
        Invocation invocation = getLastInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(AnyVararg.ANY_VARARG));

        //when
        boolean match = invocationMatcher.matches(invocation);

        //then
        assertTrue(match);
    }

    @Test
    public void shouldMatchCaptureArgumentsWhenArgsCountDoesNOTMatch() throws Exception {
        //given
        mock.varargs();
        Invocation invocation = getLastInvocation();

        //when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, (List) asList(new LocalizedMatcher(AnyVararg.ANY_VARARG)));

        //then
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void shouldCreateFromInvocations() throws Exception {
        //given
        Invocation i = new InvocationBuilder().toInvocation();
        //when
        List<InvocationMatcher> out = InvocationMatcher.createFrom(asList(i));
        //then
        assertEquals(1, out.size());
        assertEquals(i, out.get(0).getInvocation());
    }
}