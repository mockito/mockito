/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Parameterized.class)
public class MemberAccessorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[] {new ReflectionMemberAccessor()});
        data.add(new Object[] {new ModuleMemberAccessor()});
        return data;
    }

    private final MemberAccessor accessor;

    public MemberAccessorTest(MemberAccessor accessor) {
        this.accessor = accessor;
    }

    @Test
    public void test_read_field() throws Exception {
        assertThat(accessor.get(Sample.class.getDeclaredField("field"), new Sample("foo")))
                .isEqualTo("foo");
    }

    @Test
    public void test_read_static_field() throws Exception {
        Sample.staticField = "foo";
        assertThat(accessor.get(Sample.class.getDeclaredField("staticField"), null))
                .isEqualTo("foo");
    }

    @Test
    public void test_write_field() throws Exception {
        Sample sample = new Sample("foo");
        accessor.set(Sample.class.getDeclaredField("field"), sample, "bar");
        assertThat(sample.field).isEqualTo("bar");
    }

    @Test
    public void test_write_static_field() throws Exception {
        Sample.staticField = "foo";
        accessor.set(Sample.class.getDeclaredField("staticField"), null, "bar");
        assertThat(Sample.staticField).isEqualTo("bar");
    }

    @Test
    public void test_invoke() throws Exception {
        assertThat(
                        accessor.invoke(
                                Sample.class.getDeclaredMethod("test", String.class),
                                new Sample(null),
                                "foo"))
                .isEqualTo("foo");
    }

    @Test
    public void test_invoke_invocation_exception() {
        assertThatThrownBy(
                        () ->
                                accessor.invoke(
                                        Sample.class.getDeclaredMethod("test", String.class),
                                        new Sample(null),
                                        "exception"))
                .isInstanceOf(InvocationTargetException.class);
    }

    @Test
    public void test_invoke_illegal_arguments() {
        assertThatThrownBy(
                        () ->
                                accessor.invoke(
                                        Sample.class.getDeclaredMethod("test", String.class),
                                        new Sample(null),
                                        42))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test_new_instance() throws Exception {
        assertThat(accessor.newInstance(Sample.class.getDeclaredConstructor(String.class), "foo"))
                .isInstanceOf(Sample.class);
    }

    @Test
    public void test_new_instance_illegal_arguments() {
        assertThatThrownBy(
                        () ->
                                accessor.newInstance(
                                        Sample.class.getDeclaredConstructor(String.class), 42))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test_new_instance_invocation_exception() {
        assertThatThrownBy(
                        () ->
                                accessor.newInstance(
                                        Sample.class.getDeclaredConstructor(String.class),
                                        "exception"))
                .isInstanceOf(InvocationTargetException.class);
    }

    @Test
    public void test_new_instance_instantiation_exception() {
        assertThatThrownBy(
                        () -> accessor.newInstance(AbstractSample.class.getDeclaredConstructor()))
                .isInstanceOf(InstantiationException.class);
    }

    @Test
    public void test_set_final_field() throws Exception {
        Sample sample = new Sample("foo");
        accessor.set(Sample.class.getDeclaredField("finalField"), sample, "foo");
        assertThat(sample.finalField).isEqualTo("foo");
    }

    private static class Sample {

        private String field;

        private final String finalField = null;

        private static String staticField = "foo";

        public Sample(String field) {
            if ("exception".equals(field)) {
                throw new RuntimeException();
            }
            this.field = field;
        }

        private String test(String value) {
            if ("exception".equals(value)) {
                throw new RuntimeException();
            }
            return value;
        }
    }

    private abstract static class AbstractSample {}
}
