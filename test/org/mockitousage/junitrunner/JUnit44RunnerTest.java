/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockitousage.junitrunner.Filters.methodNameContains;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;

@RunWith(MockitoJUnit44Runner.class)
@SuppressWarnings( { "unchecked", "deprecation", "rawtypes" })
public class JUnit44RunnerTest {

    @InjectMocks
    private final ListDependent listDependent = new ListDependent();

    @Mock
    private List list;

    @Test
    public void shouldInitMocksUsingRunner() {
        list.add("test");
        verify(list).add("test");
    }
    @Test
    public void shouldInjectMocksUsingRunner() {
        assertSame(list, listDependent.getList());
    }

    @Test
    public void shouldFilterTestMethodsCorrectly() throws Exception{
        final MockitoJUnit44Runner runner = new MockitoJUnit44Runner(this.getClass());

        runner.filter(methodNameContains("shouldInitMocksUsingRunner"));

        assertEquals(1, runner.testCount());
    }

    class ListDependent {
        private List list;

        public List getList() {
            return list;
        }
    }
}