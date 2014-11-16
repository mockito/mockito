package org.mockito.internal.creation.instance;

import org.junit.Ignore;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    static class SomeClass {}
    class SomeInnerClass {}

    @Test public void creates_instances() {
        assertEquals(new ConstructorInstantiator(null).newInstance(SomeClass.class).getClass(), SomeClass.class);
    }

    @Test public void creates_instances_of_inner_classes() {
        assertEquals(new ConstructorInstantiator(this).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
    }

    @Ignore //TODO SF
    @Test public void explains_when_constructor_cannot_be_found() {
        fail();
    }

    @Ignore //TODO SF
    @Test public void fails_with_graceful_message() {
        fail();
    }
}