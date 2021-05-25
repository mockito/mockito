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

/** Verifies that there are no subclasses of {@link org.mockito.NotExtensible} Mockito types. */
@RunWith(JUnit4.class)
public class MockitoNotExtensibleTest {

    private CompilationTestHelper compilationTestHelper;

    @Before
    public void setup() {
        compilationTestHelper =
            CompilationTestHelper.newInstance(MockitoNotExtensible.class, getClass());
    }

    @Test
    public void notExtensibleSubclass_shouldWarn() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.invocation.InvocationOnMock;",
                "// BUG: Diagnostic contains:",
                "abstract class Invocation implements InvocationOnMock { }")
            .doTest();
    }

    @Test
    public void anonymousClassImplementingNotExtensible_shouldWarn() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.NotExtensible;",
                "class Test {",
                "  public void test() {",
                "    // BUG: Diagnostic contains:",
                "    new Anonymous() { };",
                "  }",
                "  @NotExtensible",
                "  interface Anonymous {}",
                "}")
            .doTest();
    }

    @Test
    public void anonymousClassImplementingOtherAnnotation_shouldNotWarn() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.NotExtensible;",
                "class Test {",
                "  public void test() {",
                "    new Anonymous() { };",
                "  }",
                "  @OtherAnnotation",
                "  interface Anonymous {}",
                "  @interface OtherAnnotation {}",
                "}")
            .doTest();
    }
}
