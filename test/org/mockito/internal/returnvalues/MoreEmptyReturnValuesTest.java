package org.mockito.internal.returnvalues;

import org.junit.Ignore;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class MoreEmptyReturnValuesTest extends TestBase {

    private MoreEmptyReturnValues rv = new MoreEmptyReturnValues();

    @Ignore
    @Test
    public void shouldReturnEmptyArray() {
        String[] ret = (String[]) rv.returnValueFor((new String[0]).getClass());
        ret.getClass().isArray();
        assertTrue(ret.length == 0);
    }
    
    @Test
    public void shouldReturnEmptyString() {
        assertEquals("", rv.returnValueFor(String.class));
    }

    @Test
    public void shouldReturnObjectInstance() {
        assertNotNull(rv.returnValueFor(Object.class));
    }
}