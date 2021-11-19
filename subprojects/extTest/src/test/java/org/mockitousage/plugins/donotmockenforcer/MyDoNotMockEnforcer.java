/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.donotmockenforcer;

import org.mockito.plugins.DoNotMockEnforcer;

public class MyDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public String checkTypeForDoNotMockViolation(Class<?> type) {
        // Special case a type, because we want to opt-out of enforcing
        if (type.getName().endsWith("NotMockableButSpecialCased")) {
            return null;
        }
        if (type.getName().startsWith("org.mockitousage.plugins.donotmockenforcer")) {
            return "Custom message!";
        }
        return null;
    }
}
