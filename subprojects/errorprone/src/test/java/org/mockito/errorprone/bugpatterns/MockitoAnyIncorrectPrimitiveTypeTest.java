/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Verify that there are no Mockito `any(Class)` matchers for arguments that are primitives.
 *
 * @author tvanderlippe@google.com (Tim van der Lippe)
 */
@RunWith(JUnit4.class)
public class MockitoAnyIncorrectPrimitiveTypeTest {

  private CompilationTestHelper compilationHelper;
  private BugCheckerRefactoringTestHelper refactoringHelper;

  @Before
  public void setUp() {
    compilationHelper =
        CompilationTestHelper.newInstance(MockitoAnyIncorrectPrimitiveType.class, getClass());
    refactoringHelper =
        BugCheckerRefactoringTestHelper.newInstance(
            new MockitoAnyIncorrectPrimitiveType(), getClass());
  }

  @Test
  public void testPositiveCases() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "package org.mockito;",
            "import static org.mockito.ArgumentMatchers.any;",
            "import static org.mockito.ArgumentMatchers.anyInt;",
            "import static org.mockito.Mockito.mock;",
            "import static org.mockito.Mockito.when;",
            "class Test {",
            "  public void test() {",
            "    Foo foo = mock(Foo.class);",
            "    // BUG: Diagnostic contains:",
            "    when(foo.run(anyInt())).thenReturn(5);",
            "    // BUG: Diagnostic contains:",
            "    when(foo.runWithBoth(any(String.class), anyInt())).thenReturn(5);",
            "  }",
            "  static class Foo {",
            "    int run(long arg) {",
            "      return 42;",
            "    }",
            "    int runWithBoth(String arg1, long arg2) {",
            "      return 42;",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void testNegativeCases() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "package org.mockito;",
            "import static org.mockito.ArgumentMatchers.any;",
            "import static org.mockito.ArgumentMatchers.anyFloat;",
            "import static org.mockito.ArgumentMatchers.anyLong;",
            "import static org.mockito.Mockito.mock;",
            "import static org.mockito.Mockito.when;",
            "class Test {",
            "  public void test() {",
            "    Foo foo = mock(Foo.class);",
            "    when(foo.runWithInt(anyFloat())).thenReturn(5);",
            "    when(foo.runWithBoth(any(String.class), anyLong())).thenReturn(5);",
            "  }",
            "  static class Foo {",
            "    int run(String arg) {",
            "      return 42;",
            "    }",
            "    int runWithInt(float arg) {",
            "      return 42;",
            "    }",
            "    int runWithBoth(String arg1, long arg2) {",
            "      return 42;",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void testPositivesSubclass() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "package org.mockito;",
            "import static org.mockito.Mockito.mock;",
            "import static org.mockito.Mockito.when;",
            "import static org.mockito.Mockito.any;",
            "import static org.mockito.Mockito.anyInt;",
            "class Test {",
            "  public void test() {",
            "    Foo foo = mock(Foo.class);",
            "    // BUG: Diagnostic contains:",
            "    when(foo.run(anyInt())).thenReturn(5);",
            "    // BUG: Diagnostic contains:",
            "    when(foo.runWithBoth(any(String.class), anyInt())).thenReturn(5);",
            "  }",
            "  static class Foo {",
            "    int run(long arg) {",
            "      return 42;",
            "    }",
            "    int runWithBoth(String arg1, float arg2) {",
            "      return 42;",
            "    }",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void testRefactoring() {
    refactoringHelper
        .addInputLines(
            "Test.java",
            "import static org.mockito.ArgumentMatchers.any;",
            "import static org.mockito.ArgumentMatchers.anyInt;",
            "import static org.mockito.Mockito.mock;",
            "import static org.mockito.Mockito.when;",
            "class Test {",
            "  public void test() {",
            "    Foo foo = mock(Foo.class);",
            "    when(foo.run(anyInt())).thenReturn(5);",
            "    when(foo.runWithBoth(any(String.class), anyInt())).thenReturn(5);",
            "  }",
            "  static class Foo {",
            "    int run(float arg) {",
            "      return 42;",
            "    }",
            "    int runWithBoth(String arg1, long arg2) {",
            "      return 42;",
            "    }",
            "  }",
            "}")
        .addOutputLines(
            "Test.java",
            "import static org.mockito.ArgumentMatchers.any;",
            "import static org.mockito.ArgumentMatchers.anyFloat;",
            "import static org.mockito.ArgumentMatchers.anyInt;",
            "import static org.mockito.ArgumentMatchers.anyLong;",
            "import static org.mockito.Mockito.mock;",
            "import static org.mockito.Mockito.when;",
            "class Test {",
            "  public void test() {",
            "    Foo foo = mock(Foo.class);",
            "    when(foo.run(anyFloat())).thenReturn(5);",
            "    when(foo.runWithBoth(any(String.class), anyLong())).thenReturn(5);",
            "  }",
            "  static class Foo {",
            "    int run(float arg) {",
            "      return 42;",
            "    }",
            "    int runWithBoth(String arg1, long arg2) {",
            "      return 42;",
            "    }",
            "  }",
            "}")
        .doTest();
  }
}
