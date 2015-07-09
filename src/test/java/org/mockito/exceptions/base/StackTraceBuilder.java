/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions.base;

import java.util.Arrays;
import java.util.List;

public class StackTraceBuilder {
    
    private String[] methods;

    public StackTraceBuilder methods(String ... methods) {
        this.methods = methods;
        return this;
    }

    public StackTraceElement[] toStackTrace() {
        StackTraceElement[] trace = new StackTraceElement[methods.length];
        
        for (int i = 0; i < methods.length; i++) {
            trace[i] = new StackTraceElement("DummyClass", methods[i], "DummyClass.java", 100);
        }
        
        return trace;
    }

    public List<StackTraceElement> toStackTraceList() {
        return Arrays.asList(toStackTrace());
    }
}
