/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.handler.MockHandlerFactory.createMockHandler;

/**
 * by Szczepan Faber, created at: 5/22/12
 */
public class MockHandlerFactoryTest extends TestBase {

    private final IMethods mock = Mockito.mock(IMethods.class);

    @Test
    //see issue 331
    public void handle_result_must_not_be_null_for_primitives() throws Throwable {
        //given:
        MockCreationSettings<?> settings = (MockCreationSettings<?>) new MockSettingsImpl().defaultAnswer(new Returns(null));
		MockHandler<?> handler = createMockHandler(settings);

        mock.intReturningMethod();
        Invocation invocation = super.getLastInvocation();

        //when:
        Object result = handler.handle(invocation);

        //then null value is not a valid result for a primitive
        assertNotNull(result);
        assertEquals(0, result);
    }

    @Test
    //see issue 331
    public void valid_handle_result_is_permitted() throws Throwable {
        //given:
        MockCreationSettings<?> settings = (MockCreationSettings<?>) new MockSettingsImpl().defaultAnswer(new Returns(123));
        MockHandler<?> handler =  createMockHandler(settings);

        mock.intReturningMethod();
        Invocation invocation = super.getLastInvocation();

        //when:
        Object result = handler.handle(invocation);

        //then
        assertEquals(123, result);
    }
}
