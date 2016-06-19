/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SpyStubbingMisuseTest {

    @Test
    public void nestedWhenTest() {
        Strategy mfoo = mock(Strategy.class);
        Sampler mpoo = mock(Sampler.class);
        Producer out = spy(new Producer(mfoo));

        try {
            when(out.produce()).thenReturn(mpoo);
            fail();
        } catch (WrongTypeOfReturnValue e) {
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
