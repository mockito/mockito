/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.donotmockenforcer;

import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockType;
import org.mockito.plugins.DoNotMockEnforcer;

public class MyDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public String checkTypeForDoNotMockViolation(Class<?> type) {
        // Special case a type, because we want to opt-out of enforcing
        if (type.getName().endsWith("NotMockableButSpecialCased")) {
            return null;
        }
        // Special case for allowing StaticallyNotMockable to still be used for regular mocks
        if (type.getName().endsWith("StaticallyNotMockable")) {
            return null;
        }
        if (type.getName().startsWith("org.mockitousage.plugins.donotmockenforcer")) {
            return "Custom message!";
        }
        return null;
    }

    @Override
    public String checkTypeForDoNotMockViolation(MockCreationSettings<?> creationSettings) {
        if (creationSettings.getMockType() == MockType.STATIC) {
            Class<?> type = creationSettings.getTypeToMock();
            if (type.getName().endsWith("StaticallyMockable")) {
                return null;
            }
            if (type.getName().startsWith("org.mockitousage.plugins.donotmockenforcer")) {
                return "Cannot mockStatic!";
            }
            return null;
        }
        return DoNotMockEnforcer.super.checkTypeForDoNotMockViolation(creationSettings);
    }
}
