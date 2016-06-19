/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class MatchersMixedWithRawArgumentsTest extends TestBase {
    
    @Mock private IMethods mock;
  
    //description of an idea:
    //types of arguments and descriptor value that identifies matcher:
    //Object: objenesis instance to check for identity
    //boolean: false
    //byte: max-1
    //short: max-1
    //int: max-1
    //long: max-1
    //char: 'x'
    //double: max-1
    //float: max-1
    
    //1. how objenesis deal with primitive arrays (like byte[])?
    //2. Analisys of all matchers used by R2 project finished before anyObject() and so far proves it's a good idea.

    @Ignore("prototyping new feature that allows to avoid eq() matchers when raw args passed")
    @Test
    public void shouldAllowMixingRawArgumentsWithMatchers() {
        mock.varargs("1", "2", "3");
        verify(mock).varargs("1", anyString(), "3");
        
        verify(mock).varargs(anyBoolean(), false);
    }
}