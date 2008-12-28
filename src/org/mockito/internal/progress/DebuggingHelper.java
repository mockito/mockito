package org.mockito.internal.progress;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;

//hate the name but I couldn't figure out the better way of doing it
public class DebuggingHelper {

    private final List<Invocation> stubbedInvocations = new LinkedList<Invocation>();
    private final List<Invocation> unstubbedInvocations = new LinkedList<Invocation>();

    public void addStubbedInvocation(Invocation invocation) {
        unstubbedInvocations.remove(invocation);
        stubbedInvocations.add(invocation);
    }

    public void addUnstubbedInvocation(Invocation invocation) {
        unstubbedInvocations.add(invocation);
    }

    public List<Invocation> pullStubbedInvocations() {
        List<Invocation> ret = new LinkedList<Invocation>(stubbedInvocations);
        stubbedInvocations.clear();
        return ret;
    }

    public List<Invocation> pullUnstubbedInvocations() {
        List<Invocation> ret = new LinkedList<Invocation>(unstubbedInvocations);
        unstubbedInvocations.clear();
        return ret;
    }

    public void collectData() {
        // TODO Auto-generated method stub
        
    }

    public void clearData() {
        // TODO Auto-generated method stub
        
    }
}