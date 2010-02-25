package org.mockitousage.annotation;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMock;
import org.mockito.Spy;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class SpyInjectionTest extends TestBase {

    @Spy List spy = new LinkedList();
    @InjectMock HasSpy hasSpy = new HasSpy();
    
    static class HasSpy {
        private List spy;
        public void setSpy(List spy) {
            this.spy = spy;
        }        
    }
    
	@Test
    public void shouldDoStuff() throws Exception {
        isMock(hasSpy.spy);
    }
}