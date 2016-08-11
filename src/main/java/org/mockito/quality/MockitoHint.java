package org.mockito.quality;

/**
 * Starting with 2.* of Mockito stubbing warnings / hints are printed to standard output.
 * This way the developer will:
 * <ol>
 *   <li>produce cleaner tests - by detecting and removing unused stubs</li>
 *   <li>understand why test fails - by detecting stubs that were ineffective due to argument mismatch</li>
 * </ol>
 * We would appreciate feedback about this feature so that we can make Mockito better!
 * Our goal is to provide maximum productivity when testing Java.
 * Join the discussion in the issue tracker at <a href="https://github.com/mockito/mockito/issues/384">issue 384</a>.
 * <p>
 * How to take advantage of the hints? Use one of the JUnit integrations:
 * <ul>
 *     <li>{@link org.mockito.junit.MockitoJUnit#rule()}</li>
 *     <li>{@link org.mockito.runners.MockitoJUnitRunner}</li>
 * </ul>
 * Currently, the feature is available with JUnit Rule and Runner only because
 * they provide necessary hooks (the 'before' and 'after' test events).
 * <p>
 * <h3>Cleaner tests without unnecessary stubs</h3>
 * Unnecessary stubs are stubbed method calls that were never realized during test execution, example:
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
 * <h3>Better failure diagnostics by detecting mismatched stubs</h3>
 * TODO 384
 *
 * @since 2.*
 */
public interface MockitoHint {
}
