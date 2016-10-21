package org.mockitousage.debugging;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.debugging.InvocationsPrinter;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class InvocationsPrinterTest extends TestBase {

    @Mock IMethods mock;

    @Test public void no_invocations() {
        Assert.assertEquals("No interactions and stubbings found for mock: mock", new InvocationsPrinter().printInvocations(mock));
    }

    @Test public void prints_invocations() {
        mock.simpleMethod(100);
        triggerInteraction();

        assertThat(filterLineNo("[Mockito] Interactions of: mock\n" +
                        " 1. mock.simpleMethod(100);\n" +
                        "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations(InvocationsPrinterTest.java:0)\n" +
                        " 2. mock.otherMethod();\n" +
                        "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:0)\n"))
                .isEqualTo(filterLineNo(new InvocationsPrinter().printInvocations(mock)));
    }

    @Test public void prints_stubbings() {
        triggerStubbing();

        assertThat(filterLineNo("[Mockito] Unused stubbings of: mock\n" +
                " 1. mock.simpleMethod(\"a\");\n" +
                "  - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:70)\n"))
                .isEqualTo(filterLineNo(new InvocationsPrinter().printInvocations(mock)));
    }

    @Test public void prints_invocations_and_stubbings() {
        triggerStubbing();

        mock.simpleMethod("a");
        triggerInteraction();

        assertThat(filterLineNo("[Mockito] Interactions of: mock\n" +
                        " 1. mock.simpleMethod(\"a\");\n" +
                        "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations_and_stubbings(InvocationsPrinterTest.java:49)\n" +
                        "   - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:73)\n" +
                        " 2. mock.otherMethod();\n" +
                        "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:34)\n"))
                .isEqualTo(filterLineNo(new InvocationsPrinter().printInvocations(mock)));
    }

    @Test public void prints_invocations_and_unused_stubbings() {
        triggerStubbing();

        mock.simpleMethod("b");
        triggerInteraction();

        assertThat(filterLineNo("[Mockito] Interactions of: mock\n" +
                " 1. mock.simpleMethod(\"b\");\n" +
                "  -> at org.mockitousage.debugging.InvocationsPrinterTest.prints_invocations_and_unused_stubbings(InvocationsPrinterTest.java:55)\n" +
                " 2. mock.otherMethod();\n" +
                "  -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerInteraction(InvocationsPrinterTest.java:34)\n" +
                "[Mockito] Unused stubbings of: mock\n" +
                " 1. mock.simpleMethod(\"a\");\n" +
                "  - stubbed -> at org.mockitousage.debugging.InvocationsPrinterTest.triggerStubbing(InvocationsPrinterTest.java:62)\n"))
                .isEqualTo(filterLineNo(new InvocationsPrinter().printInvocations(mock)));
    }

    private void triggerInteraction() {
        mock.otherMethod();
    }

    private void triggerStubbing() {
        when(mock.simpleMethod("a")).thenReturn("x");
    }
}