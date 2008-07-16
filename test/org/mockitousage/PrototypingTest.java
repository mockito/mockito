/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockitousage.examples.junitrunner.MockitoRunner;
import org.mockitoutil.TestBase;

@RunWith(MockitoRunner.class)
public class PrototypingTest extends TestBase {
    
    @Ignore
    @Test
    public void prototypingNewAPI() throws Exception {
//        List mock = mock(List.class);
//        stubVoid(mock).toThrow(new RuntimeException()).on().clear();
//        
//        when(mock.get(0)).thenReturn("afsdf");
//        
//        
//        when(mock.get(0))
//            .thenReturn("foo")
//            .thenReturn("bar")
//            .thenRaise(new RuntimeException());
//        
//        raise(new RuntimeException())
//            .thenRaise(new RuntimeException())
//            .thenReturn()
//            .when(mock).get(0);
//        
//        
//        
//        when(mock.get(0))
//            .thenReturn("foo")
//            .thenReturn("bar")
//            .thenThrow(new RuntimeException());
//    
//        doThrow(new RuntimeException())
//            .thenThrow(new RuntimeException())
//            .thenReturn()
//            .when(mock).get(0);
//
//        
//        raise(new RuntimeException()).when(mock).clear();
//        
//        raise(new RuntimeException())
//        .thenRaise(new RuntimeException())
//        .when(mock).clear();
//        
//        doReturn("two")
//        .thenReturn("one")
//        .when(mock).get(0);
    }
}