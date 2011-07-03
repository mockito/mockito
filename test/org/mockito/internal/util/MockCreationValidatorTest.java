/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockCreationValidatorTest extends TestBase {

    final class FinalClass {}
    MockCreationValidator validator = new MockCreationValidator();
    
    @Test
    public void shouldNotAllowExtraInterfaceThatIsTheSameAsTheMockedType() throws Exception {
        try {
            //when
            validator.validateExtraInterfaces(IMethods.class, new Class<?>[] {IMethods.class});
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("You mocked following type: IMethods", e.getMessage());
        }
    }
        
    @Test
    public void shouldNotAllowsInconsistentTypes() throws Exception {
        try {
            //when
            validator.validateMockedType(List.class, new ArrayList());
            fail();
            //then
        } catch(MockitoException e) {}
    }
    
    @Test
    public void shouldAllowOnlyConsistentTypes() throws Exception {
        //when
        validator.validateMockedType(ArrayList.class, new ArrayList());
        //then no exception is thrown
    }
    
    @Test
    public void shouldValidationBeSafeWhenNullsPassed() throws Exception {
        //when
        validator.validateMockedType(null, new ArrayList());
        //or
        validator.validateMockedType(ArrayList.class, null);
        //then no exception is thrown
    }
}