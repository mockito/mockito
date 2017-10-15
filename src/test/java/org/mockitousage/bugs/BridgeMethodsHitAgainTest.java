/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

import java.io.Serializable;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

//see issue 101
public class BridgeMethodsHitAgainTest extends TestBase {

  public interface Factory {}
  public interface ExtendedFactory extends Factory {}

  public interface SomeInterface {
    Factory factory();
  }

  public interface SomeSubInterface extends SomeInterface {
    ExtendedFactory factory();
  }

  public interface Base<T extends Serializable> {
    int test(T value);
  }

  public interface Extended extends Base<String> {
    @Override
    int test(String value);
  }

  @Mock SomeSubInterface someSubInterface;
  @Mock ExtendedFactory extendedFactory;

  @Test
  public void basicCheck() {
    Mockito.when((someSubInterface).factory()).thenReturn(extendedFactory);
    SomeInterface si = someSubInterface;
    assertTrue(si.factory() != null);
  }

  @Test
  public void checkWithExtraCast() {
    Mockito.when(((SomeInterface) someSubInterface).factory()).thenReturn(extendedFactory);
    SomeInterface si = someSubInterface;
    assertTrue(si.factory() != null);
  }

    @Test
    public void testBridgeInvocationIsRecordedForInterceptedMethod() {
        Extended ext = mock(Extended.class);
        ext.test("123");
        verify(ext).test("123");
        ((Base<String>) ext).test("456");
        verify(ext).test("456");
    }
}
