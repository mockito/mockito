/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.mockito.Answers;

public class ReturnsDeepStubsConcurrentTest {

    @Test
    public void
            given_mock_with_returns_deep_stubs__when_called_concurrently__then_does_not_throw_concurrent_modification_exception() {
        for (int i = 0; i < 1000; i++) {
            Service mock = mock(Service.class, Answers.RETURNS_DEEP_STUBS);
            IntStream.range(1, 100).parallel().forEach(index -> mock.doSomething());
        }
    }

    interface Service {
        List<String> doSomething();
    }
}
