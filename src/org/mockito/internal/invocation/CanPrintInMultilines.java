package org.mockito.internal.invocation;

public interface CanPrintInMultilines {

    String toString();
    boolean printsInMultilines();
    String toMultilineString();

}