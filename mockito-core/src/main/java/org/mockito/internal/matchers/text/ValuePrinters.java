/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.ValueRenderer;

@NullMarked
public final class ValuePrinters {
    private static final List<? extends ValueRenderer> RENDERERS;

    static {
        ArrayList<ValueRenderer> renderers = new ArrayList<>(Plugins.getValueRenderers());
        renderers.sort(Comparator.comparingInt(ValueRenderer::getPluginPriority).reversed());
        renderers.removeIf(it -> it instanceof DefaultValueRenderer);
        // The default renderer should always be last because it will always successfully render any input, so no renderer can follow it.
        // There is an argument this could be pulled from `DefaultMockitoPlugins`, but that would require `PluginLoader`
        // to be able to source a list of loaded plugins _and_ a default, which it currently does not do.
        renderers.add(DefaultValueRenderer.INSTANCE);
        RENDERERS = Collections.unmodifiableList(renderers);
    }

    /**
     * Prints given value so that it is neatly readable by humans.
     * Tries all {@link org.mockito.plugins.ValueRenderer} classes declared through the extension mechanism, and falls back to {@link DefaultValueRenderer}.
     *
     * @param value Any object to be rendered
     * @return A non-null string representing the object that can be read by humans.
     */
    public static String print(@Nullable final Object value) {
        if (value instanceof FormattedText) {
            return ((FormattedText) value).getText();
        }
        for (ValueRenderer valueRenderer : RENDERERS) {
            // TODO: given this is calling user-written code, should it be within a try-catch?  What would the failure look like other than just rethrowing?
            String output = valueRenderer.print(value);
            if (output != null) {
                return output;
            }
        }
        throw new IllegalStateException("No value printer returned a value.  This should never happen as the DefaultValuePrinter is always included in the list of renderers.");
    }

    /**
     * Print multiple values in a nice format, e.g. (1, 2, 3).  Values are printed by a {@link ValueRenderer}.
     *
     * @param start the beginning of the values, e.g. "("
     * @param separator the separator of values, e.g. ", "
     * @param end the end of the values, e.g. ")"
     * @param values the values to print
     *
     * @return neatly formatted value list
     */
    public static String printValues(@Nullable String start, @Nullable String separator, @Nullable String end, Iterator<? extends @Nullable Object> values) {
        if (start == null) {
            start = "(";
        }
        if (separator == null) {
            separator = ",";
        }
        if (end == null) {
            end = ")";
        }

        StringBuilder sb = new StringBuilder(start);
        while (values.hasNext()) {
            sb.append(print(values.next()));
            if (values.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.append(end).toString();
    }
}
