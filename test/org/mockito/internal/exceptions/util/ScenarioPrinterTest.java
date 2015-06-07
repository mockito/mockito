/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.util;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ScenarioPrinterTest extends TestBase {

    ScenarioPrinter sp = new ScenarioPrinter();
    
    @Test
    public void shouldPrintInvocations() {
        //given
        final Invocation verified = new InvocationBuilder().simpleMethod().verified().toInvocation();
        final Invocation unverified = new InvocationBuilder().differentMethod().toInvocation();
        
        //when
        final String out = sp.print((List) asList(verified, unverified));
        
        //then
        assertContains("1. -> at", out);
        assertContains("2. [?]-> at", out);
    }
    
    @Test
    public void shouldNotPrintInvocationsWhenSingleUnwanted() {
        //given
        final Invocation unverified = new InvocationBuilder().differentMethod().toInvocation();
        
        //when
        final String out = sp.print((List) asList(unverified));
        
        //then
        assertContains("Actually, above is the only interaction with this mock.", out);
    }
}