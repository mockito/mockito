/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;

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
}