/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.Collections.*;
import static java.util.Arrays.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class RegisteredInvocationsTest {

    private RegisteredInvocations registered;
    private Invocation toLowerCaseInvocation;
    private Invocation toUpperCaseInvocation;
    private Invocation toLowerCaseInvocationTwo;
    private Invocation toLowerCaseInvocationThree;

    @Before
    public void setup() throws Exception {
        Method toLowerCase = String.class.getMethod("toLowerCase", new Class[] {});
        Method toUpperCase = String.class.getMethod("toUpperCase", new Class[] {});
        
        toLowerCaseInvocation = new Invocation("mock", toLowerCase , new Object[] {}, 1);
        toLowerCaseInvocationTwo = new Invocation("mock", toLowerCase , new Object[] {}, 2);
        toUpperCaseInvocation = new Invocation("mock", toUpperCase , new Object[] {}, 3);
        toLowerCaseInvocationThree = new Invocation("mock", toLowerCase , new Object[] {}, 4);
        
        registered = new RegisteredInvocations(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                return Arrays.asList(toLowerCaseInvocation, toLowerCaseInvocationTwo, toUpperCaseInvocation, toLowerCaseInvocationThree) ;
            }});
        
        registered.add(toLowerCaseInvocation);
        registered.add(toLowerCaseInvocationTwo);
        registered.add(toUpperCaseInvocation);
        registered.add(toLowerCaseInvocationThree);
    }

    @Test
    public void shouldAskInvocationFinderForAllInvocations() throws Exception {
        final List<Object> dummyMocks = asList(new Object(), new Object());
        final StringBuilder s = new StringBuilder();
        
        registered = new RegisteredInvocations(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                s.append("InvocationsFinder was asked");
                assertSame(dummyMocks, mocks);
                return EMPTY_LIST;
            }});
        
        registered.unverifiedInvocationChunks(VerifyingMode.inOrder(null, dummyMocks));
        assertEquals("InvocationsFinder was asked", s.toString());
    }    
    
    @Test
    public void shouldGetUnverifiedInvocationChunks() throws Exception {
        Object mock = new Object();
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(VerifyingMode.inOrder(null, asList(mock)));
        
        assertEquals(3, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(2, firstChunk.getCount());
        assertEquals(toLowerCaseInvocation, firstChunk.getInvocation());
        assertEquals(toLowerCaseInvocationTwo, firstChunk.getInvocation());
        
        InvocationChunk secondChunk = chunks.get(1);
        assertEquals(1, secondChunk.getCount());
        assertEquals(toUpperCaseInvocation, secondChunk.getInvocation());
        
        InvocationChunk thirdChunk = chunks.get(2);
        assertEquals(1, thirdChunk.getCount());
        assertEquals(toLowerCaseInvocationThree, thirdChunk.getInvocation());
    }
    
    @Test
    public void shouldGetTwoUnverifiedInvocationChunks() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(VerifyingMode.inOrder(null, asList(mock)));
        
        assertEquals(2, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(1, firstChunk.getCount());
        assertEquals(toUpperCaseInvocation, firstChunk.getInvocation());
        
        InvocationChunk secondChunk = chunks.get(1);
        assertEquals(1, secondChunk.getCount());
        assertEquals(toLowerCaseInvocationThree, secondChunk.getInvocation());
    }
    
    @Test
    public void shouldGetOneUnverifiedInvocationChunk() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new ExpectedInvocation(toUpperCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(VerifyingMode.inOrder(null, asList(mock)));
        
        assertEquals(1, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(1, firstChunk.getCount());
        assertEquals(toLowerCaseInvocationThree, firstChunk.getInvocation());
    }
    
    @Test
    public void shouldNotGetAnyInvocationChunks() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new ExpectedInvocation(toUpperCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(VerifyingMode.inOrder(null, asList(mock)));
        
        assertEquals(0, chunks.size());
    }
    
    @Test
    public void shouldMarkAllToLowerCaseAsVerified() throws Exception {
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST),VerifyingMode.times(2));
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAllToLowerCaseAsVerifiedWhenAtLeastOnceIsUsed() throws Exception {
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.atLeastOnce());
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldNeverMarkInvocationsAsVerifiedIfExpectedCountIsZero() throws Exception {
        registered.markInvocationsAsVerified(new ExpectedInvocation(toLowerCaseInvocation, EMPTY_LIST), VerifyingMode.times(0));
        
        List<Invocation> invocations = registered.all();
        assertEquals(false, invocations.get(0).isVerified());
        assertEquals(false, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromFirstChunk() throws Exception {
        VerifyingMode mode = VerifyingMode.inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        registered.markInvocationsAsVerified(new ExpectedInvocation(null, EMPTY_LIST), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromSecondChunk() throws Exception {
        VerifyingMode mode = VerifyingMode.inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        
        Invocation doesntMatter = null;
        registered.markInvocationsAsVerified(new ExpectedInvocation(doesntMatter, EMPTY_LIST), mode);
        registered.markInvocationsAsVerified(new ExpectedInvocation(doesntMatter, EMPTY_LIST), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromThirdChunk() throws Exception {
        VerifyingMode mode = VerifyingMode.inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        
        Invocation doesntMatter = null;
        registered.markInvocationsAsVerified(new ExpectedInvocation(doesntMatter, EMPTY_LIST), mode);
        registered.markInvocationsAsVerified(new ExpectedInvocation(doesntMatter, EMPTY_LIST), mode);
        registered.markInvocationsAsVerified(new ExpectedInvocation(doesntMatter, EMPTY_LIST), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldRemoveLastInvocation() throws Exception {
        registered.removeLast();
        assertEquals(3, registered.all().size());
    }
}
