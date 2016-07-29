/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockitousage.junitrunner.Filters.methodNameContains;

@RunWith(MockitoJUnitRunner.class)
public class JUnit45RunnerTest {

    @InjectMocks private ListDependent listDependent = new ListDependent();
    @Mock private List<String> list;

    @Test
    public void shouldInitMocksUsingRunner() {
        list.add("test");
        verify(list).add("test");
    }

    @Test
    public void shouldInjectMocksUsingRunner() {
        assertNotNull(list);
        assertSame(list, listDependent.getList());
    }

    @Test
    public void shouldFilterTestMethodsCorrectly() throws Exception{
        MockitoJUnitRunner runner = new MockitoJUnitRunner(this.getClass());

        runner.filter(methodNameContains("shouldInitMocksUsingRunner"));

        assertEquals(1, runner.testCount());
    }

    class ListDependent {
        private List<?> list;

        public List<?> getList() {
            return list;
        }
    }
}
