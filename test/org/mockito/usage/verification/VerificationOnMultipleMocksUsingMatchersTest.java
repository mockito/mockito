package org.mockito.usage.verification;

import static org.mockito.Mockito.*;

import java.util.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class VerificationOnMultipleMocksUsingMatchersTest {

    @Test
    public void shouldVerifyUsingMatchers() throws Exception {
        List list = Mockito.mock(List.class);
        HashMap map = Mockito.mock(HashMap.class);
        
        list.add("test");
        list.add(1, "test two");
        
        map.put("test", 100);
        map.put("test two", 200);
        
        verify(list).add(EasyMock.anyObject());
        verify(list).add(EasyMock.anyInt(), EasyMock.eq("test two"));
        
        verify(map).put(EasyMock.anyObject(), EasyMock.anyObject());
        verify(map).put(EasyMock.eq("test two"), EasyMock.eq(200));
        
        verifyNoMoreInteractions(list, map);
    }
    
    @Test
    public void shouldVerifyMultipleMocks() throws Exception {
        List list = mock(List.class);
        Map map = mock(Map.class);
        Set set = mock(Set.class);

        list.add("one");
        list.add("one");
        list.add("two");
        
        map.put("one", 1);
        map.put("one", 1);
        
        verify(list, 2).add("one");
        verify(list, 1).add("two");
        verify(list, 0).add("three");
        
        verify(map, 2).put(EasyMock.anyObject(), EasyMock.anyInt());
        
        verifyNoMoreInteractions(list, map);
        verifyZeroInteractions(set);
    }
}
