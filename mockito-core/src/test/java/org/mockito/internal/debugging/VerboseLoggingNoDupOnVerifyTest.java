/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.junit.Test;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VerboseLoggingNoDupOnVerifyTest {

    static class CountingListener implements InvocationListener {
        int count = 0;

        @Override
        public void reportInvocation(MethodInvocationReport report) {
            count++;
        }
    }

    @Test
    public void verify_does_not_invoke_listeners_again() {
        CountingListener counter = new CountingListener();

        @SuppressWarnings("unchecked")
        List<String> list =
                mock(
                        List.class,
                        withSettings()
                                .invocationListeners(counter)
                                .verboseLogging()); // 保留 verbose 以覆盖原问题场景

        list.add("a"); // 真实调用 -> 监听器应触发一次
        int before = counter.count;

        verify(list).add("a"); // 验证阶段 -> 不应再触发监听器
        int after = counter.count;

        assertEquals("verify() must not add extra listener notifications", before, after);
    }
}
