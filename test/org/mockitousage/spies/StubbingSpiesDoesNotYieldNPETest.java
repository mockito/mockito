/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("rawtypes")
public class StubbingSpiesDoesNotYieldNPETest extends TestBase {
    
    class Foo {
        public int len(final String text) {
            return text.length();
        }
        
        public int size(final Map map) {
            return map.size();
        }
        
        public int size(final Collection collection) {
            return collection.size();
        }
    }
    
    @Test
    public void shouldNotThrowNPE() throws Exception {
        final Foo foo = new Foo();
        final Foo spy = spy(foo);
        
        spy.len(anyString());
        spy.size(anyMap());
        spy.size(anyList());
        spy.size(anyCollection());
        spy.size(anySet());
    }
}