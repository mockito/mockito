/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.misusing;

import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;

/**
 * This exception indicates presence of unused stubbings.
 * It is highly recommended to remove unused stubbings to keep the codebase clean.
 * In a rare scenario that unused stubbing is a false negative you can opt out from the validation via
 * (in order of ascending scope):
 * <ol>
 *     <li>Declaring specific stubbing as 'lenient' - {@link Mockito#lenient()}</li>
 *     <li>Declaring specific mock as 'lenient' - {@link org.mockito.MockSettings#lenient()}</li>
 *     <li>Declaring all mocks in given test class or test method mock as 'lenient' with
 *          our JUnit support ({@link org.mockito.junit.MockitoJUnit}) or Mockito session ({@link MockitoSession})</li>
 * </ol>
 *
 * <p>
 * Unnecessary stubbings are stubbed method calls that were never realized during test execution. Example:
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
 * Mockito JUnit Runner triggers <code>UnnecessaryStubbingException</code> only when none of the test methods use the stubbings.
 * This means that it is ok to put default stubbing in a 'setup' method or in test class constructor.
 * That default stubbing needs to be used at least once by one of the test methods.
 */
public class UnnecessaryStubbingException extends MockitoException {
    public UnnecessaryStubbingException(String message) {
        super(message);
    }
}
