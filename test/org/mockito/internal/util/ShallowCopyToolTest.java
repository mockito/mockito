package org.mockito.internal.util;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ShallowCopyToolTest extends TestBase {

    private ShallowCopyTool tool = new ShallowCopyTool();
    
    //TODO: inherited fields
    //TODO: if one field fails - should carry on
    static class SomeObject {
        private static int staticField = 900; 
        private int privateField = 1;
        private transient int privateTransientField = 100;
        String defaultField = "2";
        protected Object protectedField = new Object();
        public SomeOtherObject instancePublicField = new SomeOtherObject();
        final int finalField;
        public SomeObject(int finalField) {
            this.finalField = finalField;
        }
    }
    
    static class SomeOtherObject {}

    private SomeObject first = new SomeObject(100);
    private SomeObject second = mock(SomeObject.class);
    
    @Test
    public void shouldShallowCopyBasicFields() throws Exception {
        //given
        assertEquals(100, first.finalField);
        assertNotEquals(100, second.finalField);
        
        //when
        tool.copyToMock(first, second);
        
        //then
        assertEquals(100, second.finalField);
    }

    @Test
    public void shouldShallowCopyTransientPrivateFields() throws Exception {
        //given
        first.privateTransientField = 1000;
        assertNotEquals(1000, second.privateTransientField);
        
        //when
        tool.copyToMock(first, second);
        
        //then
        assertEquals(1000, second.privateTransientField);
    }
    
    @Test
    public void shouldShallowCopyLinkedListIntoMock() throws Exception {
        //given
        LinkedList from = new LinkedList();
        LinkedList to = mock(LinkedList.class);
        
        //when
        tool.copyToMock(from, to);
        
        //then no exception is thrown
    }
}