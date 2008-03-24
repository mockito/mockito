/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.Decamelizer.*;

import org.junit.Test;

@SuppressWarnings("unchecked")
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