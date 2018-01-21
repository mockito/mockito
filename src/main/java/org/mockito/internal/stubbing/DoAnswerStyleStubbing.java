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
    private Strictness stubbingStrictness;

    void setAnswers(List<Answer<?>> answers, Strictness stubbingStrictness) {
        this.stubbingStrictness = stubbingStrictness;
        this.answers.addAll(answers);
    }

    boolean isSet() {
        return answers.isEmpty();
    }

    void clear() {
        answers.clear();
        stubbingStrictness = null;
    }

    List<Answer<?>> getAnswers() {
        return answers;
    }

    Strictness getStubbingStrictness() {
        return stubbingStrictness;
    }
}
