package org.mockito.internal.debugging;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.MockitoLogger;

public class DebuggingInfo {

    private final List<Invocation> unusedStubs = new LinkedList<Invocation>();
    private List<InvocationMatcher> unstubbedInvocations = new LinkedList<InvocationMatcher>();
    
    //I don't know if this is needed yet
    @SuppressWarnings("unused")
    private final String testName;

    public DebuggingInfo(String testName) {
        this.testName = testName;
    }

    public void addUnusedStub(Invocation invocation) {
        this.unusedStubs.add(invocation);
    }

    public void printInfo(MockitoLogger logger) {
        if (!shouldPrint()) {
            return;
        }
        
//        print("Mockito detected some of your stubs were not called. This *might* be the reason your test failed.");
//        print("Test:");
//        print(test);
        
        Iterator<Invocation> unusedIterator = unusedStubs.iterator();
        while(unusedIterator.hasNext()) {
            Invocation unused = unusedIterator.next();
            Iterator<InvocationMatcher> unstubbedIterator = unstubbedInvocations.iterator();
            while(unstubbedIterator.hasNext()) {
                InvocationMatcher unstubbed = unstubbedIterator.next();
                if(unstubbed.hasSimilarMethod(unused)) { 
                    logger.print("Warning - stubbed method called with different arguments.");
                    logger.print("Stubbed this way:");
                    logger.print(unused);
                    logger.print(unused.getStackTrace().getStackTrace()[0]);
                    logger.print("But called with different arguments:");
                    logger.print(unstubbed.getInvocation());
                    logger.print(unstubbed.getInvocation().getStackTrace().getStackTrace()[0]);
                    logger.print();
                    
                    unusedIterator.remove();
                    unstubbedIterator.remove();
                }
            }
        }
        
        for (Invocation i : unusedStubs) {
            logger.print("Warning - this stub was not used:");
            logger.print(i);
            logger.print(i.getStackTrace().getStackTrace()[0]);
            logger.print();
        }
        
        for (InvocationMatcher i : unstubbedInvocations) {
            logger.print("Warning - this method was not stubbed:");
            logger.print(i.getInvocation());
            logger.print(i.getInvocation().getStackTrace().getStackTrace()[0]);
            logger.print();
        }
    }

    private boolean shouldPrint() {
        //TODO test, include unstubbedInvocations...
        return !unusedStubs.isEmpty() || !unstubbedInvocations.isEmpty();
    }

    public void addUnstubbedInvocation(InvocationMatcher invocation) {
        unstubbedInvocations.add(invocation);
    }
}