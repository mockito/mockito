/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

import java.util.List;

import static java.util.Arrays.asList;

public class MissingInvocationInOrderCheckerTest  {

	private MissingInvocationInOrderChecker checker;
	private InvocationMatcher wanted;
	private List<Invocation> invocations;

	@Mock
	private IMethods mock;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
    private InOrderContext context = new InOrderContextImpl();
    
    @Before
    public void setup() {
        checker = new MissingInvocationInOrderChecker();
    }                                                                    

    @Test
    public void shouldPassWhenMatchingInteractionFound() throws Exception {
        
    	invocations = asList(buildSimpleMethod().toInvocation());
        wanted = buildSimpleMethod().toInvocationMatcher();
    	
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() throws Exception {
    	invocations = asList(buildDifferentMethod().toInvocation());
        wanted = buildSimpleMethod().toInvocationMatcher();
    	
        exception.expect(WantedButNotInvoked.class);
		exception.expectMessage("Wanted but not invoked:");
		exception.expectMessage("mock.simpleMethod()");

        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
    }

    @Test
    public void shouldReportArgumentsAreDifferent() throws Exception {
    	invocations = asList(buildIntArgMethod().arg(1111).toInvocation());
        wanted = buildIntArgMethod().arg(2222).toInvocationMatcher();
        
        exception.expect(ArgumentsAreDifferent.class);

		exception.expectMessage("Argument(s) are different! Wanted:");
		exception.expectMessage("mock.intArgumentMethod(2222);");
		exception.expectMessage("Actual invocation has different arguments:");
		exception.expectMessage("mock.intArgumentMethod(1111);");
    	
    	checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
        
     }
    
    @Test
    public void shouldReportWantedDiffersFromActual() throws Exception {
        
    	Invocation invocation1 = buildIntArgMethod().arg(1111).toInvocation();
    	Invocation invocation2 = buildIntArgMethod().arg(2222).toInvocation();
    	
    	context.markVerified(invocation2);
		invocations = asList(invocation1,invocation2);
        wanted = buildIntArgMethod().arg(2222).toInvocationMatcher();
    	
        exception.expect(VerificationInOrderFailure.class);

		exception.expectMessage("Verification in order failure");
		exception.expectMessage("Wanted but not invoked:");
		exception.expectMessage("mock.intArgumentMethod(2222);");
		exception.expectMessage("Wanted anywhere AFTER following interaction:");
		exception.expectMessage("mock.intArgumentMethod(2222);");
		
        checker.check(invocations, wanted, new VerificationModeBuilder().inOrder(), context);
    }
    
    private InvocationBuilder buildIntArgMethod() {
		return new InvocationBuilder().mock(mock).method("intArgumentMethod").argTypes(int.class);
	}

	private InvocationBuilder buildSimpleMethod() {
		return new InvocationBuilder().mock(mock).simpleMethod();
	}

	private InvocationBuilder buildDifferentMethod() {
		return new InvocationBuilder().mock(mock).differentMethod();
	}
    
   
}