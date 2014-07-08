/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockitoutil.TestBase;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//see bug 212
// @Ignore("needs fixing")
public class ParentClassNotPublicVeryWeirdBugTest extends TestBase {
    
    class SuperClass {
        public boolean isValid() {
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

    //this code here (let's call it LEO) is only needed to reproduce the issue with concurrency. Start:
    class Foo {
        int blah(String a, String b, Object ... c) {
            return 1;
        }
    }

    @Test
    public void leo() throws Exception {
        Foo foo = mock(Foo.class);

        when(foo.blah(anyString(), anyString())).thenCallRealMethod();

        assertEquals(1, foo.blah("foo", "bar"));
    }
    //end of the code that should not be included in this test.

    @Test
    @Ignore
    //This test fails consistently when leo runs before. Find out why, write proper test, get rid of leo
    //@Before method in the TestBase should clear all the dodgy state after leo so it's intriguing why it does not work
    public void report_why_this_exception_happen() throws Exception {
        ClassForMocking clazzMock = mock(ClassForMocking.class);
        try {
            Mockito.when(clazzMock.isValid()).thenReturn(true);
            fail();
        } catch (MissingMethodInvocationException e) {
            Assertions.assertThat(e.getMessage())
                    .contains("the parent of the mocked class is not public.")
                    .contains("It is a limitation of the mock engine");
        }
    }
}