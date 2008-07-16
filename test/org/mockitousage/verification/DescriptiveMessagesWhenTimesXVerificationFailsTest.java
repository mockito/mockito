/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.cause.TooLittleInvocations;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class DescriptiveMessagesWhenTimesXVerificationFailsTest extends TestBase {

    private LinkedList mock;

    @Before
    public void setup() {
        mock = Mockito.mock(LinkedList.class);
    }

    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(3)).clear();
        try {
            Mockito.verify(mock, times(100)).clear();
            fail();
        } catch (TooLittleActualInvocations e) {
            String expected =
                "\n" +
                "linkedList.clear();" +
                "\n" +
                "Wanted 100 times but was 3";
            assertEquals(expected, e.getMessage());

            assertEquals(TooLittleInvocations.class, e.getCause().getClass());

            String expectedCause =
                "\n" +
                "Too little invocations:";
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(4)).clear();
        try {
            Mockito.verify(mock, times(1)).clear();
            fail();
        } catch (TooManyActualInvocations e) {
            String expected =
                "\n" +
                "linkedList.clear();" +
                "\n" +
                "Wanted 1 time but was 4";
            assertEquals(expected, e.getMessage());

            assertEquals(UndesiredInvocation.class, e.getCause().getClass());

            String expectedCause =
                "\n" +
                "Undesired invocation:";

            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }
}