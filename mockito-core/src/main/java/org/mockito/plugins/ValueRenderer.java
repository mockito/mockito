/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Prints a Java object value in a way humans can read it neatly.
 * Used for printing arguments in verification errors.
 *
 * @since 5.25.0
 */
@NullMarked
public interface ValueRenderer {
    int DEFAULT_PRIORITY = 0;

    /**
     * Determines the order in which renders will be tried, if multiple renderers are available.
     * A higher number will be tried first.  Renderers with the same priority will be ordered be classname.
     * <p>
     * The default renderer {@link org.mockito.internal.matchers.text.DefaultValueRenderer} will always be
     * tried last, as it can always render any object.
     *
     * @return a number indicating the priority of this renderer.
     */
    default int getPluginPriority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * Prints given value so that it is neatly readable by humans.
     * Must handle explosive toString() implementations.
     * If a renderer implementation determines it cannot render this object, it indicates this by returning null.
     *
     * @return A string rendering of {@code value}, or {@code null} if the current renderer does not support this type of value.
     */
    @Nullable String print(@Nullable final Object value);
    // TODO: @Nullable is from jspecify - putting it on a plugin forces jspecify to be
    //  an api dependency - is this desirable / is there a better way of annotating
    //  this nullability?
}
