package org.mockitousage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

@RunWith(MockitoJUnitRunner.class)
public class TestClassWithoutMockitPluginAnnotation extends TestBase {
    
    @InjectMocks
    private ClassToBeInjectedWithMocks classToBeInjectedWithMocks;
    
    @Test
    public void ensure_class_is_instantiated_with_a_real_instance() throws Exception {
        assertTrue(classToBeInjectedWithMocks.getClass().equals(ClassToBeInjectedWithMocks.class));
    }
    
    static class ClassToBeInjectedWithMocks {
    }

}