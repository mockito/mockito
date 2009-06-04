/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitoutil.TestBase;

//see issue 
public class EqWithIntsDoesntCopeWithLongsTest extends TestBase {

    class Boo {
        public void withLong(long y) {
        }
    }
    
    @Test
    public void shouldNotReportArgumentTypesWhenToStringIsTheSame() throws Exception {
        //given
        Boo boo = mock(Boo.class);
        boo.withLong(100);
        
        try {
            //when
            verify(boo).withLong(eq(100));
            fail();
        } catch (ArgumentsAreDifferent e) {
            //then
            assertContains("withLong((Integer) 100);", e.getMessage());
            assertContains("withLong((Long) 100);", e.getMessage());
        }
    }
}