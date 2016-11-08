/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

/**
 * Unnecessary stubs are stubbed method calls that were never realized during test execution
 * (see also {@link org.mockito.quality.MockitoHint}), example:
 * <pre class="code"><code class="java">
 * //code under test:
 * ...
 * String result = translator.translate("one")
 * ...
 *
 * //test:
 * ...
 * when(translator.translate("one")).thenReturn("jeden"); // <- stubbing realized during code execution
 * when(translator.translate("two")).thenReturn("dwa"); // <- stubbing never realized
 * ...
 * </pre>
 * Notice that one of the stubbed methods were never realized in the code under test, during test execution.
 * The stray stubbing might be an oversight of the developer, the artifact of copy-paste
 * or the effect not understanding the test/code.
 * Either way, the developer ends up with unnecessary test code.
 * In order to keep the codebase clean & maintainable it is necessary to remove unnecessary code.
 * Otherwise tests are harder to read and reason about.
 * <p>
 * To find out more about detecting unused stubbings see {@link org.mockito.quality.MockitoHint}.
 */
public class UnnecessaryStubbingException extends RuntimeException {
    public UnnecessaryStubbingException(String message) {
        super(message);
    }
}
