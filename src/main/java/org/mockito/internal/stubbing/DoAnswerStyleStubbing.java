/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds answers declared using 'doAnswer' stubbing style.
 */
class DoAnswerStyleStubbing implements Serializable {

    private final List<Answer<?>> answers = new ArrayList<Answer<?>>();
    private Strictness strictness;

    void setAnswers(List<Answer<?>> answers, Strictness strictness) {
        this.strictness = strictness;
        this.answers.addAll(answers);
    }

    boolean isSet() {
        return answers.isEmpty();
    }

    void clear() {
        answers.clear();
        strictness = null;
    }

    List<Answer<?>> getAnswers() {
        return answers;
    }

    Strictness getStrictness() {
        return strictness;
    }
}
