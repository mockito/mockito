/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.junitrunner;
import java.util.List;
import static org.mockito.Mockito.*;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.Mock;

@RunWith(MockitoRunner.class)
@SuppressWarnings("unchecked")
public class ExampleTest {
    
    @Mock private List list;
    @Mock private Map map;
    
    @Test
    public void shouldInitMocksUsingRunner() {
        list.add("test");
        map.clear();
        
        verify(list).add("test");
        verify(map).clear();
    }
}