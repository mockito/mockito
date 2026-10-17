/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.valuerenderer;

import java.lang.reflect.Method;

import org.mockito.plugins.ValueRenderer;

public class MyDebugValueRenderer1 implements ValueRenderer {
    @Override
    public int getPluginPriority() {
        return DEFAULT_PRIORITY + 2;
    }

    @Override
    public String print(Object value) {
        if (value == null) {
            return null;
        }
        if (!value.getClass().getSimpleName().contains("Debug")) {
            // This is the only difference between MyDebugValueRenderer1 and MyDebugValueRenderer2
            return null;
        }
        for (Method it : value.getClass().getMethods()) {
            if (it.getName().equals("toDebugString")) {
                return value.getClass().getSimpleName()
                        + ": Rendered by "
                        + getClass().getSimpleName()
                        + " with priority "
                        + getPluginPriority();
            }
        }
        return null;
    }
}
