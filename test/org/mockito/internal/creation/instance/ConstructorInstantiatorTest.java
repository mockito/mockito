package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    static class SomeClass {}
    class SomeInnerClass {}
    static class SomeClass2 {
        SomeClass2(String x) {}
    }

    @Test public void creates_instances() {
        assertEquals(new ConstructorInstantiator(null).newInstance(SomeClass.class).getClass(), SomeClass.class);
    }

    @Test public void creates_instances_of_inner_classes() {
        assertEquals(new ConstructorInstantiator(this).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
    }

    @Test public void explains_when_constructor_cannot_be_found() {
        try {
            new ConstructorInstantiator(null).newInstance(SomeClass2.class);
            fail();
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'SomeClass2'.\n" +
                    "Please ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}