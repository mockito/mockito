/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HashCodeAndEqualsSafeSetTest {

    @Test
    public void can_add_mock_that_have_failing_hashCode_method() throws Exception {
        new HashCodeAndEqualsSafeSet().add(mock(UnmockableHashCodeAndEquals.class));
    }

    @Test
    public void mock_with_failing_hashCode_method_can_be_added() throws Exception {
        new HashCodeAndEqualsSafeSet().add(mock(UnmockableHashCodeAndEquals.class));
    }

    @Test
    public void mock_with_failing_equals_method_can_be_used() throws Exception {
        HashCodeAndEqualsSafeSet mocks = new HashCodeAndEqualsSafeSet();
        UnmockableHashCodeAndEquals mock = mock(UnmockableHashCodeAndEquals.class);
        mocks.add(mock);

        assertThat(mocks.contains(mock)).isTrue();
        assertThat(mocks.contains(mock(UnmockableHashCodeAndEquals.class))).isFalse();
    }

    @Test
    public void can_remove() throws Exception {
        HashCodeAndEqualsSafeSet mocks = new HashCodeAndEqualsSafeSet();
        UnmockableHashCodeAndEquals mock = mock(UnmockableHashCodeAndEquals.class);
        mocks.add(mock);
        mocks.remove(mock);

        assertThat(mocks.isEmpty()).isTrue();
    }


    @Test
    public void can_add_a_collection() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock(UnmockableHashCodeAndEquals.class),
                mock(Observer.class));

        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(mocks);

        assertThat(workingSet.containsAll(mocks)).isTrue();
    }

    @Test
    public void can_retain_a_collection() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock(UnmockableHashCodeAndEquals.class),
                mock(Observer.class));

        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(mocks);
        workingSet.add(mock(List.class));

        assertThat(workingSet.retainAll(mocks)).isTrue();
        assertThat(workingSet.containsAll(mocks)).isTrue();
    }

    @Test
    public void can_remove_a_collection() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock(UnmockableHashCodeAndEquals.class),
                mock(Observer.class));

        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(mocks);
        workingSet.add(mock(List.class));

        assertThat(workingSet.removeAll(mocks)).isTrue();
        assertThat(workingSet.containsAll(mocks)).isFalse();
    }

    @Test
    public void can_iterate() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock(UnmockableHashCodeAndEquals.class),
                mock(Observer.class));

        LinkedList<Object> accumulator = new LinkedList<Object>();
        for (Object mock : mocks) {
            accumulator.add(mock);
        }
        assertThat(accumulator).isNotEmpty();
    }

    @Test
    public void toArray_just_work() throws Exception {
        UnmockableHashCodeAndEquals mock1 = mock(UnmockableHashCodeAndEquals.class);
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(mock1);

        assertThat(mocks.toArray()[0]).isSameAs(mock1);

        assertThat(mocks.toArray(new UnmockableHashCodeAndEquals[0])[0]).isSameAs(mock1);
    }

    private static class UnmockableHashCodeAndEquals {
        @Override public final int hashCode() {
            throw new NullPointerException("I'm failing on hashCode and I don't care");
        }

        @Override public final boolean equals(Object obj) {
            throw new NullPointerException("I'm failing on equals and I don't care");
        }
    }
}
