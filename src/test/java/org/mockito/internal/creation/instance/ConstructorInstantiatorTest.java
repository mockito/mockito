package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ConstructorInstantiatorTest extends TestBase {

    static class SomeClass {

    }

    class SomeInnerClass {

    }

    class ChildOfThis extends ConstructorInstantiatorTest {

    }

    static class SomeClass2 {

        SomeClass2(String x) {
        }
    }

    @Test
    public void creates_instances() {
        assertEquals(new ConstructorInstantiator(null).newInstance(SomeClass.class).getClass(), SomeClass.class);
    }

    @Test
    public void creates_instances_of_inner_classes() {
        assertEquals(new ConstructorInstantiator(this).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
        assertEquals(new ConstructorInstantiator(new ChildOfThis()).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
    }

    @Test
    public void explains_when_constructor_cannot_be_found() {
        try {
            new ConstructorInstantiator(null).newInstance(SomeClass2.class);
            fail();
        } catch (InstantiationException e) {
            assertThat(e).hasMessageContaining("Unable to create instance of 'SomeClass2'.\n" +
                    "Please ensure it has 0-arg constructor which invokes cleanly.");
        }
    }
}
