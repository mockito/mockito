/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class AnyXMatchersAcceptNullsTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Ignore
    @Test
    public void shouldAnyXMatchersAcceptNull() {
        stub(mock.oneArg(anyObject())).toReturn("0");
        stub(mock.oneArg(anyString())).toReturn("1");
        stub(mock.oneArg(anyInt())).toReturn("2");
        
        assertEquals("0", mock.oneArg((Object) null));
        assertEquals("1", mock.oneArg((String) null));
        assertEquals("2", mock.oneArg((Integer) null));
    }
    
    
    
//        TODO add anyList()       matcher
//        TODO add anyMap()        matcher
//        TODO add anyCollection() matcher
//        TODO add anySet() matcher
    
}