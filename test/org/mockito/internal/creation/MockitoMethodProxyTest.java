package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockitoutil.TestBase;
import org.powermock.reflect.Whitebox;


public class MockitoMethodProxyTest extends TestBase {

  @Test
  public void shouldCreateWithMethodProxy() throws Exception {
    MethodProxy proxy = MethodProxy.create(String.class, String.class, "", "toString", "toString");
    MockitoMethodProxy mockitoMethodProxy = new MockitoMethodProxy(proxy);
    assertNotNull( mockitoMethodProxy.getMethodProxy());
  }
  
  @Test
  public void shouldCreateCorrectCreationInfo() throws Exception {
    //given
    MethodProxy proxy = MethodProxy.create(String.class, Integer.class, "", "", "");
    MockitoMethodProxy mockitoMethodProxy = new MockitoMethodProxy(proxy);
    
    //when
    Object methodProxy = mockitoMethodProxy.getMethodProxy();
   
    //then
    Object info = Whitebox.getInternalState(methodProxy, "createInfo");
    assertEquals(String.class, Whitebox.getInternalState(info, "c1"));
    assertEquals(Integer.class, Whitebox.getInternalState(info, "c2"));
  }
  
  @Test
  public void shouldCreateCorrectSignatures() throws Exception {
    //given
    MethodProxy proxy = MethodProxy.create(String.class, Integer.class, "a", "b", "c");
    MockitoMethodProxy mockitoMethodProxy = new MockitoMethodProxy(proxy);
    
    //when
    MethodProxy methodProxy = (MethodProxy) mockitoMethodProxy.getMethodProxy();
    
    //then
    assertEquals("a", methodProxy.getSignature().getDescriptor());
    assertEquals("b", methodProxy.getSignature().getName());
    assertEquals("c", methodProxy.getSuperName());
  }
  
}
