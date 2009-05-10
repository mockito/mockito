/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MockingMultipleInterfacesTest extends TestBase {

    class Foo {}
    interface IFoo {}
    interface IBar {}
    
    @Test
    public void shouldAllowMultipleInterfaces() {
        //when
        Foo mock = mock(Foo.class, configureWith().extraInterfaces(IFoo.class, IBar.class));
        
        //then
        assertThat(mock, is(IFoo.class));
        assertThat(mock, is(IBar.class));
    }
}