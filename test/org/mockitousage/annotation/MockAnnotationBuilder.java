package org.mockitousage.annotation;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.lang.annotation.Annotation;

public class MockAnnotationBuilder {

    private Answers answer=Answers.RETURNS_DEFAULTS;
    private Class<? extends Answer> customAnswerClass;

    public static MockAnnotationBuilder aMockAnnotation() {
        return new MockAnnotationBuilder();
    }

    public MockAnnotationBuilder withAnswer(Answers answer) {
        this.answer = answer;
        return this;
    }

    public MockAnnotationBuilder withCustomAnswer(Class<? extends Answer> customAnswerClass) {
        this.customAnswerClass = customAnswerClass;
        return this;
    }

    public Mock build() {
        return new Mock() {
            @Override
            public Answers answer() {
                return answer;
            }

            @Override
            public String name() {
                return null;
            }

            @Override
            public Class<?>[] extraInterfaces() {
                return new Class<?>[0];
            }

            @Override
            public boolean serializable() {
                return false;
            }

            @Override
            public Class<? extends Answer> customAnswer() {
                return customAnswerClass;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
    }
}
