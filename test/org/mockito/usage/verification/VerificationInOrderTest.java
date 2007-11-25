/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.*;

@SuppressWarnings("unchecked")  
public class VerificationInOrderTest {
    
    private LinkedList list;
    private HashMap map;
    private HashSet set;
    private Strictly strictly;

    @Before
    public void setUp() {
        list = mock(LinkedList.class);
        map = mock(HashMap.class);
        set = mock(HashSet.class);

        list.add("one");
        map.put("two", "two");
        list.add("three and four");
        list.add("three and four");
        map.put("five", "five");
        set.add("six");
        
        strictly = strictOrderVerifier(list, map, set);
    }
    
    @Test
    public void shouldVerifyInOrder() {
        strictly.verify(list).add("one");
        strictly.verify(map).put("two", "two");
        strictly.verify(list, 2).add("three and four");
        strictly.verify(map).put("five", "five");
        strictly.verify(set).add("six");
        strictly.verifyNoMoreInteractions();
    } 

    @Test(expected = VerificationAssertionError.class)
    public void shouldFailOnOrdinaryVerificationError() {
        strictly.verify(list).add("xxx");
    }
    
    @Test(expected = NumberOfInvocationsAssertionError.class)
    public void shouldFailOnExpectedNumberOfInvocations() {
        strictly.verify(list, 2).add("xxx");
    }
    
    @Test
    public void shouldFailOnWrongOrder() {
        strictly.verify(list, 1).add("one");
        strictly.verify(map).put("two", "two");
        try {
            strictly.verify(map).put("five", "five");
            fail();
        } catch (StrictVerificationError e) {}
    }
    
    @Test
    public void shouldFailOnWrongOrderWhenCheckingExpectedNumberOfInvocations() {
        strictly.verify(list, 1).add("one");
        strictly.verify(map).put("two", "two");
        try {
            strictly.verify(map, 1).put("five", "five");
            fail();
        } catch (StrictVerificationError e) {}
    }
    
    @Test
    public void shouldFailWhenPriorVerificationCalledAgain() {
        strictly.verify(list, 1).add("one");
        strictly.verify(map).put("two", "two");
        strictly.verify(list, 2).add("three and four");
        try {
            strictly.verify(list, 1).add("one");
            fail();
        } catch (StrictVerificationError e) {}
    }
}