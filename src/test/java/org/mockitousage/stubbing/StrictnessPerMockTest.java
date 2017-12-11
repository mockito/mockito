/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class StrictnessPerMockTest {

    MockitoSession mockito = Mockito.mockitoSession().strictness(Strictness.STRICT_STUBS).startMocking();

    @Test public void strictness_per_mock() throws Throwable {
        //given
        IMethods mock = mock(IMethods.class);
        IMethods lenientMock = mock(IMethods.class, withSettings().strictness(Strictness.LENIENT));

        //when
        given(lenientMock.simpleMethod(100)).willReturn("100");
        given(mock.simpleMethod(100)).willReturn("100");

        //on lenient mock, we can call the stubbed method with different argument:
        lenientMock.simpleMethod(200);

        //on other mock, we will get strict stubbing exception
        try {
            mock.simpleMethod(200);
            fail();
        } catch (PotentialStubbingProblem e) {}

        //we are not reporting unused stubbings here:
        mockito.finishMocking();
    }
}
