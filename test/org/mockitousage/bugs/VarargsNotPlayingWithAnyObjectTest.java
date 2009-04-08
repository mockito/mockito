/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

public class VarargsNotPlayingWithAnyObjectTest extends TestBase {

    interface VarargMethod {
        Object run(String... args);
    }

    @Ignore
    @Test
    public void testVarargStubbing() {
        VarargMethod mock = Mockito.mock(VarargMethod.class);
        mock.run("a", "b");
        
        verify(mock).run((String[]) anyObject());
        verify(mock).run(new String[] {anyObject()});
        verify(mock, never()).run();
    }
}