package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import org.junit.Test;
import org.example.org.mockito.DoNotMock;

/**
 * Repro for issue #3592:
 * DefaultDoNotMockEnforcer does not include custom DoNotMock annotation's toString()
 * (and thus its fields like "reason") in the violation message.
 */
public class DefaultDoNotMockEnforcerCustomAnnotationTest {

    @DoNotMock(reason = "use ExampleFake instead")
    static class Offender {}

    @Test
    public void violationMessage_shouldIncludeCustomAnnotationFields() {
        DefaultDoNotMockEnforcer enforcer = new DefaultDoNotMockEnforcer();

        // ✅ correct API name:
        String message = enforcer.checkTypeForDoNotMockViolation(Offender.class);
        assertNotNull("Expected a violation message for custom DoNotMock", message);

        // Expect the reason (from the custom annotation) to appear in the message.
        // Current behavior (before fix) likely misses this, so the assertion should FAIL.
        assertTrue(
            "Expected violation message to include the custom annotation reason",
            message.contains("use ExampleFake instead")
        );

        // If you also want to assert the annotation FQN is printed (stricter), uncomment:
        // assertTrue(message.contains("@org.example.org.mockito.DoNotMock"));
    }
}
