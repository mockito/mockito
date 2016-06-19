/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InjectionByTypeShouldFirstLookForExactTypeThenAncestorTest {

    private static final Object REFERENCE = new Object();

    @Mock private Bean mockedBean;

    @InjectMocks private Service illegalInjectionExample = new Service();
    @InjectMocks private ServiceWithReversedOrder reversedOrderService = new ServiceWithReversedOrder();
    @InjectMocks private WithNullObjectField withNullObjectField = new WithNullObjectField();

    @Test
    public void just_for_information_fields_are_read_in_declaration_order_see_Service() {
        Field[] declaredFields = Service.class.getDeclaredFields();

        assertEquals("mockShouldNotGoInHere", declaredFields[0].getName());
        assertEquals("mockShouldGoInHere", declaredFields[1].getName());
    }

    @Test
    public void mock_should_be_injected_once_and_in_the_best_matching_type() {
        assertSame(REFERENCE, illegalInjectionExample.mockShouldNotGoInHere);
        assertSame(mockedBean, illegalInjectionExample.mockShouldGoInHere);
    }

    @Test
    public void should_match_be_consistent_regardless_of_order() {
        assertSame(REFERENCE, reversedOrderService.mockShouldNotGoInHere);
        assertSame(mockedBean, reversedOrderService.mockShouldGoInHere);
    }

    @Test
    public void should_inject_the_mock_only_once_and_in_the_correct_type() {
        assertNull(withNullObjectField.keepMeNull);
        assertSame(mockedBean, withNullObjectField.injectMePlease);
    }

    public static class Bean {}

    public static class Service {
        public final Object mockShouldNotGoInHere = REFERENCE;
        public Bean mockShouldGoInHere;
    }

    public static class ServiceWithReversedOrder {
        public Bean mockShouldGoInHere;
        public final Object mockShouldNotGoInHere = REFERENCE;
    }

    class WithNullObjectField{
        Bean injectMePlease;
        Object keepMeNull = null;
    }
}
