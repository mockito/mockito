/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;


import org.junit.Test;
import org.mockito.Mockito;



import static org.mockito.Mockito.mock;

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
    public void is_valid_mocked() {
        ClassForMocking clazzMock = mock(ClassForMocking.class);
        Mockito.when(clazzMock.isValid()).thenReturn(true);
    }
}