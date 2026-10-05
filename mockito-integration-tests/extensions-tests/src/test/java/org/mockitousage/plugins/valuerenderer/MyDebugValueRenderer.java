/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.valuerenderer;

import java.lang.reflect.Method;

import org.mockito.plugins.ValueRenderer;

public class MyDebugValueRenderer implements ValueRenderer {
    @Override
    public int getPluginPriority() {
        return DEFAULT_PRIORITY + 2;
    }

    @Override
    public String print(Object value) {
        if (value == null) {
            return null;
        }
        for (Method it : value.getClass().getMethods()) {
            if (it.getName().equals("toDebugString")) {
                return value.getClass().getSimpleName() + ": Rendered by " + getClass().getSimpleName() + " with priority " + getPluginPriority();
            }
        }
        return null;
    }
}
