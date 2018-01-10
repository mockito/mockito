/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class AnyLong implements LambdaArgumentMatcher<Long> {
    @Override
    public Long getValue() {
        return 0L;
    }

    @Override
    public boolean matches(Long argument) {
        return true;
    }
}
