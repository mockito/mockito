/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.junit.Test;
import org.mockito.internal.runners.RunnerImpl;
import org.mockitoutil.TestBase;


public class RunnerProviderTest extends TestBase {
    
    @Test
    public void shouldCreateRunnerInstance() throws Throwable {
        //given
        RunnerProvider provider = new RunnerProvider();
        //when
        RunnerImpl runner = provider.newInstance("org.mockito.internal.runners.JUnit45AndHigherRunnerImpl", this.getClass());
        //then
        assertNotNull(runner);
    }
}