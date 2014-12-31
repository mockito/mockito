package org.mockitousage.bugs.deepstubs;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class DeepStubFailingWhenGenricNestedAsRawTypeTest {

  interface MyClass1<MC2 extends MyClass2> {
    MC2 getNested();
  }

  interface MyClass2<MC3 extends MyClass3> {
    MC3 getNested();
  }

  interface MyClass3 {
    String returnSomething();
  }

  @Test
  public void discoverDeepMockingOfGenerics() {
    MyClass1 myMock1 = mock(MyClass1.class, RETURNS_DEEP_STUBS);
    when(myMock1.getNested().getNested().returnSomething()).thenReturn("Hello World.");
  }
}
