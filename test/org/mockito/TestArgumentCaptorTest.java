package org.mockito;

import org.junit.Test;
import org.mockitoutil.TestBase;
import static org.mockito.ArgumentCaptor.*;


public class TestArgumentCaptorTest extends TestBase {
    
    @Test
    public void shouldReturnDummyValueThatDoesntCauseNPE() throws Exception {
        assertNotNull(forClass(Boolean.class).capture());
        assertNotNull(forClass(Character.class).capture());
        assertNotNull(forClass(Byte.class).capture());
        assertNotNull(forClass(Short.class).capture());
        assertNotNull(forClass(Integer.class).capture());
        assertNotNull(forClass(Long.class).capture());
        assertNotNull(forClass(Float.class).capture());
        assertNotNull(forClass(Double.class).capture());
    }
}