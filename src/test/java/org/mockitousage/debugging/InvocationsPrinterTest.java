/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.debugging.InvocationsPrinter;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class InvocationsPrinterTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void no_invocations() {
        assertThat(new InvocationsPrinter().printInvocations(mock))
                .isEqualTo("No interactions and stubbings found for mock: mock");
    }

    @Test
    public void prints_invocations() {
        mock.simpleMethod(100);
        triggerInteraction();

        assertThat(filterLineNo(new InvocationsPrinter().printInvocations(mock)))
                .isEqualTo(
                        filterLineNo(
                                "[Mockito] Interactions of: mock\n"
                                        + " 1. mock.simpleMethod(100);\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations(InvocationsPrinterTest.java:0)\n"
                                        + " 2. mock.otherMethod();\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:0)\n"));
    }

    @Test
    public void prints_stubbings() {
        triggerStubbing();

        assertThat(filterLineNo(new InvocationsPrinter().printInvocations(mock)))
                .isEqualTo(
                        filterLineNo(
                                "[Mockito] Unused stubbings of: mock\n"
                                        + " 1. mock.simpleMethod(\"a\");\n"
                                        + "  - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:70)\n"));
    }

    @Test
    public void prints_invocations_and_stubbings() {
        triggerStubbing();

        mock.simpleMethod("a");
        triggerInteraction();

        assertThat(filterLineNo(new InvocationsPrinter().printInvocations(mock)))
                .isEqualTo(
                        filterLineNo(
                                "[Mockito] Interactions of: mock\n"
                                        + " 1. mock.simpleMethod(\"a\");\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations_and_stubbings(InvocationsPrinterTest.java:49)\n"
                                        + "   - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:73)\n"
                                        + " 2. mock.otherMethod();\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:34)\n"));
    }

    @Test
    public void prints_invocations_and_unused_stubbings() {
        triggerStubbing();

        mock.simpleMethod("b");
        triggerInteraction();

        assertThat(filterLineNo(new InvocationsPrinter().printInvocations(mock)))
                .isEqualTo(
                        filterLineNo(
                                "[Mockito] Interactions of: mock\n"
                                        + " 1. mock.simpleMethod(\"b\");\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations_and_unused_stubbings(InvocationsPrinterTest.java:55)\n"
                                        + " 2. mock.otherMethod();\n"
                                        + "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:34)\n"
                                        + "[Mockito] Unused stubbings of: mock\n"
                                        + " 1. mock.simpleMethod(\"a\");\n"
                                        + "  - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:62)\n"));
    }

    private void triggerInteraction() {
        mock.otherMethod();
    }

    private void triggerStubbing() {
        when(mock.simpleMethod("a")).thenReturn("x");
    }
}
