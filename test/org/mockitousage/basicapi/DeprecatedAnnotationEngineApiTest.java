/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.After;
import org.junit.Test;
import org.mockito.InjectMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockitoutil.TestBase;

public class DeprecatedAnnotationEngineApiTest extends TestBase {

    @After
    public void goBackToDefaultConfiguration() {
        ConfigurationAccess.getConfig().overrideAnnotationEngine(null);
    }
    
    class SimpleTest {
        @InjectMock Tested tested = new Tested();
        @Mock Dependency mock;
    }
    
    class Tested {        
        Dependency dependency;
        public void setDependency(Dependency dependency) {
            this.dependency = dependency;
        }        
    }
    
    class Dependency {}
    
    @Test
    public void shouldInjectMocksIfThereIsNoUserDefinedEngine() throws Exception {
        //given
        AnnotationEngine defaultEngine = new DefaultMockitoConfiguration().getAnnotationEngine();
        ConfigurationAccess.getConfig().overrideAnnotationEngine(defaultEngine);
        SimpleTest test = new SimpleTest();
        
        //when
        MockitoAnnotations.initMocks(test);
        
        //then   
        assertNotNull(test.mock);
        assertNotNull(test.tested.dependency);
        assertSame(test.mock, test.tested.dependency);
    }
    
    @Test
    public void shouldRespectUsersEngine() throws Exception {
        //given
        AnnotationEngine customizedEngine = new DefaultAnnotationEngine() { /**/ };
        ConfigurationAccess.getConfig().overrideAnnotationEngine(customizedEngine);
        SimpleTest test = new SimpleTest();
        
        //when
        MockitoAnnotations.initMocks(test);
        
        //then   
        assertNotNull(test.mock);
        assertNull(test.tested.dependency);
    }
}