/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.internal.util.Decamelizer.decamelizeMatcher;

public class DecamelizerTest extends TestBase {
    
    @Test
    public void shouldProduceDecentDescription() throws Exception {
        assertEquals("<Sentence with strong language>", decamelizeMatcher("SentenceWithStrongLanguage"));
        assertEquals("<W e i r d o 1>", decamelizeMatcher("WEIRDO1"));
        assertEquals("<_>", decamelizeMatcher("_"));
        assertEquals("<Has exactly 3 elements>", decamelizeMatcher("HasExactly3Elements"));
        assertEquals("<custom argument matcher>", decamelizeMatcher(""));
    }
}