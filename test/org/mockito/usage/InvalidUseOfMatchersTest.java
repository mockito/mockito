/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IMethods;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.InvalidUseOfMatchersException;

@SuppressWarnings("unchecked")
public class InvalidUseOfMatchersTest {
    
    private IMethods mock;
    
    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Ignore
    @Test
    public void shouldDetectWrongNumberOfMatchersWhenStubbing() {
        Mockito.stub(mock.threeArgumentMethod(1, "2", "3")).andReturn(null);
        try {
            Mockito.stub(mock.threeArgumentMethod(1, eq("2"), "3")).andReturn(null);
            fail();
        } catch (InvalidUseOfMatchersException e) {
        }
    }
    
    @Ignore
    @Test
    public void shouldDetectStupidUseOfMatchersWhenVerifying() {
        mock.oneArg(true);
        eq("that's the stupid way");
        eq("of using matchers");
        try {
            Mockito.verify(mock).oneArg(true);
            fail();
        } catch (InvalidUseOfMatchersException e) {
        }
    }

    @Test
    public void shouldScreamWhenMatchersAreInvalid() {
        mock.simpleMethodWithArgument(not(eq("asd")));
        try {
            mock.simpleMethodWithArgument(not("jkl"));
            fail();
        } catch (IllegalStateException e) {
            assertEquals("no matchers found.", e.getMessage());
        }
        
        try {
            mock.simpleMethodWithArgument(or(eq("jkl"), "asd"));
            fail();
        } catch (IllegalStateException e) {
            assertEquals("2 matchers expected, 1 recorded.", e.getMessage());
        }
        
        try {
            mock.threeArgumentMethod(1, "asd", eq("asd"));
            fail();
        } catch (IllegalStateException e) {
            assertEquals("3 matchers expected, 1 recorded.", e.getMessage());
        }
    }
}