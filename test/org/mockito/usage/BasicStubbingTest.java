package org.mockito.usage;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.util.JUnitMatchers.contains;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

@SuppressWarnings("unchecked")
public class BasicStubbingTest {

    private interface DummyInterface {
        int getInt(String value);
        String getString(int argumentOne, String argumentTwo);
        List<String> getList();
    }
    
    @Test
    public void shouldStubAllMethodsByDefault() throws Exception {
        DummyInterface mock = Mockito.mock(DummyInterface.class);

        assertEquals(0, mock.getInt("test"));
        assertEquals(0, mock.getInt("testTwo"));
        
        assertNull(mock.getString(0, null));
        assertNull(mock.getString(100, null));
        
        assertEquals(0, mock.getList().size());
        assertEquals(0, mock.getList().size());
    }
    
    @Test
    public void shouldStubAndLetBeCalledAnyTimes() throws Exception {
        DummyInterface mock = Mockito.mock(DummyInterface.class);
        
        Mockito.stub(mock.getInt("14")).andReturn(14);
        
        assertThat(mock.getInt("14"), equalTo(14));
        assertThat(mock.getInt("14"), equalTo(14));
        
        Mockito.stub(mock.getList()).andReturn(Arrays.asList("elementOne", "elementTwo"));
        
        assertThat(mock.getList(), contains("elementOne", "elementTwo"));
        assertThat(mock.getList(), contains("elementOne", "elementTwo"));
        
        Mockito.stub(mock.getString(10, "test")).andReturn("test");
        
        assertThat(mock.getString(10, "test"), equalTo("test"));
        assertThat(mock.getString(10, "test"), equalTo("test"));
    }
}
