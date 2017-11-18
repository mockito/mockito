/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.function.Consumer;

public class OngoingStubbingConsumer<A> {
    private final Consumer<A> method;
    private LambdaArgumentMatcher<A> matcher;

    OngoingStubbingConsumer(Consumer<A> method) {
        this.method = method;
    }

    public OngoingStubbingConsumerWithArguments invokedWith(LambdaArgumentMatcher<A> matcher) {
        this.matcher = matcher;
        return new OngoingStubbingConsumerWithArguments();
    }

    public OngoingStubbingConsumerWithArguments invokedWith(A value) {
        this.matcher = new Equals<>(value);
        return new OngoingStubbingConsumerWithArguments();
    }

    public class OngoingStubbingConsumerWithArguments extends StubInProgress {
        @Override
        void invokeMethod() {
            try {
                method.accept(matcher.getValue());
            } catch (Exception ignored) {
                method.accept(matcher.constructObject());
            }
        }
    }
}
