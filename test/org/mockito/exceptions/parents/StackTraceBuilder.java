package org.mockito.exceptions.parents;

import java.util.*;

public class StackTraceBuilder {
    
    private String[] methods;

    public StackTraceBuilder methods(String ... methods) {
        this.methods = methods;
        return this;
    }

    public StackTraceElement[] toStackTrace() {
        StackTraceElement[] trace = new StackTraceElement[methods.length];
        
        for (int i = 0 ; i < methods.length ; i++) {
            trace[i] = new StackTraceElement("DummyClass", methods[i], "DummyClass.java", 100);
        }
        
        return trace;
    }

    public List<StackTraceElement> toStackTraceList() {
        return Arrays.asList(toStackTrace());
    }
}
