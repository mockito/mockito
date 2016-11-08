/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.quality;

/**
 * Starting with 2.1.0 of Mockito stubbing hints / warnings are printed to standard output.
 * Hints contain clickable links that take you right to the line of code that contains a possible problem.
 * Those are hints - they not necessarily indicate real problems 100% of the time.
 * This way the developer can:
 * <ol>
 *   <li>produce cleaner tests - by detecting and removing unused stubs</li>
 *   <li>understand why test fails - by detecting stubs that were ineffective due to argument mismatch</li>
 * </ol>
 * We would appreciate feedback about this feature so that we can make Mockito better!
 * Our goal is to provide maximum productivity when testing Java.
 * Join the discussion in the tracker for <a href="https://github.com/mockito/mockito/issues/384">issue 384</a>.
 * <p>
 * How to take advantage of the hints? Use one of the JUnit integrations:
 * <ul>
 *     <li>{@link org.mockito.junit.MockitoJUnit#rule()}</li>
 *     <li>{@link org.mockito.runners.MockitoJUnitRunner}</li>
 * </ul>
 * Currently, the feature is available with JUnit Rule and JUnit Runner only because
 * they provide necessary hooks (the 'before' and 'after' test events).
 * <p>
 * <h3>Cleaner tests without unnecessary stubs</h3>
 * Unnecessary stubs are stubbed method calls that were never realized during test execution.
 * To find out more and see the example test code, see {@link org.mockito.exceptions.misusing.UnnecessaryStubbingException}.
 *
 * <h3>Better failure diagnostics by detecting mismatched stubs</h3>
 *
 * When the test fails for a wrong reason, sometimes it's because stubbed method was called with incorrect argument(s).
 * In this scenario, the problem is not often obvious.
 * Hence, Mockito generates a hint to the standard output indicating this scenario.
 * Hint contains a clickable link to the line of code where the potential problem is.
 * <p>
 * Example:
 *
 * <p>
 * Let's say the test fails on assertion.
 * Let's say the underlying reason is a stubbed method that was called with different arguments:
 * <pre class="code"><code class="java">
 * //test:
 * Dictionary dictionary = new Dictionary(translator);
 * when(translator.translate("Mockito")).thenReturn("cool framework");
 * String translated = dictionary.search("Mockito");
 * assertEquals("cool framework", translated);
 *
 * //code:
 * public String search(String word) {
 *     ...
 *     return translator.translate("oups");
 *
 * </code></pre>
 * On standard output you'll see a hint with clickable links to both locations:
 * a) stubbing declaration and b) the method call on a stub with mismatched argument.
 * <p>
 * Note that it is just a warning, not an assertion.
 * The test fails on assertion because it is the assertion's duty
 * to document what the test stands for and what behavior it proves.
 * Hints just makes it quicker to figure out if the test fails for the right reason.
 * <p>
 * Feedback very welcome at <a href="https://github.com/mockito/mockito/issues/384">issue 384</a>.
 *
 * @since 2.1.0
 */
public interface MockitoHint {
}
