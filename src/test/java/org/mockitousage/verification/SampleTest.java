package org.mockitousage.verification;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

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

        // This is certainly Reporter.incorrectUseOfApi(), but helps for reproducing the issue
        // Can occur in unit test which uses mocks in spawned threads
        OngoingStubbing ongoingStubbing = Mockito.when(foo.bar("DOG"));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Before providing thenReturn - foo.bar(\"DOG\"): " + foo.bar("DOG"));
                System.out.println("Before providing thenReturn - foo.bar(\"BUG\"): " + foo.bar("BUG"));
            }
        });
        t.start();
        t.join();
        // Because "BUG" is the recent invocation, stubbing happens as foo.bar("BUG") returning "PUG"
        ongoingStubbing.thenReturn("PUG");
        System.out.println("After providing thenReturn - foo.bar(\"DOG\"): " + foo.bar("DOG"));
        System.out.println("After providing thenReturn - foo.bar(\"BUG\"): " + foo.bar("BUG"));

        assertEquals("PUG", foo.bar("DOG"));
    }

    public static class Foo {

        public String bar(String arg) {
            return "REAL METHOD";
        }
    }
}
