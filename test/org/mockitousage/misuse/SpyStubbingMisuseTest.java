/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;

public class SpyStubbingMisuseTest {

    @Test
    public void nestedWhenTest() {
        final Strategy mfoo = mock(Strategy.class);
        final Sampler mpoo = mock(Sampler.class);
        final Producer out = spy(new Producer(mfoo));

        try {
            when(out.produce()).thenReturn(mpoo);
            fail();
        } catch (final WrongTypeOfReturnValue e) {
            assertThat(e.getMessage()).contains("spy").contains("syntax").contains("doReturn|Throw");
        }
    }

    public class Sample { }

    public class Strategy {
        Sample getSample() {
            return new Sample();
        }
    }

    public class Sampler {
        Sample sample;
        Sampler(final Strategy f) {
            sample = f.getSample();
        }
    }

    public class Producer {
        Strategy strategy;
        Producer(final Strategy f) {
            strategy = f;
        }
        Sampler produce() {
            return new Sampler(strategy);
        }
    }
}
