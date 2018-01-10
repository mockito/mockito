/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class AnyInt implements LambdaArgumentMatcher<Integer> {
    @Override
    public boolean matches(Integer argument) {
        return true;
    }

    @Override
    public Integer getValue() {
        return 0;
    }
}
