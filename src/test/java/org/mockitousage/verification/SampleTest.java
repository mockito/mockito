package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SampleTest extends TestBase {

    @Test
    public void testDefaultResponse() throws Exception {
        final String defaultAnswer = "DEFAULT ANSWER";
        final Foo foo = Mockito.mock(Foo.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return defaultAnswer;
            }
        });
        Thread checker = new Thread() {
            @Override
            public void run() {
                assertTrue(foo.bar("BUG").contains(defaultAnswer));
            }
        };
        checker.start();

        Mockito.when(foo.bar("DOG")).thenReturn("PUG");
        if (foo.bar("DOG").equals(defaultAnswer)) {
            System.out.println("something went wrong");
        }
        assertEquals("PUG", foo.bar("DOG"));
    }

    public static class Foo {

        public String bar(String arg) {
            return "REAL METHOD";
        }
    }
}
