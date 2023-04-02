package org.mockito;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThatMatch;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ArgumentMatchersTest extends TestBase {

    interface Gryffindor {
        void join(String student);
    }

    @Test
    public void shouldPassAssertion() {
        Gryffindor gryffindor = mock(Gryffindor.class);

        gryffindor.join("Harry Potter");

        verify(gryffindor).join(argThatMatch(name -> assertThat(name).isEqualTo("Harry Potter")));
    }
    @Test
    public void shouldFailAssertion() {
        Gryffindor gryffindor = mock(Gryffindor.class);

        gryffindor.join("Draco Malfoy");

        try {
            verify(gryffindor).join(argThatMatch(name -> assertThat(name).isEqualTo("Harry Potter")));
            fail();
        } catch (AssertionError e) {
            assertThat(e).hasMessageContainingAll("Harry Potter", "Draco Malfoy");
        }
    }
}
