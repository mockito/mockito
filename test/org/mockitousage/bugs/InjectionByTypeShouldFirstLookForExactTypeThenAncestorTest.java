package org.mockitousage.bugs;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class InjectionByTypeShouldFirstLookForExactTypeThenAncestorTest {

    @Mock private Bean mockedBean;
    @InjectMocks private Service illegalInjectionExample = new Service();

    private Object reference = new Object();

    @Test
    public void just_for_information_fields_are_read_in_declaration_order_see_Service() {
        Field[] declaredFields = Service.class.getDeclaredFields();

        assertEquals("mockShouldNotGoInHere", declaredFields[0].getName());
        assertEquals("mockShouldGoInHere", declaredFields[1].getName());
    }

    @Test
    @Ignore
    public void test() {
        assertSame(reference, illegalInjectionExample.mockShouldNotGoInHere);
        assertSame(mockedBean, illegalInjectionExample.mockShouldGoInHere);
    }

    public class Bean {}

    public class Service {

        public final Object mockShouldNotGoInHere = reference;

        public Bean mockShouldGoInHere;

    }


}
