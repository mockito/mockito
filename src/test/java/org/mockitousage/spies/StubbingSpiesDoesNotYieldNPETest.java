/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.util.Collection;
import java.util.Map;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.spy;

public class StubbingSpiesDoesNotYieldNPETest extends TestBase {
    
    class Foo {
        public int len(String text) {
            return text.length();
        }
        
        public int size(Map<?, ?> map) {
            return map.size();
        }
        
        public int size(Collection<?> collection) {
            return collection.size();
        }
    }
    
    @Test
    public void shouldNotThrowNPE() throws Exception {
        Foo foo = new Foo();
        Foo spy = spy(foo);
        
        spy.len(anyString());
        spy.size(anyMap());
        spy.size(anyList());
        spy.size(anyCollection());
        spy.size(anySet());
    }
}