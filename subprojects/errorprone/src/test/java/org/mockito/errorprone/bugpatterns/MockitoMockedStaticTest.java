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

@RunWith(JUnit4.class)
public class MockitoMockedStaticTest {

    private CompilationTestHelper compilationTestHelper;

    @Before
    public void setup() {
        compilationTestHelper =
            CompilationTestHelper.newInstance(MockitoMockedStatic.class, getClass());
    }

    @Test
    public void mockedStaticWithMockField_shouldError() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.Mock;",
                "import org.mockito.MockedStatic;",
                "import org.mockito.Mockito;",
                "class Test {",
                "  @Mock",
                "  private Object mock;",
                "  public void test() {",
                "    try (MockedStatic<Object> mockedStatic = Mockito.mockStatic(Object.class)) {",
                "      // Do something with the mock",
                "    }",
                "  }",
                "}")
            .doTest();
    }

    @Test
    public void mockedStaticWithMockParameter_shouldError() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.Mock;",
                "import org.mockito.MockedStatic;",
                "import org.mockito.Mockito;",
                "class Test {",
                "  public void test(@Mock Object mock) {",
                "    try (MockedStatic<Object> mockedStatic = Mockito.mockStatic(Object.class)) {",
                "      // Do something with the mock",
                "    }",
                "  }",
                "}")
            .doTest();
    }

    @Test
    public void mockedStaticWithoutMock_shouldNotError() {
        compilationTestHelper
            .addSourceLines(
                "input.java",
                "import org.mockito.MockedStatic;",
                "import org.mockito.Mockito;",
                "class Test {",
                "  public void test() {",
                "    try (MockedStatic<Object> mockedStatic = Mockito.mockStatic(Object.class)) {",
                "      // Do something without a mock",
                "    }",
                "  }",
                "}")
            .doTest();
    }
}
