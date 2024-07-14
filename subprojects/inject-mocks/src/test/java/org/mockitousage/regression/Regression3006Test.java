/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <a href="https://github.com/mockito/mockito/issues/3006">Issue #3006</a>
 */
@ExtendWith(MockitoExtension.class)
public class Regression3006Test {

    @InjectMocks protected ConcreteJob<JobData> job;

    @Mock JobInstance<JobData, ConcreteJob<JobData>> instance;

    @Test
    public void testMockExistsAndUsed() {
        assertNotNull(job);
        assertNotNull(instance);
        assertTrue(MockUtil.isMock(instance));
        // compiler allows job.instance = instance, and so does @InjectMocks
        assertEquals(instance, job.instance);
    }

    interface JobData {}

    static class AbstractJob<D extends JobData, A extends AbstractJob<D, A>> {
        JobInstance<D, A> instance;
    }

    static class JobInstance<D extends JobData, J extends AbstractJob<D, J>> {}

    static class ConcreteJob<D extends JobData> extends AbstractJob<D, ConcreteJob<D>> {}

    @Nested
    public class Regression3006ArrayIndexOutOfBounds {}
}
