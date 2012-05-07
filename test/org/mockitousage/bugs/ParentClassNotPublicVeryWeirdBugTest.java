/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

//see bug 212
// @Ignore("needs fixing")
public class ParentClassNotPublicVeryWeirdBugTest {
    
    class SuperClass {
        public boolean isValid() {
            System.out.println(" cool: " + System.currentTimeMillis());
          return false;
        }
      }
    
    public class ClassForMocking extends SuperClass {
    }
    
    @Test
    @Ignore("needs fixing")
    public void is_valid_mocked() {
        ClassForMocking clazzMock = mock(ClassForMocking.class);
        Mockito.when(clazzMock.isValid()).thenReturn(true);
    }

    @Test
    public void report_why_this_exception_happen() throws Exception {
        ClassForMocking clazzMock = mock(ClassForMocking.class);
        try {
            Mockito.when(clazzMock.isValid()).thenReturn(true);
            fail();
        } catch (MissingMethodInvocationException e) {
            assertThat(e.getMessage())
                    .contains("the parent of the mocked class is not public.")
                    .contains("It is a limitation of the mock engine");
        }
    }
}