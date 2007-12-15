/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.CrazyMatchers;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.StateResetter;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class InvalidUseOfMatchersTest extends RequiresValidState {

    private IMethods mock;

    @Before
    public void setUp() {
        StateResetter.reset();
        mock = Mockito.mock(IMethods.class);
    }

    @After
    public void resetState() {
        StateResetter.reset();
    }

    @Test
    public void shouldDetectWrongNumberOfMatchersWhenStubbing() {
        Mockito.stub(mock.threeArgumentMethod(1, "2", "3")).andReturn(null);
        try {
            Mockito.stub(mock.threeArgumentMethod(1, eq("2"), "3")).andReturn(null);
            fail();
        } catch (InvalidUseOfMatchersException e) {}
    }

    @Test
    public void shouldDetectStupidUseOfMatchersWhenVerifying() {
        mock.oneArg(true);
        eq("that's the stupid way");
        eq("of using matchers");
        try {
            Mockito.verify(mock).oneArg(true);
            fail();
        } catch (InvalidUseOfMatchersException e) {}
    }

    @Test
    public void shouldScreamWhenMatchersAreInvalid() {
        mock.simpleMethod(CrazyMatchers.not(eq("asd")));
        try {
            mock.simpleMethod(CrazyMatchers.not("jkl"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertEquals(
                    "\n" +
                    "No matchers found for Not(?)." +
                    "\n" +
                    "Read more: http://code.google.com/p/mockito/matchers"
                    , e.getMessage());
        }

        try {
            mock.simpleMethod(CrazyMatchers.or(eq("jkl"), "asd"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertEquals(
                    "\n" +
                    "2 matchers expected, 1 recorded." +
                    "\n" +
                    "Read more: http://code.google.com/p/mockito/matchers"
                    , e.getMessage());
        }

        try {
            mock.threeArgumentMethod(1, "asd", eq("asd"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertEquals(
                    "\n" +
                    "3 matchers expected, 1 recorded." +
                    "\n" +
                    "Read more: http://code.google.com/p/mockito/matchers"
                    , e.getMessage());
        }
    }
}