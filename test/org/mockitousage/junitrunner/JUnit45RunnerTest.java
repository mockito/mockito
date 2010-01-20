/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.mockito.Mockito.verify;
import static org.mockitousage.junitrunner.Filters.methodNameContains;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class JUnit45RunnerTest extends TestBase {
    
    @Mock private List list;
    
    @Test
    public void shouldInitMocksUsingRunner() {
        list.add("test");
        verify(list).add("test");
    }
    
    @Test
    public void shouldFilterTestMethodsCorrectly() throws Exception{
    	MockitoJUnitRunner runner = new MockitoJUnitRunner(this.getClass());
    	
    	runner.filter(methodNameContains("shouldInitMocksUsingRunner"));
    	
    	assertEquals(1, runner.testCount());
    }
}
