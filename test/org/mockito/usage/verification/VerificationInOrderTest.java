/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.exceptions.*;
import org.mockito.internal.StrictOrderVerifier;

@SuppressWarnings("unchecked")  
public class VerificationInOrderTest {
    
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
        strictly.verifyNoMoreInteractions();
    } 

    @Test
    public void shouldVerifyInOrderWithExactNumberOfInvocations() {
        strictly.verify(list, 1).add("one");
        strictly.verify(map).put("two", "two");
        strictly.verify(list, 2).add("three and four");
        strictly.verify(map, 1).put("five", "five");
        strictly.verify(set, 1).add("six");
        strictly.verifyNoMoreInteractions();
    }  
    
    @Test(expected = VerificationAssertionError.class)
    public void shouldFailOnOrdinaryVerificationError() {
        strictly.verify(list).add("xxx");
    }
    
    @Test(expected = NumberOfInvocationsAssertionError.class)
    public void shouldFailOnExactNumberOfInvocations() {
        strictly.verify(list, 2).add("xxx");
    }
    
    @Ignore
    @Test
    public void shouldFailOnWrongOrder() {
        strictly.verify(list, 1).add("one");
        strictly.verify(map).put("two", "two");
        try {
            strictly.verify(map, 1).put("five", "five");
            fail();
        } catch (StrictVerificationError e) {
//            String expected = "\n" +
//                    "Expected next invocation:" +
//                    "\n" +
//                    "HashMap.put(\"five\", \"five\")" +
//                    "\n" +
//                  "Actual next invocation:" +
//                  "\n" +
//                  "LinkedList.add(\"three and four\")" +
//                  "\n";
//            assertEquals(expected, e.getMessage());
        }
    }
}