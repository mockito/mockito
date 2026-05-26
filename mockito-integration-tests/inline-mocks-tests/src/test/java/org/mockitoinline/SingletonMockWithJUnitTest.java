/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockedSingleton;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

// Regression test for issue #3826
public class SingletonMockWithJUnitTest {

    public enum MyEnum {
        A
    }

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void mockSingleton_with_MockitoJUnit_should_not_throw_NotAMockException() {
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(MyEnum.A)) {}
    }
}
