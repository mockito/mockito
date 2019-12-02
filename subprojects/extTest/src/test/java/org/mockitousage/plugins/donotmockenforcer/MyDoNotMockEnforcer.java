/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.donotmockenforcer;

import java.util.Optional;
import org.mockito.plugins.DoNotMockEnforcer;

public class MyDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public Optional<String> allowMockType(Class<?> type) {
        // Special case a type, because we want to opt-out of enforcing
        if (type.getName().endsWith("NotMockableButSpecialCased")) {
            return Optional.empty();
        }
        if (type.getName().startsWith("org.mockitousage.plugins.donotmockenforcer")) {
            return Optional.of("Custom message!");
        }
        return Optional.empty();
    }
}
