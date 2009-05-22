/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CreationValidatorTest extends TestBase {

    final class FinalClass {}
    
    @Test
    public void shouldNotAllowExtraInterfaceThatIsTheSameAsTheMockedType() throws Exception {
        //given
        CreationValidator validator = new CreationValidator();
        
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
    public void shouldIgnoreIfExtraInterfacesAreNull() throws Exception {
        //given
        CreationValidator validator = new CreationValidator();
        
        //when
        validator.validateExtraInterfaces(IMethods.class, (Class[]) null);

        //then ok
    }
    
    @Test
    public void shouldNotAllowFinalClasses() throws Exception {
        //given
        CreationValidator validator = new CreationValidator();
        
        try {
            //when
            validator.validateType(FinalClass.class);
            fail();
        } catch (MockitoException e) {
            //then
            assertContains("Cannot mock/spy", e.getMessage());
        }
    }
}