/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import org.mockito.internal.creation.MockAwareInterceptor;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsChunker;
import org.mockito.internal.invocation.InvocationsMarker;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.OngoingStubbing;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockito.internal.stubbing.EmptyReturnValues;
import org.mockito.internal.stubbing.StubbedMethodSelector;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.MissingInvocationVerifier;
import org.mockito.internal.verification.NoMoreInvocationsVerifier;
import org.mockito.internal.verification.NumberOfInvocationsVerifier;
import org.mockito.internal.verification.Verifier;
import org.mockito.internal.verification.VerifyingRecorder;
import org.mockito.internal.verification.WrongOrderOfInvocationsVerifier;

/**
 * Invocation handler set on mock objects.
 *
 * @param <T> type of mock object to handle
 */
public class MockHandler<T> implements MockAwareInterceptor<T>, OngoingStubbing<T>, VoidMethodStubbable<T>, StubbedMethodSelector<T> {

    private final VerifyingRecorder verifyingRecorder;
    private final Stubber stubber;
    private final MatchersBinder matchersBinder;
    private final MockingProgress mockingProgress;
    
    private T mock;
    
    public MockHandler(MockingProgress mockingProgress, MatchersBinder matchersBinder) {
        this.mockingProgress = mockingProgress;
        this.matchersBinder = matchersBinder;
        stubber = new Stubber(mockingProgress);
        
        verifyingRecorder = createRecorder(); 
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (stubber.hasThrowableForVoidMethod()) {
            Invocation invocation = new Invocation(proxy, method, args, mockingProgress.nextSequenceNumber());
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
            stubber.addVoidMethodForThrowable(invocationMatcher);
            return null;
        }
        
        VerificationModeImpl verificationMode = mockingProgress.pullVerificationMode();
        mockingProgress.validateState();
        
        Invocation invocation = new Invocation(proxy, method, args, mockingProgress.nextSequenceNumber());
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
        
        if (verificationMode != null) {
            verifyingRecorder.verify(invocationMatcher, verificationMode);
            return EmptyReturnValues.emptyValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationMatcher);
        verifyingRecorder.recordInvocation(invocationMatcher.getInvocation());

        mockingProgress.reportStubbable(this);
        
        return stubber.resultFor(invocationMatcher.getInvocation());
    }

    public void verifyNoMoreInteractions() {
        verifyingRecorder.verify(VerificationModeImpl.noMoreInteractions());
    }
    
    public void andReturn(T value) {
        verifyingRecorder.eraseLastInvocation();
        stubber.addReturnValue(value);
    }

    public void andThrow(Throwable throwable) {
        verifyingRecorder.eraseLastInvocation();
        stubber.addThrowable(throwable);
    }
    
    public StubbedMethodSelector<T> toThrow(Throwable throwable) {
        stubber.addThrowableForVoidMethod(throwable);
        return this;
    }

    public T on() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }

    public List<Invocation> getRegisteredInvocations() {
        return verifyingRecorder.getRegisteredInvocations();
    }
    
    private VerifyingRecorder createRecorder() {
        InvocationsChunker chunker = new InvocationsChunker(new AllInvocationsFinder());
        InvocationsMarker marker = new InvocationsMarker();
        List<Verifier> verifiers = Arrays.asList(
                new MissingInvocationVerifier(),
                new WrongOrderOfInvocationsVerifier(),
                new NumberOfInvocationsVerifier(),
                new NoMoreInvocationsVerifier());
        return new VerifyingRecorder(chunker, marker, verifiers);
    }
}