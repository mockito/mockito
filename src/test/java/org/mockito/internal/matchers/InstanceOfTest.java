package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InstanceOfTest {

    @Test
    public void should_describe_the_matcher() {
        assertThat(new InstanceOf(Object.class).toString()).contains("isA")
                                                           .contains("Object");
        assertThat(new InstanceOf(Object[].class).toString()).contains("isA")
                                                           .contains("Object[]");
        assertThat(new InstanceOf(Object.class, "matches something").toString()).isEqualTo("matches something");
    }

    @Test
    public void should_check_instance_type() {
        assertThat(new InstanceOf(Object.class).matches(new Object())).isTrue();
        assertThat(new InstanceOf(Object.class).matches(new ArrayList())).isTrue();
        assertThat(new InstanceOf(List.class).matches(new ArrayList())).isTrue();
        assertThat(new InstanceOf(List.class).matches(new Object())).isFalse();
    }

    @Test
    public void should_check_for_primitive_wrapper_types() {
        assertThat(new InstanceOf(int.class).matches(1000)).isTrue();
        assertThat(new InstanceOf(Integer.class).matches(1000)).isTrue();
        assertThat(new InstanceOf(int.class).matches(new Integer(1000))).isTrue();
        assertThat(new InstanceOf(Integer.class).matches(new Integer(1000))).isTrue();

        assertThat(new InstanceOf(double.class).matches(1000.1)).isTrue();
        assertThat(new InstanceOf(Double.class).matches(1000.1)).isTrue();
        assertThat(new InstanceOf(double.class).matches(new Double(1000.1))).isTrue();
        assertThat(new InstanceOf(Double.class).matches(new Double(1000.1))).isTrue();

        assertThat(new InstanceOf(int.class).matches(1000L)).isFalse();
        assertThat(new InstanceOf(Integer.class).matches(1000L)).isFalse();
        assertThat(new InstanceOf(int.class).matches(new Long(1000))).isFalse();
        assertThat(new InstanceOf(Integer.class).matches(new Long(1000))).isFalse();

        assertThat(new InstanceOf(long.class).matches(1000L)).isTrue();
        assertThat(new InstanceOf(Long.class).matches(1000L)).isTrue();
        assertThat(new InstanceOf(long.class).matches(new Long(1000))).isTrue();
        assertThat(new InstanceOf(Long.class).matches(new Long(1000))).isTrue();

        assertThat(new InstanceOf(long.class).matches(1000)).isFalse();
        assertThat(new InstanceOf(Long.class).matches(1000)).isFalse();
        assertThat(new InstanceOf(long.class).matches(new Integer(1000))).isFalse();
        assertThat(new InstanceOf(Long.class).matches(new Integer(1000))).isFalse();
    }

    @Test
    public void can_be_vararg_aware() {
        assertThat(new InstanceOf.VarArgAware(Number[].class)).isInstanceOf(VarargMatcher.class);
        assertThat(new InstanceOf.VarArgAware(Number[].class).matches(new Integer[0])).isTrue();
        assertThat(new InstanceOf.VarArgAware(Number[].class).matches(new Number[0])).isTrue();
        assertThat(new InstanceOf.VarArgAware(Number[].class).matches(new Object[0])).isFalse();
    }
}