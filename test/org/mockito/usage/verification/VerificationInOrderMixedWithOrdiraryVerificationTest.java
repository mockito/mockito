/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.internal.StrictOrderVerifier;

@SuppressWarnings("unchecked")  
public class VerificationInOrderMixedWithOrdiraryVerificationTest {
    
    private LinkedList list;
    private HashMap map;
    private HashSet set;
    private StrictOrderVerifier strictly;

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
        strictly.verify(list).add("three and four");
        strictly.verify(map).put("five", "five");
        strictly.verify(set).add("six");
    } 
    
    //TODO add some more testing about it... what about a case when verifier gets different mock (not strict)?
    
}