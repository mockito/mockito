/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class PlaygroundTest extends TestBase {

    List list = new LinkedList();
    List spy = Mockito.spy(list);
    @Mock IMethods mock;
    
    @Test
    public void playWithSomething() {
        //Here you can play with mockito
    }
}