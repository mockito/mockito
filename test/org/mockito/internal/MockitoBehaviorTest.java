package org.mockito.internal;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.*;
import org.mockito.exceptions.NumberOfInvocationsAssertionError;

@SuppressWarnings("unchecked")
public class MockitoBehaviorTest {

    private MockitoBehavior behavior;
    private ExpectedInvocation toLowerCaseInvocation;
    private ExpectedInvocation toUpperCaseInvocation;
    private ExpectedInvocation toLowerCaseInvocationTwo;
    private ExpectedInvocation toLowerCaseInvocationThree;

    @Before
    public void setup() throws Exception {
        Method toLowerCase = String.class.getMethod("toLowerCase", new Class[] {});
        Method toUpperCase = String.class.getMethod("toUpperCase", new Class[] {});
        
        toLowerCaseInvocation = new ExpectedInvocation(new Invocation("mock", toLowerCase , new Object[] {}), Collections.EMPTY_LIST);
        toLowerCaseInvocationTwo = new ExpectedInvocation(new Invocation("mock", toLowerCase , new Object[] {}), Collections.EMPTY_LIST);
        toLowerCaseInvocationThree = new ExpectedInvocation(new Invocation("mock", toLowerCase , new Object[] {}), Collections.EMPTY_LIST);
        toUpperCaseInvocation = new ExpectedInvocation(new Invocation("mock", toUpperCase , new Object[] {}), Collections.EMPTY_LIST);
        
        behavior = new MockitoBehavior();
        
        behavior.addInvocation(toLowerCaseInvocation);
        behavior.addInvocation(toLowerCaseInvocationTwo);
        
        behavior.addInvocation(toUpperCaseInvocation);
        
        behavior.addInvocation(toLowerCaseInvocationThree);
    }
    
    //TODO use some of this test to create test for invocationsChunker
    
    @Test
    public void shouldMarkAllInvocationsAsVerified() throws Exception {
        behavior.markInvocationsAsVerified(toLowerCaseInvocation, VerifyingMode.times(2));
        
        List<Invocation> invocations = behavior.getRegisteredInvocations();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAllInvocationsAsVerifiedWhenAtLeastOnceIsUsed() throws Exception {
        behavior.markInvocationsAsVerified(toLowerCaseInvocation, VerifyingMode.atLeastOnce());
        
        List<Invocation> invocations = behavior.getRegisteredInvocations();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldNeverMarkInvocationsAsVerifiedIfExpectedCountIsZero() throws Exception {
        behavior.markInvocationsAsVerified(toLowerCaseInvocation, VerifyingMode.times(0));
        
        List<Invocation> invocations = behavior.getRegisteredInvocations();
        assertEquals(false, invocations.get(0).isVerified());
        assertEquals(false, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenVerifyingInOrder() throws Exception {
        try {
            behavior.checkForWrongNumberOfInvocations(toLowerCaseInvocation, VerifyingMode.inOrder(1, Arrays.asList(new Object())));
        } catch (NumberOfInvocationsAssertionError e) {
            fail();
        }
    }
}
