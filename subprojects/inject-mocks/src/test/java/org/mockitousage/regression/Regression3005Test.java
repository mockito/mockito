/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verify regression https://github.com/mockito/mockito/issues/3005 is fixed.
 */
@ExtendWith(MockitoExtension.class)
public class Regression3005Test {

    @InjectMocks protected ConcreteJob job;
    @Mock JobInstance<?> instance;

    @Test
    public void test() {
        assertNotNull(job);
        assertNotNull(instance);
        assertTrue(MockUtil.isMock(instance));
        assertSame(instance, job.instance);
    }

    static class AbstractJob<A extends AbstractJob<A>> {
        JobInstance<A> instance;
    }

    static class JobInstance<J extends AbstractJob<J>> {}

    static class ConcreteJob extends AbstractJob<ConcreteJob> {}
}
