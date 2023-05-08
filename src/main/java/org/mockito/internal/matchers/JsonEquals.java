/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import jakarta.json.Json;
import jakarta.json.JsonStructure;
import jakarta.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import org.mockito.ArgumentMatcher;

public class JsonEquals implements ArgumentMatcher<String>, Serializable {

    private static final Map<String, Object> OPTIONS = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
    private final String value;
    private final JsonStructure json;

    public JsonEquals(final String value) {
        if (value == null) throw new IllegalArgumentException("string value is null");
        this.value = value;
        this.json = Json.createReader(new StringReader(value)).read();
    }

    @Override
    public boolean matches(String arg) {
        final JsonStructure argJson = Json.createReader(new StringReader(arg)).read();

        return json.equals(argJson);
    }

    @Override
    public String toString() {
        try (final StringWriter writer = new StringWriter()) {
            Json.createWriterFactory(OPTIONS)
                .createWriter(writer)
                .write(json);
            return writer.toString();
        } catch (final IOException e) {
            e.printStackTrace();
            return value;
        }
    }
}
