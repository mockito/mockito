package org.mockito.internal.invocation;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class MockitoMethodTest extends TestBase {

  @Test
  public void shouldBeSerializable() throws Exception {
    Class<?>[] args = new Class<?>[0];
    MockitoMethod method = new MockitoMethod(this.getClass().getMethod("toString", args));
    ByteArrayOutputStream serialized = new ByteArrayOutputStream();
    new ObjectOutputStream(serialized).writeObject(method);
  }
  
  @Test
  public void shouldBeAbleToRetrieveTheMethodInitializedWith() throws Exception {
    Class<?>[] args = new Class<?>[0];
    Method method = this.getClass().getMethod("toString", args);
    MockitoMethod mockMethod = new MockitoMethod(method);
    assertEquals(method, mockMethod.getMethod());
  }
}
