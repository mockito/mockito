/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockitoutil.TestBase;

//see issue 221
public class NPEOnAnyClassMatcherAutounboxTest extends TestBase {

    interface Foo {
        void bar(final long id);
    }

    @Test
    public void shouldNotThrowNPE() {
        final Foo f = mock(Foo.class);
        f.bar(1);
        verify(f).bar(any(Long.class));
    }
}