/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class InvalidUseOfMatchersTest extends TestBase {

    private IMethods mock;

    @Before
    public void setUp() {
        StateMaster.reset();
        mock = Mockito.mock(IMethods.class);
    }

    @After
    public void resetState() {
        StateMaster.reset();
    }

    @Test
    public void shouldDetectWrongNumberOfMatchersWhenStubbing() {
        Mockito.when(mock.threeArgumentMethod(1, "2", "3")).thenReturn(null);
        try {
            Mockito.when(mock.threeArgumentMethod(1, eq("2"), "3")).thenReturn(null);
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
        mock.simpleMethod(AdditionalMatchers.not(eq("asd")));
        try {
            mock.simpleMethod(AdditionalMatchers.not("jkl"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e, messageContains("No matchers found for Not(?)."));
        }

        try {
            mock.simpleMethod(AdditionalMatchers.or(eq("jkl"), "asd"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e, messageContains("2 matchers expected, 1 recorded."));
        }

        try {
            mock.threeArgumentMethod(1, "asd", eq("asd"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e, messageContains("3 matchers expected, 1 recorded."));
        }
    }
}