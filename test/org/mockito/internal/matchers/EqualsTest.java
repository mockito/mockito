/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockitoutil.TestBase;


public class EqualsTest extends TestBase {
    
    public void shouldBeEqual() {
        assertEquals(new Equals(null), new Equals(null));
        assertEquals(new Equals(new Integer(2)), new Equals(new Integer(2)));
        assertFalse(new Equals(null).equals(null));
        assertFalse(new Equals(null).equals("Test"));
        try {
            new Equals(null).hashCode();
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }
    
    @Test
    public void shouldGiveVerboselyDescribedVersionOfInt() throws Exception {
        String descStr = describe(new Equals(100).getVerboseSelfDescribing());
        
        assertEquals("(Integer) 100", descStr);
    }

    @Test
    public void shouldGiveVerboselyDescribedVersionOfLong() throws Exception {
        String descStr = describe(new Equals(100L).getVerboseSelfDescribing());
        
        assertEquals("(Long) 100", descStr);
    }
    
    @Test
    public void shouldAppendQuotingForString() {
        String descStr = describe(new Equals("str"));
        
        assertEquals("\"str\"", descStr);
    }

    @Test
    public void shouldAppendQuotingForChar() {
        String descStr = describe(new Equals('s'));
        
        assertEquals("'s'", descStr);
    }
    
    @Test
    public void shouldDescribeUsingToString() {
        String descStr = describe(new Equals(100));
        
        assertEquals("100", descStr);
    }

    @Test
    public void shouldDescribeNull() {
        String descStr = describe(new Equals(null));
        
        assertEquals("null", descStr);
    }

    private String describe(SelfDescribing m) {
        StringDescription desc = new StringDescription();
        m.describeTo(desc);
        String descStr = desc.toString();
        return descStr;
    }
}
