/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitoutil.TestBase;

@SuppressWarnings("all")
public class ReflectionMatchersTest extends TestBase {

    class Parent {
        private final int parentField;
        protected String protectedParentField;
        public Parent(final int parentField, final String protectedParentField) {
            this.parentField = parentField;
            this.protectedParentField = protectedParentField;
        }
    }
    
    class Child extends Parent {
        private final int childFieldOne;
        private final Object childFieldTwo;
        public Child(final int parentField, final String protectedParentField, final int childFieldOne, final Object childFieldTwo) {
            super(parentField, protectedParentField);
            this.childFieldOne = childFieldOne;
            this.childFieldTwo = childFieldTwo;
        } 
    }
    
    interface MockMe {
        void run(final Child child);
    }
    
    MockMe mock;
    
    @Before
    public void setup() {
        mock = mock(MockMe.class);
        
        final Child actual = new Child(1, "foo", 2, "bar");
        mock.run(actual);
    }
    
    @Test
    public void shouldMatchWhenFieldValuesEqual() throws Exception {
        final Child wanted = new Child(1, "foo", 2, "bar");
        verify(mock).run(refEq(wanted));
    }
    
    @Test(expected=ArgumentsAreDifferent.class)
    public void shouldNotMatchWhenFieldValuesDiffer() throws Exception {
        final Child wanted = new Child(1, "foo", 2, "bar XXX");
        verify(mock).run(refEq(wanted));
    }
    
    @Test(expected=ArgumentsAreDifferent.class)
    public void shouldNotMatchAgain() throws Exception {
        final Child wanted = new Child(1, "foo", 999, "bar");
        verify(mock).run(refEq(wanted));
    }
    
    @Test(expected=ArgumentsAreDifferent.class)
    public void shouldNotMatchYetAgain() throws Exception {
        final Child wanted = new Child(1, "XXXXX", 2, "bar");
        verify(mock).run(refEq(wanted));
    }
    
    @Test(expected=ArgumentsAreDifferent.class)
    public void shouldNotMatch() throws Exception {
        final Child wanted = new Child(234234, "foo", 2, "bar");
        verify(mock).run(refEq(wanted));
    }

    @Test
    public void shouldMatchWhenFieldValuesEqualWithOneFieldExcluded() throws Exception {
        final Child wanted = new Child(1, "foo", 2, "excluded");
        verify(mock).run(refEq(wanted, "childFieldTwo"));
    }

    @Test
    public void shouldMatchWhenFieldValuesEqualWithTwoFieldsExcluded() throws Exception {
        final Child wanted = new Child(234234, "foo", 2, "excluded");
        verify(mock).run(refEq(wanted, "childFieldTwo", "parentField"));
        verify(mock).run(refEq(wanted, "parentField", "childFieldTwo"));
    }
    
    @Test(expected=ArgumentsAreDifferent.class)
    public void shouldNotMatchWithFieldsExclusion() throws Exception {
        final Child wanted = new Child(234234, "foo", 2, "excluded");
        verify(mock).run(refEq(wanted, "childFieldTwo"));
    }    
}