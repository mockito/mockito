/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

/**
 * Contains text that has already been formatted
 * and hence it does not need any formatting (like quotes around string, etc.)
 */
class FormattedText {

    private final String text;

    public FormattedText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
