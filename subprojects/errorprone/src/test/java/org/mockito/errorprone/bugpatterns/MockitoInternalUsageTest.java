/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import com.google.errorprone.CompilationTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Verify that there are no usages of Mockito internal implementations.
 *
 * @author tvanderlippe@google.com (Tim van der Lippe)
 */
@RunWith(JUnit4.class)
public class MockitoInternalUsageTest {

  private CompilationTestHelper compilationHelper;

  @Before
  public void setUp() {
    compilationHelper = CompilationTestHelper.newInstance(MockitoInternalUsage.class, getClass());
  }

  @Test
  public void testPositiveCases() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "import java.io.Serializable;",
            "class MockitoInternalUsagePositiveCases {",
            "  public void newObject() {",
            "    // BUG: Diagnostic contains:",
            "    new org.mockito.internal.MockitoCore();",
            "    // BUG: Diagnostic contains:",
            "    new InternalConsumer(new org.mockito.internal.MockitoCore());",
            "  }",
            "  public void staticMethodInvocation() {",
            "    // BUG: Diagnostic contains:",
            "    org.mockito.internal.configuration.GlobalConfiguration.validate();",
            "  }",
            "  public void variableTypeDeclaration() {",
            "    // BUG: Diagnostic contains:",
            "    org.mockito.internal.stubbing.InvocationContainerImpl container = null;",
            "  }",
            "  // BUG: Diagnostic contains:",
            "  class ExtendsClause extends org.mockito.internal.MockitoCore {}",
            "  // BUG: Diagnostic contains:",
            "  abstract class ImplementsClause implements"
                + " org.mockito.internal.junit.MockitoTestListener {}",
            "  abstract class SecondImplementsClause",
            "      // BUG: Diagnostic contains:",
            "      implements Serializable, org.mockito.internal.junit.MockitoTestListener {}",
            "  // BUG: Diagnostic contains:",
            "  class ExtendsGeneric<T extends org.mockito.internal.stubbing.InvocationContainerImpl>"
                + " {}",
            "  // BUG: Diagnostic contains:",
            "  class SecondExtendsGeneric<R, T extends"
                + " org.mockito.internal.stubbing.InvocationContainerImpl> {}",
            "  class FieldClause {",
            "    // BUG: Diagnostic contains:",
            "    org.mockito.internal.MockitoCore core;",
            "  }",
            "  class MethodArgumentClause {",
            "    // BUG: Diagnostic contains:",
            "    public void methodArgument(org.mockito.internal.MockitoCore core) {}",
            "  }",
            "  static class InternalConsumer {",
            "    // BUG: Diagnostic contains:",
            "    InternalConsumer(org.mockito.internal.MockitoCore core) {}",
            "  }",
            "}")
        .doTest();
  }

  @Test
  public void testPositiveImportsCases() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "package foo;",
            "// BUG: Diagnostic contains:",
            "import org.mockito.internal.MockitoCore;",
            "// BUG: Diagnostic contains:",
            "import org.mockito.internal.stubbing.InvocationContainerImpl;",
            "// BUG: Diagnostic contains:",
            "import org.mockito.internal.*;",
            "class Test {}")
        .doTest();
  }

  @Test
  public void testNegativeCases() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "package org.mockito;",
            "import org.mockito.internal.MockitoCore;",
            "class Mockito {}")
        .doTest();
  }
}
