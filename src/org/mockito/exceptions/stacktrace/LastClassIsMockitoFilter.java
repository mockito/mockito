/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.stacktrace;


public class LastClassIsMockitoFilter implements StackTraceFilter {
    public boolean isLastStackElementToRemove(StackTraceElement e) {
        return e.getClassName().equals("org.mockito.Mockito");
    }
}