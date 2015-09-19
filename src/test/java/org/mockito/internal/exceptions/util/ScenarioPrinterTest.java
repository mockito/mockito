/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.util;

import static java.util.Arrays.*;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ScenarioPrinterTest extends TestBase {

    ScenarioPrinter sp = new ScenarioPrinter();
    
    @Test
    public void shouldPrintInvocations() {
        //given
        Invocation verified = new InvocationBuilder().simpleMethod().verified().toInvocation();
        Invocation unverified = new InvocationBuilder().differentMethod().toInvocation();
        
        //when
        String out = sp.print((List) asList(verified, unverified));
        
        //then
        assertContains("1. -> at", out);
        assertContains("2. [?]-> at", out);
    }
    
    @Test
    public void shouldNotPrintInvocationsWhenSingleUnwanted() {
        //given
        Invocation unverified = new InvocationBuilder().differentMethod().toInvocation();
        
        //when
        String out = sp.print((List) asList(unverified));
        
        //then
        assertContains("Actually, above is the only interaction with this mock.", out);
    }
}