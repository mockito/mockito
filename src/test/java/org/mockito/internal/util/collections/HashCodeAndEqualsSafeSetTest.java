/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class HashCodeAndEqualsSafeSetTest {

    @Rule
    public MockitoRule r = MockitoJUnit.rule();
    @Mock
    private UnmockableHashCodeAndEquals mock1;

    @Test
    public void can_add_mock_that_have_failing_hashCode_method() throws Exception {
        new HashCodeAndEqualsSafeSet().add(mock1);
    }

    @Test
    public void mock_with_failing_hashCode_method_can_be_added() throws Exception {
        new HashCodeAndEqualsSafeSet().add(mock1);
    }

    @Test
    public void mock_with_failing_equals_method_can_be_used() throws Exception {
        HashCodeAndEqualsSafeSet mocks = new HashCodeAndEqualsSafeSet();
        mocks.add(mock1);

        assertThat(mocks.contains(mock1)).isTrue();

        UnmockableHashCodeAndEquals mock2 = mock(UnmockableHashCodeAndEquals.class);
        assertThat(mocks.contains(mock2)).isFalse();
    }

    @Test
    public void can_remove() throws Exception {
        HashCodeAndEqualsSafeSet mocks = new HashCodeAndEqualsSafeSet();
        UnmockableHashCodeAndEquals mock = mock1;
        mocks.add(mock);
        mocks.remove(mock);

        assertThat(mocks.isEmpty()).isTrue();
    }


    @Test
    public void can_add_a_collection() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock1,
                mock(Observer.class));

        HashCodeAndEqualsSafeSet workingSet = new HashCodeAndEqualsSafeSet();

        workingSet.addAll(mocks);

        assertThat(workingSet.containsAll(mocks)).isTrue();
    }

    @Test
    public void can_retain_a_collection() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(
                mock1,
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
                mock1,
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
                mock1,
                mock(Observer.class));

        LinkedList<Object> accumulator = new LinkedList<Object>();
        for (Object mock : mocks) {
            accumulator.add(mock);
        }
        assertThat(accumulator).isNotEmpty();
    }

    @Test
    public void toArray_just_work() throws Exception {
        HashCodeAndEqualsSafeSet mocks = HashCodeAndEqualsSafeSet.of(mock1);

        assertThat(mocks.toArray()[0]).isSameAs(mock1);

        assertThat(mocks.toArray(new UnmockableHashCodeAndEquals[0])[0]).isSameAs(mock1);
    }
    
    @Test(expected=CloneNotSupportedException.class)    
    public void cloneIsNotSupported() throws CloneNotSupportedException{
        HashCodeAndEqualsSafeSet.of().clone();
    }
    
    @Test
    public void isEmptyAfterClear() throws Exception {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        set.clear();
        
        assertThat(set).isEmpty();
    }
    
    @Test
    public void isEqualToItself(){
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set).isEqualTo(set);
    }
    
    @Test
    public void isNotEqualToAnOtherTypeOfSetWithSameContent(){
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of();
        assertThat(set).isNotEqualTo(new HashSet<Object>());
    }
    
    @Test
    public void isNotEqualWhenContentIsDifferent(){
       
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set).isNotEqualTo(HashCodeAndEqualsSafeSet.of());
    }
    
    @Test
    public void hashCodeIsEqualIfContentIsEqual(){
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set.hashCode()).isEqualTo(HashCodeAndEqualsSafeSet.of(mock1).hashCode());
    }
    
    @Test
    public void toStringIsNotNullOrEmpty() throws Exception {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        assertThat(set.toString()).isNotEmpty();
    }
    
    @Test
    public void removeByIterator() throws Exception {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(mock1);
        Iterator<Object> iterator = set.iterator();
        iterator.next();
        iterator.remove();
        
        assertThat(set).isEmpty();
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