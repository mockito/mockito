/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SpyAsDefaultMockUsageTest {

    @Test
    public void nestedWhenTest() {
        Strategy mfoo = mock(Strategy.class);
        Sampler mpoo = mock(Sampler.class);
        Producer out = spy(new Producer(mfoo));

        when(out.produce()).thenReturn(mpoo);
        assertSame(mpoo, out.produce());
    }

    public class Sample {}

    public class Strategy {
        Sample getSample() {
            return new Sample();
        }
    }

    public class Sampler {
        Sample sample;

        Sampler(Strategy f) {
            sample = f.getSample();
        }
    }

    public class Producer {
        Strategy strategy;

        Producer(Strategy f) {
            strategy = f;
        }

        Sampler produce() {
            return new Sampler(strategy);
        }
    }
}
