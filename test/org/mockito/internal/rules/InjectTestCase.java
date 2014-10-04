package org.mockito.internal.rules;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

@Ignore("used internally by JUnitRuleTest")
public class InjectTestCase {

    @Mock
    private Injected injected;

    @InjectMocks
    private InjectInto injectInto;

    @Test
    public void dummy() throws Exception {
    }

    public void unfinishedStubbingThrowsException() throws Exception {
        Mockito.when(injected.stringMethod());
    }

    public Injected getInjected() {
        return injected;
    }

    public InjectInto getInjectInto() {
        return injectInto;
    }

    private static class Injected {
        public String stringMethod() {
            return "string";
        }
    }

    private static class InjectInto {
        private Injected injected;
    }

}
