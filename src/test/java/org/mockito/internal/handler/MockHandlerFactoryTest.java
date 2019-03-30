/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.invocation.MockHandlerFactory;
import org.mockito.mock.MockCreationSettings;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * by Szczepan Faber, created at: 5/22/12
 */
public class MockHandlerFactoryTest extends TestBase {

    private final IMethods mock = Mockito.mock(IMethods.class);
    private static final MockHandlerFactory factory = new DefaultMockHandlerFactory();

    @Test
    //see issue 331
    public void handle_result_must_not_be_null_for_primitives() throws Throwable {
        //given:
        MockCreationSettings<?> settings = (MockCreationSettings<?>) new MockSettingsImpl().defaultAnswer(new Returns(null));
		MockHandler<?> handler = factory.createMockHandler(settings);

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
        MockHandler<?> handler =  factory.createMockHandler(settings);

        mock.intReturningMethod();
        Invocation invocation = super.getLastInvocation();

        //when:
        Object result = handler.handle(invocation);

        //then
        assertEquals(123, result);
    }

    @Test
    public void alternate_MockHandlerFactory_is_called() {
        //given:
        TestMockedHandlerFactory altFactory = new TestMockedHandlerFactory();

        //when:
        IMethods mock1 = Mockito.mock(IMethods.class);
        IMethods mockAlt = Mockito.mock(IMethods.class, Mockito.withSettings().mockHandlerFactory(altFactory));
        IMethods mock2 = Mockito.mock(IMethods.class);

        //then alt factory used once
        assertEquals(1, altFactory.callCount);
    }

    @Test
    public void alternate_MockHandlerFactory_is_called_on_reset() {
        //given:
        TestMockedHandlerFactory altFactory = new TestMockedHandlerFactory();
        IMethods mock = Mockito.mock(IMethods.class);
        IMethods altMock = Mockito.mock(IMethods.class, Mockito.withSettings().mockHandlerFactory(altFactory));
        MockHandler handler = MockUtil.getMockHandler(mock);
        MockHandler altHandler = MockUtil.getMockHandler(altMock);

        //when:
        MockUtil.resetMock(mock);
        MockUtil.resetMock(altMock);

        //then alt factory used twice: 1 for creation and 1 for reset
        assertEquals(2, altFactory.callCount);
        assertNotEquals(handler, MockUtil.getMockHandler(mock));
        assertNotEquals(altHandler, MockUtil.getMockHandler(altMock));
    }


    private static class TestMockedHandlerFactory extends DefaultMockHandlerFactory{
        public int callCount = 0;

        @Override
        public <T> MockHandler<T> createMockHandler(MockCreationSettings<T> settings){
            callCount++;
            return super.createMockHandler(settings);
        }
    }
}
