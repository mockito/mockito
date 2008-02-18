/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.exceptions.base.MockitoException;

@SuppressWarnings("unchecked")
public class AnnotationsTest extends TestBase {

    @Mock List list;
    @Mock final Map map = new HashMap();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldInitMocks() throws Exception {
        list.clear();
        map.clear();
        
        verify(list).clear();
        verify(map).clear();
    }
    
    @Test
    public void shouldScreamWhenInitializingMocksForNullClass() throws Exception {
        try {
            MockitoAnnotations.initMocks(null);
            fail();
        } catch (MockitoException e) {
            assertEquals("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations",
                    e.getMessage());
        }
    }
    
    @Test
    public void shouldLookForAnnotatedMocksInSuperClasses() throws Exception {
        Sub sub = new Sub();
        MockitoAnnotations.initMocks(sub);
        
        assertNotNull(sub.getMock());
        assertNotNull(sub.getBaseMock());
        assertNotNull(sub.getSuperBaseMock());
    }
    
    class SuperBase {
        @Mock private IMethods mock;
        
        public IMethods getSuperBaseMock() {
            return mock;
        }
    }
    
    class Base extends SuperBase {
        @Mock private IMethods mock;
        
        public IMethods getBaseMock() {
            return mock;
        }
    }
    
    class Base2 extends SuperBase {
        @Mock private IMethods mock;
        
        public IMethods getBaseMock() {
            return mock;
        }
    }
    
    class Sub extends Base {
        @Mock private IMethods mock;
        
        public IMethods getMock() {
            return mock;
        }
    }
}