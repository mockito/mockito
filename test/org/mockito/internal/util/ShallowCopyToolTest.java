package org.mockito.internal.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class ShallowCopyToolTest extends TestBase {

    private ShallowCopyTool tool = new ShallowCopyTool();
    
    //TODO: inherited fields
    class SomeObject {
        private int fieldOne = 1;
        String fieldTwo = "2";
        protected Object fieldThree = new Object();
        public SomeOtherObject fieldFour = new SomeOtherObject();
        final int fieldFive;
        public SomeObject(int fieldFiveValue) {
            this.fieldFive = fieldFiveValue;
        }
    }
    
    class SomeOtherObject {}

    @Test
    public void shouldShallowCopy() throws Exception {
        //given
        SomeObject first = new SomeObject(100);
        SomeObject second = new SomeObject(200);
        assertEquals(200, second.fieldFive);
        
        //when
        tool.copy(first, second);
        
        //then
        assertEquals(100, second.fieldFive);
    }
}