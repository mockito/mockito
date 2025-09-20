/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesFutureTest {

    interface ReturnFuture { Future<String> f(); }
    interface ReturnCompletableFuture { CompletableFuture<Void> v(); }
    interface ReturnCompletionStage { CompletionStage<Integer> s(); }

    @Test
    public void returnsCompletedFuture_forFuture() throws Exception {
        ReturnFuture m = mock(ReturnFuture.class);
        Future<String> fut = m.f();
        assertNotNull(fut);
        assertTrue(fut.isDone());
        assertNull(fut.get());
    }

    @Test
    public void returnsCompletedFuture_forCompletableFuture() {
        ReturnCompletableFuture m = mock(ReturnCompletableFuture.class);
        CompletableFuture<Void> fut = m.v();
        assertNotNull(fut);
        assertTrue(fut.isDone());
        assertDoesNotThrow(fut::join);
    }

    @Test
    public void returnsCompletedFuture_forCompletionStage() {
        ReturnCompletionStage m = mock(ReturnCompletionStage.class);
        CompletionStage<Integer> st = m.s();
        assertNotNull(st);
        assertTrue(st.toCompletableFuture().isDone());
        assertNull(st.toCompletableFuture().join());
    }
}
