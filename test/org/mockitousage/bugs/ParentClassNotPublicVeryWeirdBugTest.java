/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

//see bug 212
@Ignore //needs fixing
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
    public void isValidMocked() {
        ClassForMocking clazzMock = mock(ClassForMocking.class);
        Mockito.when(clazzMock.isValid()).thenReturn(true);
    }
}