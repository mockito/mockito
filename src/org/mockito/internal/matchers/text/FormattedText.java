package org.mockito.internal.matchers.text;

/**
 * Contains text that has already been formatted.
 */
class FormattedText {

    private String text;

    public FormattedText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
