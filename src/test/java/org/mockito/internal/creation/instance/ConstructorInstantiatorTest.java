/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ConstructorInstantiatorTest extends TestBase {

    static class SomeClass {}

    class SomeInnerClass {}

    class ChildOfThis extends ConstructorInstantiatorTest {}

    static class SomeClass2 {

        SomeClass2(String x) {}
    }

    static class SomeClass3 {

        SomeClass3(int i) {}
    }

    @Test
    public void creates_instances() {
        assertThat(
                        new ConstructorInstantiator(false, new Object[0])
                                .newInstance(SomeClass.class)
                                .getClass())
                .isEqualTo(SomeClass.class);
    }

    @Test
    public void creates_instances_of_inner_classes() {
        assertThat(
                        new ConstructorInstantiator(true, this)
                                .newInstance(SomeInnerClass.class)
                                .getClass())
                .isEqualTo(SomeInnerClass.class);
        assertThat(
                        new ConstructorInstantiator(true, new ChildOfThis())
                                .newInstance(SomeInnerClass.class)
                                .getClass())
                .isEqualTo(SomeInnerClass.class);
    }

    @Test
    public void creates_instances_with_arguments() {
        assertThat(
                        new ConstructorInstantiator(false, "someString")
                                .newInstance(SomeClass2.class)
                                .getClass())
                .isEqualTo(SomeClass2.class);
    }

    @Test
    public void creates_instances_with_null_arguments() {
        assertThat(
                        new ConstructorInstantiator(false, new Object[] {null})
                                .newInstance(SomeClass2.class)
                                .getClass())
                .isEqualTo(SomeClass2.class);
    }

    @Test
    public void creates_instances_with_primitive_arguments() {
        assertThat(new ConstructorInstantiator(false, 123).newInstance(SomeClass3.class).getClass())
                .isEqualTo(SomeClass3.class);
    }

    @Test
    public void fails_when_null_is_passed_for_a_primitive() {
        assertThatThrownBy(
                        () -> {
                            new ConstructorInstantiator(false, new Object[] {null})
                                    .newInstance(SomeClass3.class)
                                    .getClass();
                        })
                .isInstanceOf(org.mockito.creation.instance.InstantiationException.class)
                .hasMessageContaining("Unable to create instance of 'SomeClass3'.");
    }

    @Test
    public void explains_when_constructor_cannot_be_found() {
        try {
            new ConstructorInstantiator(false, new Object[0]).newInstance(SomeClass2.class);
            fail();
        } catch (org.mockito.creation.instance.InstantiationException e) {
            assertThat(e)
                    .hasMessageContaining(
                            "Unable to create instance of 'SomeClass2'.\n"
                                    + "Please ensure that the target class has a 0-arg constructor.");
        }
    }
}
