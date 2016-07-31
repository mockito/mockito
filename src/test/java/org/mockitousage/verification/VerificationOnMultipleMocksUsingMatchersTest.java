/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class VerificationOnMultipleMocksUsingMatchersTest extends TestBase {

    @Test
    public void shouldVerifyUsingMatchers() throws Exception {
        List<Object> list = Mockito.mock(List.class);
        HashMap<Object, Object> map = Mockito.mock(HashMap.class);
        
        list.add("test");
        list.add(1, "test two");
        
        map.put("test", 100);
        map.put("test two", 200);
        
        verify(list).add(anyObject());
        verify(list).add(anyInt(), eq("test two"));
        
        verify(map, times(2)).put(anyObject(), anyObject());
        verify(map).put(eq("test two"), eq(200));
        
        verifyNoMoreInteractions(list, map);
    }
    
    @Test
    public void shouldVerifyMultipleMocks() throws Exception {
        List<String> list = mock(List.class);
        Map<Object, Integer> map = mock(Map.class);
        Set<?> set = mock(Set.class);

        list.add("one");
        list.add("one");
        list.add("two");
        
        map.put("one", 1);
        map.put("one", 1);
        
        verify(list, times(2)).add("one");
        verify(list, times(1)).add("two");
        verify(list, times(0)).add("three");
        
        verify(map, times(2)).put(anyObject(), anyInt());
        
        verifyNoMoreInteractions(list, map);
        verifyZeroInteractions(set);
    }
}
