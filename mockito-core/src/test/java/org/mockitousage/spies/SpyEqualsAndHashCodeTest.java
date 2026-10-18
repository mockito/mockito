/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockitoutil.TestBase;

public class SpyEqualsAndHashCodeTest extends TestBase {

    @Test
    public void spy_uses_real_equals_and_hash_code() {
        ValueHolder real = new ValueHolder("value");
        ValueHolder spy = spy(real);

        assertThat(spy.equals(real)).isTrue();
        assertThat(real.equals(spy)).isTrue();
        assertThat(spy.equals(new ValueHolder("other"))).isFalse();
        assertThat(spy.hashCode()).isEqualTo(real.hashCode());
    }

    @Test
    public void spy_created_from_class_uses_real_equals_and_hash_code() {
        ValueHolder spy = spy(ValueHolder.class);

        assertThat(spy.equals(new ValueHolder(ValueHolder.DEFAULT))).isTrue();
        assertThat(spy.hashCode()).isEqualTo(new ValueHolder(ValueHolder.DEFAULT).hashCode());
    }

    @Test
    public void spy_can_be_looked_up_by_an_equal_object_in_hash_based_collections() {
        ValueHolder spy = spy(new ValueHolder("value"));

        Set<ValueHolder> set = new HashSet<>();
        set.add(spy);
        Map<ValueHolder, String> map = new HashMap<>();
        map.put(spy, "found");

        assertThat(set.contains(new ValueHolder("value"))).isTrue();
        assertThat(map.get(new ValueHolder("value"))).isEqualTo("found");
    }

    @Test
    public void calls_to_equals_and_hash_code_on_a_spy_are_not_recorded() {
        ValueHolder spy = spy(new ValueHolder("value"));

        assertThat(spy.hashCode()).isEqualTo(Objects.hashCode("value"));
        assertThat(spy.equals(new ValueHolder("value"))).isTrue();

        verifyNoInteractions(spy);
    }

    @Test
    public void mock_keeps_identity_based_equals_and_hash_code() {
        ValueHolder mock = mock(ValueHolder.class);
        ValueHolder otherMock = mock(ValueHolder.class);

        assertThat(mock.equals(mock)).isTrue();
        assertThat(mock.equals(otherMock)).isFalse();
        assertThat(mock.hashCode()).isEqualTo(System.identityHashCode(mock));
    }

    @Test
    public void mockito_does_not_rely_on_equals_or_hash_code_of_a_spy() {
        Hostile spy = spy(new Hostile());

        when(spy.name()).thenReturn("stubbed");
        assertThat(spy.name()).isEqualTo("stubbed");

        verify(spy).name();
        InOrder inOrder = inOrder(spy);
        inOrder.verify(spy).name();
        verifyNoMoreInteractions(spy);
        assertThat(mockingDetails(spy).isSpy()).isTrue();

        reset(spy);
        assertThat(spy.name()).isEqualTo("real");
    }

    public static class ValueHolder {

        static final String DEFAULT = "default";

        private final String value;

        public ValueHolder() {
            this(DEFAULT);
        }

        public ValueHolder(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ValueHolder)) {
                return false;
            }
            return Objects.equals(value, ((ValueHolder) other).value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }

    public static class Hostile {

        public String name() {
            return "real";
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("equals must not be called by Mockito");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("hashCode must not be called by Mockito");
        }
    }
}
