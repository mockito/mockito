/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.util;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(out)
            .contains("1. -> at")
            .contains("2. [?]-> at");
    }
    
    @Test
    public void shouldNotPrintInvocationsWhenSingleUnwanted() {
        //given
        Invocation unverified = new InvocationBuilder().differentMethod().toInvocation();
        
        //when
        String out = sp.print((List) asList(unverified));
        
        //then
        assertThat(out).contains("Actually, above is the only interaction with this mock.");
    }
}