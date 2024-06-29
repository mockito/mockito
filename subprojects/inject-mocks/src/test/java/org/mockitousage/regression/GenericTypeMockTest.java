/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.regression;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that verify Mockito can discern mocks by generic types, so if there are multiple mock candidates
 * with the same generic type but different type parameters available for injection into a given field,
 * Mockito won't fail to inject (even if mock field name doesn't match under test's field name).
 */
@ExtendWith(MockitoExtension.class)
public class GenericTypeMockTest {

    static class Regression3005Classes {

        static class AbstractJob<A extends AbstractJob<A>> {
            JobInstance<A> instance;
        }

        static class JobInstance<J extends AbstractJob<J>> {}

        static class ConcreteJob extends AbstractJob<ConcreteJob> {}
    }

    /**
     * Verify regression https://github.com/mockito/mockito/issues/3005 is fixed.
     */
    @Nested
    public class RegressionClassCastExceptionWithWildcards {

        @InjectMocks
        protected Regression3005Classes.ConcreteJob job;
        @Mock
        Regression3005Classes.JobInstance<?> instance;

        @Test
        public void testNoClassCastException() {
            assertNotNull(job);
            assertNotNull(instance);
            assertTrue(MockUtil.isMock(instance));
            // instance cannot (or should not) be assigned to job.instance according to compiler:
            // Incompatible types. Found: 'JobInstance<capture<?>>', required: 'JobInstance<ConcreteJob>'
            assertNull(job.instance);
        }
    }

    static class Regression3006Classes {

        interface JobData {}

        static class AbstractJob<D extends JobData, A extends AbstractJob<D, A>> {
            JobInstance<D,A> instance;
        }

        static class JobInstance<D extends JobData, J extends AbstractJob<D, J>> {}

        static class ConcreteJob<D extends JobData> extends AbstractJob<D,ConcreteJob<D>> {}
    }

    /**
     * Verify regression https://github.com/mockito/mockito/issues/3006 is fixed.
     */
    @Nested
    public class Regression3006ArrayIndexOutOfBounds {

        @InjectMocks
        protected Regression3006Classes.ConcreteJob<Regression3006Classes.JobData> job;

        @Mock
        Regression3006Classes.JobInstance<
            Regression3006Classes.JobData,
            Regression3006Classes.ConcreteJob<Regression3006Classes.JobData>
            > instance;

        @Test
        public void testMockExistsAndUsed() {
            assertNotNull(job);
            assertNotNull(instance);
            assertTrue(MockUtil.isMock(instance));
            // compiler allows job.instance = instance, and so does @InjectMocks
            assertEquals(instance, job.instance);
        }
    }

    static class Regression3019Classes {

        static class Something {}

        static class ParameterizedInjectedObject<T extends Something> {
            public void init() {}
        }

        static class AbstractGenericClass<T extends Something> {

            ParameterizedInjectedObject<T> object;

            public void init() {
                object.init();
            }
        }

        static class EntryPoint extends AbstractGenericClass<Something> {

            @Override
            public void init() {
                super.init();
                // do other things ...
            }
        }
    }

    /**
     * Verify regression https://github.com/mockito/mockito/issues/3019 is fixed.
     */
    @Nested
    public class Regression3019MissingInjection {

        @Mock
        private Regression3019Classes.ParameterizedInjectedObject<Regression3019Classes.Something> injected;

        @InjectMocks
        private Regression3019Classes.EntryPoint subject;

        @Test
        public void testSuccessfullyInjected() {
            assertNotNull(injected);
            assertTrue(MockUtil.isMock(injected));
            assertNotNull(subject);
            assertNotNull(subject.object);
            // test it does not throw NPE
            subject.init();
            Mockito.verify(injected).init();
        }
    }

}

