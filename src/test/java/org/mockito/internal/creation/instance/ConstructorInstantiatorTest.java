/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    static class SomeClass3 {

        SomeClass3(int i) {

        }
    }

    @Test
    public void creates_instances() {
        assertEquals(new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClass.class).getClass(), SomeClass.class);
    }

    @Test
    public void creates_instances_of_inner_classes() {
        assertEquals(new ConstructorInstantiator(true, this).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
        assertEquals(new ConstructorInstantiator(true, new ChildOfThis()).newInstance(SomeInnerClass.class).getClass(), SomeInnerClass.class);
    }

    @Test
    public void creates_instances_with_arguments() {
        assertEquals(new ConstructorInstantiator(false, "someString").newInstance(SomeClass2.class).getClass(), SomeClass2.class);
    }

    @Test
    public void creates_instances_with_null_arguments() {
        assertEquals(new ConstructorInstantiator(false, new Object[]{null}).newInstance(SomeClass2.class).getClass(), SomeClass2.class);
    }

    @Test
    public void creates_instances_with_primitive_arguments() {
        assertEquals(new ConstructorInstantiator(false, 123).newInstance(SomeClass3.class).getClass(), SomeClass3.class);
    }

    @Test(expected = org.mockito.creation.instance.InstantiationException.class)
    public void fails_when_null_is_passed_for_a_primitive() {
        assertEquals(new ConstructorInstantiator(false, new Object[]{null}).newInstance(SomeClass3.class).getClass(), SomeClass3.class);
    }

    @Test
    public void explains_when_constructor_cannot_be_found() {
        try {
            new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClass2.class);
            fail();
        } catch (org.mockito.creation.instance.InstantiationException e) {
            assertThat(e).hasMessageContaining("Unable to create instance of 'SomeClass2'.\n" +
                    "Please ensure that the target class has a 0-arg constructor.");
        }
    }
}
