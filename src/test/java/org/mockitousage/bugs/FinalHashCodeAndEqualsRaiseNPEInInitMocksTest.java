/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.Charset;

// issue 327
public class FinalHashCodeAndEqualsRaiseNPEInInitMocksTest {
    @Mock private Charset charset;
    @InjectMocks private FieldCharsetHolder fieldCharsetHolder;
    @InjectMocks private ConstructorCharsetHolder constructorCharsetHolder;

    @Test
    public void dont_raise_NullPointerException() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    private static class FieldCharsetHolder {
        private Charset charset;
    }

    private static class ConstructorCharsetHolder {
        public ConstructorCharsetHolder(Charset charset) {
        }
    }
}
