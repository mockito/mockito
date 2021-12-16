/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyAndSetterInjectionTest {

    @Mock private Observer whatever;
    @Mock private Observer whichever;

    public DefaultConstructor defaultConstructor;
    public NoArgsConstructor noArgsConstructor;
    public OneArgConstructor oneArgConstructor;
    public VarargConstructor varargConstructor;
    public ThrowingConstructor throwingConstructor;
    public ThrowingSetter throwingSetter;

    protected PropertyAndSetterInjection underTest;

    @Before
    public void setup() {
        underTest = new PropertyAndSetterInjection();
    }

    @Test
    public void should_instantiate_default_constructor() throws Exception {
        boolean result =
                underTest.process(field("defaultConstructor"), this, newSetOf(whatever, whichever));

        assertThat(result).isTrue();
        assertThat(defaultConstructor).isNotNull();
    }

    @Test
    public void should_instantiate_no_args_constructor() throws Exception {
        boolean result = underTest.process(field("noArgsConstructor"), this, newSetOf(whatever));

        assertThat(result).isTrue();
        assertThat(noArgsConstructor).isNotNull();
    }

    @Test
    public void should_instantiate_but_not_initialize_fields() throws Exception {
        boolean result = underTest.process(field("defaultConstructor"), this, new HashSet<>());

        assertThat(result).isFalse();
        assertThat(defaultConstructor).isNotNull();
        assertThat(defaultConstructor.whatever).isNull();
        assertThat(defaultConstructor.other).isNull();
    }

    @Test
    public void should_fail_to_instantiate_one_arg_constructor() throws Exception {
        try {
            underTest.process(field("oneArgConstructor"), this, newSetOf(whatever));
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("Cannot instantiate")
                    .contains("oneArgConstructor")
                    .contains("OneArgConstructor")
                    .contains("has no default constructor");
        }
        assertThat(throwingConstructor).isNull();
    }

    @Test
    public void should_fail_to_instantiate_vararg_constructor() throws Exception {
        try {
            underTest.process(field("varargConstructor"), this, newSetOf(whatever));
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("Cannot instantiate")
                    .contains("varargConstructor")
                    .contains("VarargConstructor")
                    .contains("has no default constructor");
        }
        assertThat(throwingConstructor).isNull();
    }

    @Test
    public void should_fail_to_instantiate_throwing_constructor() throws Exception {
        try {
            underTest.process(field("throwingConstructor"), this, newSetOf(whatever));
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("Cannot instantiate")
                    .contains("throwingConstructor")
                    .contains("ThrowingConstructor")
                    .contains("business logic failed");
        }
        assertThat(throwingConstructor).isNull();
    }

    @Test
    public void should_initialize_fields_by_setter() throws Exception {
        boolean result = underTest.process(field("defaultConstructor"), this, newSetOf(whatever));

        assertThat(result).isTrue();
        assertThat(defaultConstructor).isNotNull();
        assertThat(defaultConstructor.whatever).isEqualTo(whatever);
        assertThat(defaultConstructor.other).isEqualTo(whatever);
    }

    @Test
    public void should_initialize_fields_by_property() throws Exception {
        boolean result =
                underTest.process(field("noArgsConstructor"), this, newSetOf(whatever, whichever));

        assertThat(result).isTrue();
        assertThat(noArgsConstructor).isNotNull();
        assertThat(noArgsConstructor.whatever).isEqualTo(whatever);
        assertThat(noArgsConstructor.whichever).isEqualTo(whichever);
    }

    @Test
    public void should_fail_to_initialize_fields_by_throwing_setter() throws Exception {
        try {
            underTest.process(field("throwingSetter"), this, newSetOf(whatever));
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("couldn't inject mock")
                    .contains("whatever")
                    .contains("Observer")
                    .contains("business logic failed");
        }
        assertThat(throwingSetter).isNotNull();
        assertThat(throwingSetter.whatever).isNull();
    }

    private Set<Object> newSetOf(Object... mocks) {
        return new HashSet<>(Arrays.asList(mocks));
    }

    protected Field field(String fieldName) throws NoSuchFieldException {
        return this.getClass().getField(fieldName);
    }

    private static class NoArgsConstructor {
        private Observer whatever;
        private Observer whichever;

        NoArgsConstructor() {}
    }

    public static class DefaultConstructor {
        private Observer whatever;
        private Observer other;

        public void setWhatever(Observer whatever) {
            this.whatever = whatever;
            this.other = whatever;
        }
    }

    static class OneArgConstructor {
        public OneArgConstructor(String whatever) {}
    }

    static class VarargConstructor {
        VarargConstructor(String... whatever) {}
    }

    static class ThrowingConstructor {
        ThrowingConstructor() {
            throw new NullPointerException("business logic failed");
        }
    }

    static class ThrowingSetter {
        private Observer whatever;

        public void setWhatever(Observer whatever) {
            throw new NullPointerException("business logic failed");
        }
    }
}
