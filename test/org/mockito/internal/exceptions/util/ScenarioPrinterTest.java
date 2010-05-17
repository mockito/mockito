package org.mockito.internal.exceptions.util;

import static java.util.Arrays.*;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
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
        //TODO add more tests and scenarios that print decent stuff when nothing is verified or there are no verifications at all
    }
}