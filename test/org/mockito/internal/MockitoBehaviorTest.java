package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class MockitoBehaviorTest {

    private Method toLowerCase;
    private Method toUpperCase;
    private MockitoBehavior behavior;

    @Before
    public void setup() throws Exception {
        toLowerCase = String.class.getMethod("toLowerCase", new Class[] {});
        toUpperCase = String.class.getMethod("toUpperCase", new Class[] {});
        behavior = new MockitoBehavior();
    }
    
    @Ignore
    @Test
    public void shouldMarkVerifiedOnlyOneExecutionChunk() throws Exception {
        ExpectedInvocation toLowerCaseInvocation = new ExpectedInvocation(new Invocation("mock", toLowerCase , new Object[] {}), Collections.EMPTY_LIST);
        ExpectedInvocation toUpperCaseInvocation = new ExpectedInvocation(new Invocation("mock", toUpperCase , new Object[] {}), Collections.EMPTY_LIST);
        
        behavior.addInvocation(toLowerCaseInvocation);
        behavior.addInvocation(toLowerCaseInvocation);
        
        behavior.addInvocation(toUpperCaseInvocation);
        behavior.addInvocation(toLowerCaseInvocation);
        
        behavior.markInvocationsAsVerified(toLowerCaseInvocation, VerifyingMode.inOrder(2, Arrays.asList(new Object())));
        
        List<ExpectedInvocation> invocations = behavior.getRegisteredInvocations();
        assertEquals(true, invocations.get(0).getInvocation().isVerified());
        assertEquals(true, invocations.get(1).getInvocation().isVerified());
        assertEquals(false, invocations.get(2).getInvocation().isVerified());
        assertEquals(false, invocations.get(3).getInvocation().isVerified());
    }
}
