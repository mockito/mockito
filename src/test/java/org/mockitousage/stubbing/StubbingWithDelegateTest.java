/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class StubbingWithDelegateTest {
    public class FakeList<T> {
        private T value;

        public T get(int i) {
            return value;
        }

        public T set(int i, T value) {
            this.value = value;
            return value;
        }

        public int size() {
            return 10;
        }

        public ArrayList<T> subList(int fromIndex, int toIndex) {
            return new ArrayList<T>();
        }
    }

    public class FakeListWithWrongMethods<T> {
        public double size() {
            return 10;
        }

        public Collection<T> subList(int fromIndex, int toIndex) {
            return new ArrayList<T>();
        }
    }

    @Test
    public void when_not_stubbed_delegate_should_be_called() {
        List<String> delegatedList = new ArrayList<String>();
        delegatedList.add("un");

        List<String> mock = mock(List.class, delegatesTo(delegatedList));

        mock.add("two");

        assertEquals(2, mock.size());
    }

    @Test
    public void when_stubbed_the_delegate_should_not_be_called() {
        List<String> delegatedList = new ArrayList<String>();
        delegatedList.add("un");
        List<String> mock = mock(List.class, delegatesTo(delegatedList));

        doReturn(10).when(mock).size();

        mock.add("two");

        assertEquals(10, mock.size());
        assertEquals(2, delegatedList.size());
    }

    @Test
    public void delegate_should_not_be_called_when_stubbed2() {
        List<String> delegatedList = new ArrayList<String>();
        delegatedList.add("un");
        List<String> mockedList = mock(List.class, delegatesTo(delegatedList));

        doReturn(false).when(mockedList).add(Mockito.anyString());

        mockedList.add("two");

        assertEquals(1, mockedList.size());
        assertEquals(1, delegatedList.size());
    }

    @Test
    public void null_wrapper_dont_throw_exception_from_org_mockito_package() throws Exception {
        IMethods methods = mock(IMethods.class, delegatesTo(new MethodsImpl()));

        try {
            byte b = methods.byteObjectReturningMethod(); // real method returns null
            fail();
        } catch (Exception e) {
            assertThat(e.toString()).doesNotContain("org.mockito");
        }
    }

    @Test
    public void instance_of_different_class_can_be_called() {
        List<String> mock = mock(List.class, delegatesTo(new FakeList<String>()));

        mock.set(1, "1");
        assertThat(mock.get(1).equals("1"));
    }

    @Test
    public void method_with_subtype_return_can_be_called() {
        List<String> mock = mock(List.class, delegatesTo(new FakeList<String>()));

        List<String> subList = mock.subList(0, 0);
        assertThat(subList.isEmpty());
    }

    @Test
    public void calling_missing_method_should_throw_exception() {
        List<String> mock = mock(List.class, delegatesTo(new FakeList<String>()));

        try {
            mock.isEmpty();
            fail();
        } catch (MockitoException e) {
            assertThat(e.toString()).contains("Methods called on mock must exist");
        }
    }

    @Test
    public void calling_method_with_wrong_primitive_return_should_throw_exception() {
        List<String> mock = mock(List.class, delegatesTo(new FakeListWithWrongMethods<String>()));

        try {
            mock.size();
            fail();
        } catch (MockitoException e) {
            assertThat(e.toString()).contains("Methods called on delegated instance must have compatible return type");
        }
    }

    @Test
    public void calling_method_with_wrong_reference_return_should_throw_exception() {
        List<String> mock = mock(List.class, delegatesTo(new FakeListWithWrongMethods<String>()));

        try {
            mock.subList(0, 0);
            fail();
        } catch (MockitoException e) {
            assertThat(e.toString()).contains("Methods called on delegated instance must have compatible return type");
        }
    }

    @Test
    public void exception_should_be_propagated_from_delegate() throws Exception {
        final RuntimeException failure = new RuntimeException("angry-method");
        IMethods methods = mock(IMethods.class, delegatesTo(new MethodsImpl() {
            @Override
            public String simpleMethod() {
                throw failure;
            }
        }));

        try {
            methods.simpleMethod(); // delegate throws an exception
            fail();
        } catch (RuntimeException e) {
            assertThat(e).isEqualTo(failure);
        }
    }
}
