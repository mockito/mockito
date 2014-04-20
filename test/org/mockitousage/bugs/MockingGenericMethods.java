/*
 * Copyright (c) 2014 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;

import junit.framework.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockingGenericMethods {

    private static final String GET_ID_RESULT = "Simple_result";

    public interface SimpleInterface{
        String getId();
    }

    public class SimpleGenericClass<T>{
        public T getId(){
            throw new IllegalStateException("Should be mocked");
        }
    }

    public class GenericClassExtension extends SimpleGenericClass<String> implements SimpleInterface{

    }

    @Test
    public void should_generic_method_be_mocked_successfully(){
        //given
        GenericClassExtension instance = mock(GenericClassExtension.class);
        when(instance.getId()).thenReturn(GET_ID_RESULT);

        //when
        String invokeVirtualResult = instance.getId();
        String invokeInterfaceResult = ((SimpleInterface)instance).getId();

        //then
        Assert.assertEquals(invokeVirtualResult, invokeInterfaceResult);
    }
}
