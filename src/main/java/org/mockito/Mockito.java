/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.debugging.MockitoDebuggerImpl;
import org.mockito.internal.framework.DefaultMockitoFramework;
import org.mockito.internal.session.DefaultMockitoSessionBuilder;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.invocation.MockHandler;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.quality.MockitoHint;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;
import org.mockito.session.MockitoSessionLogger;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer1;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.Stubbing;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.verification.After;
import org.mockito.verification.Timeout;
import org.mockito.verification.VerificationAfterDelay;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationWithTimeout;

/**
 * <p align="left"><img src="logo.png" srcset="logo@2x.png 2x" alt="Mockito logo"/></p>
 * The Mockito library enables mock creation, verification and stubbing.
 *
 * <p>
 * This javadoc content is also available on the <a href="http://mockito.org">http://mockito.org</a> web page.
 * All documentation is kept in javadocs because it guarantees consistency between what's on the web and what's in the source code.
 * It allows access to documentation straight from the IDE even if you work offline.
 * It motivates Mockito developers to keep documentation up-to-date with the code that they write,
 * every day, with every commit.
 *
 * <h1>Contents</h1>
 *
 * <b>
 *      <a href="#0">0. Migrating to Mockito 2</a><br/>
 *      <a href="#0.1">0.1 Mockito Android support</a></br/>
 *      <a href="#0.2">0.2 Configuration-free inline mock making</a></br/>
 *      <a href="#1">1. Let's verify some behaviour! </a><br/>
 *      <a href="#2">2. How about some stubbing? </a><br/>
 *      <a href="#3">3. Argument matchers </a><br/>
 *      <a href="#4">4. Verifying exact number of invocations / at least once / never </a><br/>
 *      <a href="#5">5. Stubbing void methods with exceptions </a><br/>
 *      <a href="#6">6. Verification in order </a><br/>
 *      <a href="#7">7. Making sure interaction(s) never happened on mock </a><br/>
 *      <a href="#8">8. Finding redundant invocations </a><br/>
 *      <a href="#9">9. Shorthand for mocks creation - <code>&#064;Mock</code> annotation </a><br/>
 *      <a href="#10">10. Stubbing consecutive calls (iterator-style stubbing) </a><br/>
 *      <a href="#11">11. Stubbing with callbacks </a><br/>
 *      <a href="#12">12. <code>doReturn()</code>|<code>doThrow()</code>|<code>doAnswer()</code>|<code>doNothing()</code>|<code>doCallRealMethod()</code> family of methods</a><br/>
 *      <a href="#13">13. Spying on real objects </a><br/>
 *      <a href="#14">14. Changing default return values of unstubbed invocations (Since 1.7) </a><br/>
 *      <a href="#15">15. Capturing arguments for further assertions (Since 1.8.0) </a><br/>
 *      <a href="#16">16. Real partial mocks (Since 1.8.0) </a><br/>
 *      <a href="#17">17. Resetting mocks (Since 1.8.0) </a><br/>
 *      <a href="#18">18. Troubleshooting & validating framework usage (Since 1.8.0) </a><br/>
 *      <a href="#19">19. Aliases for behavior driven development (Since 1.8.0) </a><br/>
 *      <a href="#20">20. Serializable mocks (Since 1.8.1) </a><br/>
 *      <a href="#21">21. New annotations: <code>&#064;Captor</code>, <code>&#064;Spy</code>, <code>&#064;InjectMocks</code> (Since 1.8.3) </a><br/>
 *      <a href="#22">22. Verification with timeout (Since 1.8.5) </a><br/>
 *      <a href="#23">23. Automatic instantiation of <code>&#064;Spies</code>, <code>&#064;InjectMocks</code> and constructor injection goodness (Since 1.9.0)</a><br/>
 *      <a href="#24">24. One-liner stubs (Since 1.9.0)</a><br/>
 *      <a href="#25">25. Verification ignoring stubs (Since 1.9.0)</a><br/>
 *      <a href="#26">26. Mocking details (Improved in 2.2.x)</a><br/>
 *      <a href="#27">27. Delegate calls to real instance (Since 1.9.5)</a><br/>
 *      <a href="#28">28. <code>MockMaker</code> API (Since 1.9.5)</a><br/>
 *      <a href="#29">29. BDD style verification (Since 1.10.0)</a><br/>
 *      <a href="#30">30. Spying or mocking abstract classes (Since 1.10.12, further enhanced in 2.7.13 and 2.7.14)</a><br/>
 *      <a href="#31">31. Mockito mocks can be <em>serialized</em> / <em>deserialized</em> across classloaders (Since 1.10.0)</a></h3><br/>
 *      <a href="#32">32. Better generic support with deep stubs (Since 1.10.0)</a></h3><br/>
 *      <a href="#33">33. Mockito JUnit rule (Since 1.10.17)</a><br/>
 *      <a href="#34">34. Switch <em>on</em> or <em>off</em> plugins (Since 1.10.15)</a><br/>
 *      <a href="#35">35. Custom verification failure message (Since 2.1.0)</a><br/>
 *      <a href="#36">36. Java 8 Lambda Matcher Support (Since 2.1.0)</a><br/>
 *      <a href="#37">37. Java 8 Custom Answer Support (Since 2.1.0)</a><br/>
 *      <a href="#38">38. Meta data and generic type retention (Since 2.1.0)</a><br/>
 *      <a href="#39">39. Mocking final types, enums and final methods (Since 2.1.0)</a><br/>
 *      <a href="#40">40. Improved productivity and cleaner tests with "stricter" Mockito (Since 2.+)</a><br/>
 *      <a href="#41">41. Advanced public API for framework integrations (Since 2.10.+)</a><br/>
 *      <a href="#42">42. New API for integrations: listening on verification start events (Since 2.11.+)</a><br/>
 *      <a href="#43">43. New API for integrations: <code>MockitoSession</code> is usable by testing frameworks (Since 2.15.+)</a><br/>
 *      <a href="#44">44. Deprecated <code>org.mockito.plugins.InstantiatorProvider</code> as it was leaking internal API. it was replaced by <code>org.mockito.plugins.InstantiatorProvider2 (Since 2.15.4)</code></a><br/>
 *      <a href="#45">45. New JUnit Jupiter (JUnit5+) extension</a><br/>
 *      <a href="#46">46. New <code>Mockito.lenient()</code> and <code>MockSettings.lenient()</code> methods (Since 2.20.0)</a><br/>
 *      <a href="#47">47. New API for clearing mock state in inline mocking (Since 2.25.0)</a><br/>
 *      <a href="#48">48. New API for mocking static methods (Since 3.4.0)</a><br/>
 * </b>
 *
 * <h3 id="0">0. <a class="meaningful_link" href="#mockito2" name="mockito2">Migrating to Mockito 2</a></h3>
 *
 * In order to continue improving Mockito and further improve the unit testing experience, we want you to upgrade to 2.1.0!
 * Mockito follows <a href="http://semver.org/">semantic versioning</a> and contains breaking changes only on major version upgrades.
 * In the lifecycle of a library, breaking changes are necessary
 * to roll out a set of brand new features that alter the existing behavior or even change the API.
 * For a comprehensive guide on the new release including incompatible changes,
 * see '<a href="https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2">What's new in Mockito 2</a>' wiki page.
 * We hope that you enjoy Mockito 2!
 *
 * <h3 id="0.1">0.1. <a class="meaningful_link" href="#mockito-android" name="mockito-android">Mockito Android support</a></h3>
 *
 * With Mockito version 2.6.1 we ship "native" Android support. To enable Android support, add the `mockito-android` library as dependency
 * to your project. This artifact is published to the same Mockito organization and can be imported for Android as follows:
 *
 * <pre class="code"><code>
 * repositories {
 *   jcenter()
 * }
 * dependencies {
 *   testCompile "org.mockito:mockito-core:+"
 *   androidTestCompile "org.mockito:mockito-android:+"
 * }
 * </code></pre>
 *
 * You can continue to run the same unit tests on a regular VM by using the `mockito-core` artifact in your "testCompile" scope as shown
 * above. Be aware that you cannot use the <a href="#39">inline mock maker</a> on Android due to limitations in the Android VM.
 *
 * If you encounter issues with mocking on Android, please open an issue
 * <a href="https://github.com/mockito/mockito/issues/new">on the official issue tracker</a>.
 * Do provide the version of Android you are working on and dependencies of your project.
 *
 * <h3 id="0.2">0.2. <a class="meaningful_link" href="#mockito-inline" name="mockito-inline">Configuration-free inline mock making</a></h3>
 *
 * Starting with version 2.7.6, we offer the 'mockito-inline' artifact that enables <a href="#39">inline mock making</a> without configuring
 * the MockMaker extension file. To use this, add the `mockito-inline` instead of the `mockito-core` artifact as follows:
 *
 * <pre class="code"><code>
 * repositories {
 *   jcenter()
 * }
 * dependencies {
 *   testCompile "org.mockito:mockito-inline:+"
 * }
 * </code></pre>
 *
 * Be aware that this artifact may be abolished when the inline mock making feature is integrated into the default mock maker.
 *
 * <p>
 * For more information about inline mock making, see <a href="#39">section 39</a>.
 *
 * <h3 id="1">1. <a class="meaningful_link" href="#verification" name="verification">Let's verify some behaviour!</a></h3>
 *
 * The following examples mock a List, because most people are familiar with the interface (such as the
 * <code>add()</code>, <code>get()</code>, <code>clear()</code> methods). <br>
 * In reality, please don't mock the List class. Use a real instance instead.
 *
 * <pre class="code"><code class="java">
 * //Let's import Mockito statically so that the code looks clearer
 * import static org.mockito.Mockito.*;
 *
 * //mock creation
 * List mockedList = mock(List.class);
 *
 * //using mock object
 * mockedList.add("one");
 * mockedList.clear();
 *
 * //verification
 * verify(mockedList).add("one");
 * verify(mockedList).clear();
 * </code></pre>
 *
 * <p>
 * Once created, a mock will remember all interactions. Then you can selectively
 * verify whatever interactions you are interested in.
 * </p>
 *
 *
 *
 * <h3 id="2">2. <a class="meaningful_link" href="#stubbing" name="stubbing">How about some stubbing?</a></h3>
 *
 * <pre class="code"><code class="java">
 * //You can mock concrete classes, not just interfaces
 * LinkedList mockedList = mock(LinkedList.class);
 *
 * //stubbing
 * when(mockedList.get(0)).thenReturn("first");
 * when(mockedList.get(1)).thenThrow(new RuntimeException());
 *
 * //following prints "first"
 * System.out.println(mockedList.get(0));
 *
 * //following throws runtime exception
 * System.out.println(mockedList.get(1));
 *
 * //following prints "null" because get(999) was not stubbed
 * System.out.println(mockedList.get(999));
 *
 * //Although it is possible to verify a stubbed invocation, usually <b>it's just redundant</b>
 * //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
 * //If your code doesn't care what get(0) returns, then it should not be stubbed.
 * verify(mockedList).get(0);
 * </code></pre>
 *
 * <ul>
 * <li> By default, for all methods that return a value, a mock will return either null,
 * a primitive/primitive wrapper value, or an empty collection, as appropriate.
 * For example 0 for an int/Integer and false for a boolean/Boolean. </li>
 *
 * <li> Stubbing can be overridden: for example common stubbing can go to
 * fixture setup but the test methods can override it.
 * Please note that overridding stubbing is a potential code smell that points out too much stubbing</li>
 *
 * <li> Once stubbed, the method will always return a stubbed value, regardless
 * of how many times it is called. </li>
 *
 * <li> Last stubbing is more important - when you stubbed the same method with
 * the same arguments many times.
 * Other words: <b>the order of stubbing matters</b> but it is only meaningful rarely,
 * e.g. when stubbing exactly the same method calls or sometimes when argument matchers are used, etc.</li>
 *
 * </ul>
 *
 *
 *
 * <h3 id="3">3. <a class="meaningful_link" href="#argument_matchers" name="argument_matchers">Argument matchers</a></h3>
 *
 * Mockito verifies argument values in natural java style: by using an <code>equals()</code> method.
 * Sometimes, when extra flexibility is required then you might use argument matchers:
 *
 * <pre class="code"><code class="java">
 * //stubbing using built-in anyInt() argument matcher
 * when(mockedList.get(anyInt())).thenReturn("element");
 *
 * //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
 * when(mockedList.contains(argThat(isValid()))).thenReturn(true);
 *
 * //following prints "element"
 * System.out.println(mockedList.get(999));
 *
 * //<b>you can also verify using an argument matcher</b>
 * verify(mockedList).get(anyInt());
 *
 * //<b>argument matchers can also be written as Java 8 Lambdas</b>
 * verify(mockedList).add(argThat(someString -> someString.length() > 5));
 *
 * </code></pre>
 *
 * <p>
 * Argument matchers allow flexible verification or stubbing.
 * {@link ArgumentMatchers Click here} {@link org.mockito.hamcrest.MockitoHamcrest or here} to see more built-in matchers
 * and examples of <b>custom argument matchers / hamcrest matchers</b>.
 * <p>
 * For information solely on <b>custom argument matchers</b> check out javadoc for {@link ArgumentMatcher} class.
 * <p>
 * Be reasonable with using complicated argument matching.
 * The natural matching style using <code>equals()</code> with occasional <code>anyX()</code> matchers tend to give clean & simple tests.
 * Sometimes it's just better to refactor the code to allow <code>equals()</code> matching or even implement <code>equals()</code> method to help out with testing.
 * <p>
 * Also, read <a href="#15">section 15</a> or javadoc for {@link ArgumentCaptor} class.
 * {@link ArgumentCaptor} is a special implementation of an argument matcher that captures argument values for further assertions.
 * <p>
 * <b>Warning on argument matchers:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided
 * by matchers.
 * <p>
 * The following example shows verification but the same applies to stubbing:
 *
 * <pre class="code"><code class="java">
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without an argument matcher.
 * </code></pre>
 *
 * <p>
 * Matcher methods like <code>anyObject()</code>, <code>eq()</code> <b>do not</b> return matchers.
 * Internally, they record a matcher on a stack and return a dummy value (usually null).
 * This implementation is due to static type safety imposed by the java compiler.
 * The consequence is that you cannot use <code>anyObject()</code>, <code>eq()</code> methods outside of verified/stubbed method.
 *
 *
 *
 *
 * <h3 id="4">4. <a class="meaningful_link" href="#exact_verification" name="exact_verification">Verifying exact number of invocations</a> /
 * <a class="meaningful_link" href="#at_least_verification" name="at_least_verification">at least x</a> / never</h3>
 *
 * <pre class="code"><code class="java">
 * //using mock
 * mockedList.add("once");
 *
 * mockedList.add("twice");
 * mockedList.add("twice");
 *
 * mockedList.add("three times");
 * mockedList.add("three times");
 * mockedList.add("three times");
 *
 * //following two verifications work exactly the same - times(1) is used by default
 * verify(mockedList).add("once");
 * verify(mockedList, times(1)).add("once");
 *
 * //exact number of invocations verification
 * verify(mockedList, times(2)).add("twice");
 * verify(mockedList, times(3)).add("three times");
 *
 * //verification using never(). never() is an alias to times(0)
 * verify(mockedList, never()).add("never happened");
 *
 * //verification using atLeast()/atMost()
 * verify(mockedList, atMostOnce()).add("once");
 * verify(mockedList, atLeastOnce()).add("three times");
 * verify(mockedList, atLeast(2)).add("three times");
 * verify(mockedList, atMost(5)).add("three times");
 *
 * </code></pre>
 *
 * <p>
 * <b>times(1) is the default.</b> Therefore using times(1) explicitly can be
 * omitted.
 *
 *
 *
 *
 * <h3 id="5">5. <a class="meaningful_link" href="#stubbing_with_exceptions" name="stubbing_with_exceptions">Stubbing void methods with exceptions</a></h3>
 *
 * <pre class="code"><code class="java">
 *   doThrow(new RuntimeException()).when(mockedList).clear();
 *
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </code></pre>
 *
 * Read more about <code>doThrow()</code>|<code>doAnswer()</code> family of methods in <a href="#12">section 12</a>.
 * <p>
 *
 * <h3 id="6">6. <a class="meaningful_link" href="#in_order_verification" name="in_order_verification">Verification in order</a></h3>
 *
 * <pre class="code"><code class="java">
 * // A. Single mock whose methods must be invoked in a particular order
 * List singleMock = mock(List.class);
 *
 * //using a single mock
 * singleMock.add("was added first");
 * singleMock.add("was added second");
 *
 * //create an inOrder verifier for a single mock
 * InOrder inOrder = inOrder(singleMock);
 *
 * //following will make sure that add is first called with "was added first", then with "was added second"
 * inOrder.verify(singleMock).add("was added first");
 * inOrder.verify(singleMock).add("was added second");
 *
 * // B. Multiple mocks that must be used in a particular order
 * List firstMock = mock(List.class);
 * List secondMock = mock(List.class);
 *
 * //using mocks
 * firstMock.add("was called first");
 * secondMock.add("was called second");
 *
 * //create inOrder object passing any mocks that need to be verified in order
 * InOrder inOrder = inOrder(firstMock, secondMock);
 *
 * //following will make sure that firstMock was called before secondMock
 * inOrder.verify(firstMock).add("was called first");
 * inOrder.verify(secondMock).add("was called second");
 *
 * // Oh, and A + B can be mixed together at will
 * </code></pre>
 *
 * Verification in order is flexible - <b>you don't have to verify all
 * interactions</b> one-by-one but only those that you are interested in
 * testing in order.
 * <p>
 * Also, you can create an InOrder object passing only the mocks that are relevant for
 * in-order verification.
 *
 *
 *
 *
 * <h3 id="7">7. <a class="meaningful_link" href="#never_verification" name="never_verification">Making sure interaction(s) never happened on mock</a></h3>
 *
 * <pre class="code"><code class="java">
 * //using mocks - only mockOne is interacted
 * mockOne.add("one");
 *
 * //ordinary verification
 * verify(mockOne).add("one");
 *
 * //verify that method was never called on a mock
 * verify(mockOne, never()).add("two");
 *
 * //verify that other mocks were not interacted
 * verifyZeroInteractions(mockTwo, mockThree);
 *
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="8">8. <a class="meaningful_link" href="#finding_redundant_invocations" name="finding_redundant_invocations">Finding redundant invocations</a></h3>
 *
 * <pre class="code"><code class="java">
 * //using mocks
 * mockedList.add("one");
 * mockedList.add("two");
 *
 * verify(mockedList).add("one");
 *
 * //following verification will fail
 * verifyNoMoreInteractions(mockedList);
 * </code></pre>
 *
 * A word of <b>warning</b>:
 * Some users who did a lot of classic, expect-run-verify mocking tend to use <code>verifyNoMoreInteractions()</code> very often, even in every test method.
 * <code>verifyNoMoreInteractions()</code> is not recommended to use in every test method.
 * <code>verifyNoMoreInteractions()</code> is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
 * Abusing it leads to <strong>overspecified</strong>, <strong>less maintainable</strong> tests.
 *
 * <p>
 * See also {@link Mockito#never()} - it is more explicit and
 * communicates the intent well.
 * <p>
 *
 *
 *
 *
 * <h3 id="9">9. <a class="meaningful_link" href="#mock_annotation" name="mock_annotation">Shorthand for mocks creation - <code>&#064;Mock</code> annotation</a></h3>
 *
 * <ul>
 * <li>Minimizes repetitive mock creation code.</li>
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because the <b>field name</b>
 * is used to identify the mock.</li>
 * </ul>
 *
 * <pre class="code"><code class="java">
 *   public class ArticleManagerTest {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Mock private UserProvider userProvider;
 *
 *       private ArticleManager manager;
 * </code></pre>
 *
 * <b>Important!</b> This needs to be somewhere in the base class or a test
 * runner:
 *
 * <pre class="code"><code class="java">
 * MockitoAnnotations.openMocks(testClass);
 * </code></pre>
 *
 * You can use built-in runner: {@link MockitoJUnitRunner} or a rule: {@link MockitoRule}.
 * For JUnit5 tests, refer to the JUnit5 extension described in <a href="#45">section 45</a>.
 * <p>
 * Read more here: {@link MockitoAnnotations}
 *
 *
 *
 *
 * <h3 id="10">10. <a class="meaningful_link" href="#stubbing_consecutive_calls" name="stubbing_consecutive_calls">Stubbing consecutive calls</a> (iterator-style stubbing)</h3>
 *
 * Sometimes we need to stub with different return value/exception for the same
 * method call. Typical use case could be mocking iterators.
 * Original version of Mockito did not have this feature to promote simple mocking.
 * For example, instead of iterators one could use {@link Iterable} or simply
 * collections. Those offer natural ways of stubbing (e.g. using real
 * collections). In rare scenarios stubbing consecutive calls could be useful,
 * though:
 * <p>
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod("some arg"))
 *   .thenThrow(new RuntimeException())
 *   .thenReturn("foo");
 *
 * //First call: throws runtime exception:
 * mock.someMethod("some arg");
 *
 * //Second call: prints "foo"
 * System.out.println(mock.someMethod("some arg"));
 *
 * //Any consecutive call: prints "foo" as well (last stubbing wins).
 * System.out.println(mock.someMethod("some arg"));
 * </code></pre>
 *
 * Alternative, shorter version of consecutive stubbing:
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod("some arg"))
 *   .thenReturn("one", "two", "three");
 * </code></pre>
 *
 * <strong>Warning</strong> : if instead of chaining {@code .thenReturn()} calls, multiple stubbing with the same matchers or arguments
 * is used, then each stubbing will override the previous one:
 *
 * <pre class="code"><code class="java">
 * //All mock.someMethod("some arg") calls will return "two"
 * when(mock.someMethod("some arg"))
 *   .thenReturn("one")
 * when(mock.someMethod("some arg"))
 *   .thenReturn("two")
 * </code></pre>
 *
 *
 *
 * <h3 id="11">11. <a class="meaningful_link" href="#answer_stubs" name="answer_stubs">Stubbing with callbacks</a></h3>
 *
 * Allows stubbing with generic {@link Answer} interface.
 * <p>
 * Yet another controversial feature which was not included in Mockito
 * originally. We recommend simply stubbing with <code>thenReturn()</code> or
 * <code>thenThrow()</code>, which should be enough to test/test-drive
 * any clean & simple code. However, if you do have a need to stub with the generic Answer interface, here is an example:
 *
 * <pre class="code"><code class="java">
 * when(mock.someMethod(anyString())).thenAnswer(
 *     new Answer() {
 *         public Object answer(InvocationOnMock invocation) {
 *             Object[] args = invocation.getArguments();
 *             Object mock = invocation.getMock();
 *             return "called with arguments: " + Arrays.toString(args);
 *         }
 * });
 *
 * //Following prints "called with arguments: [foo]"
 * System.out.println(mock.someMethod("foo"));
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="12">12. <a class="meaningful_link" href="#do_family_methods_stubs" name="do_family_methods_stubs"><code>doReturn()</code>|<code>doThrow()</code>|
 * <code>doAnswer()</code>|<code>doNothing()</code>|<code>doCallRealMethod()</code> family of methods</a></h3>
 *
 * Stubbing void methods requires a different approach from {@link Mockito#when(Object)} because the compiler does not
 * like void methods inside brackets...
 * <p>
 * Use <code>doThrow()</code> when you want to stub a void method with an exception:
 * <pre class="code"><code class="java">
 *   doThrow(new RuntimeException()).when(mockedList).clear();
 *
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </code></pre>
 * </p>
 *
 * <p>
 * You can use <code>doThrow()</code>, <code>doAnswer()</code>, <code>doNothing()</code>, <code>doReturn()</code>
 * and <code>doCallRealMethod()</code> in place of the corresponding call with <code>when()</code>, for any method.
 * It is necessary when you
 * <ul>
 *     <li>stub void methods</li>
 *     <li>stub methods on spy objects (see below)</li>
 *     <li>stub the same method more than once, to change the behaviour of a mock in the middle of a test.</li>
 * </ul>
 * but you may prefer to use these methods in place of the alternative with <code>when()</code>, for all of your stubbing calls.
 * <p>
 * Read more about these methods:
 * <p>
 * {@link Mockito#doReturn(Object)}
 * <p>
 * {@link Mockito#doThrow(Throwable...)}
 * <p>
 * {@link Mockito#doThrow(Class)}
 * <p>
 * {@link Mockito#doAnswer(Answer)}
 * <p>
 * {@link Mockito#doNothing()}
 * <p>
 * {@link Mockito#doCallRealMethod()}
 *
 *
 *
 *
 * <h3 id="13">13. <a class="meaningful_link" href="#spy" name="spy">Spying on real objects</a></h3>
 *
 * You can create spies of real objects. When you use the spy then the <b>real</b> methods are called
 * (unless a method was stubbed).
 * <p>
 * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
 *
 * <p>
 * Spying on real objects can be associated with "partial mocking" concept.
 * <b>Before the release 1.8</b>, Mockito spies were not real partial mocks.
 * The reason was we thought partial mock is a code smell.
 * At some point we found legitimate use cases for partial mocks
 * (3rd party interfaces, interim refactoring of legacy code).
 * <p>
 *
 * <pre class="code"><code class="java">
 *   List list = new LinkedList();
 *   List spy = spy(list);
 *
 *   //optionally, you can stub out some methods:
 *   when(spy.size()).thenReturn(100);
 *
 *   //using the spy calls <b>*real*</b> methods
 *   spy.add("one");
 *   spy.add("two");
 *
 *   //prints "one" - the first element of a list
 *   System.out.println(spy.get(0));
 *
 *   //size() method was stubbed - 100 is printed
 *   System.out.println(spy.size());
 *
 *   //optionally, you can verify
 *   verify(spy).add("one");
 *   verify(spy).add("two");
 * </code></pre>
 *
 * <h4>Important gotcha on spying real objects!</h4>
 * <ol>
 * <li>Sometimes it's impossible or impractical to use {@link Mockito#when(Object)} for stubbing spies.
 * Therefore when using spies please consider <code>doReturn</code>|<code>Answer</code>|<code>Throw()</code> family of
 * methods for stubbing. Example:
 *
 * <pre class="code"><code class="java">
 *   List list = new LinkedList();
 *   List spy = spy(list);
 *
 *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
 *   when(spy.get(0)).thenReturn("foo");
 *
 *   //You have to use doReturn() for stubbing
 *   doReturn("foo").when(spy).get(0);
 * </code></pre>
 * </li>
 *
 * <li>Mockito <b>*does not*</b> delegate calls to the passed real instance, instead it actually creates a copy of it.
 * So if you keep the real instance and interact with it, don't expect the spied to be aware of those interaction
 * and their effect on real instance state.
 * The corollary is that when an <b>*unstubbed*</b> method is called <b>*on the spy*</b> but <b>*not on the real instance*</b>,
 * you won't see any effects on the real instance.
 * </li>
 *
 * <li>Watch out for final methods.
 * Mockito doesn't mock final methods so the bottom line is: when you spy on real objects + you try to stub a final method = trouble.
 * Also you won't be able to verify those method as well.
 * </li>
 * </ol>
 *
 *
 *
 *
 * <h3 id="14">14. Changing <a class="meaningful_link" href="#defaultreturn" name="defaultreturn">default return values of unstubbed invocations</a> (Since 1.7)</h3>
 *
 * You can create a mock with specified strategy for its return values.
 * It's quite an advanced feature and typically you don't need it to write decent tests.
 * However, it can be helpful for working with <b>legacy systems</b>.
 * <p>
 * It is the default answer so it will be used <b>only when you don't</b> stub the method call.
 *
 * <pre class="code"><code class="java">
 *   Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
 *   Foo mockTwo = mock(Foo.class, new YourOwnAnswer());
 * </code></pre>
 *
 * <p>
 * Read more about this interesting implementation of <i>Answer</i>: {@link Mockito#RETURNS_SMART_NULLS}
 *
 *
 *
 *
 * <h3 id="15">15. <a class="meaningful_link" href="#captors" name="captors">Capturing arguments</a> for further assertions (Since 1.8.0)</h3>
 *
 * Mockito verifies argument values in natural java style: by using an <code>equals()</code> method.
 * This is also the recommended way of matching arguments because it makes tests clean & simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre class="code"><code class="java">
 *   ArgumentCaptor&lt;Person&gt; argument = ArgumentCaptor.forClass(Person.class);
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </code></pre>
 *
 * <b>Warning:</b> it is recommended to use ArgumentCaptor with verification <b>but not</b> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of assert (aka verify or 'then') block.
 * Also it may reduce defect localization because if stubbed method was not called then no argument is captured.
 * <p>
 * In a way ArgumentCaptor is related to custom argument matchers (see javadoc for {@link ArgumentMatcher} class).
 * Both techniques can be used for making sure certain arguments were passed to mocks.
 * However, ArgumentCaptor may be a better fit if:
 * <ul>
 * <li>custom argument matcher is not likely to be reused</li>
 * <li>you just need it to assert on argument values to complete verification</li>
 * </ul>
 * Custom argument matchers via {@link ArgumentMatcher} are usually better for stubbing.
 *
 *
 *
 *
 * <h3 id="16">16. <a class="meaningful_link" href="#partial_mocks" name="partial_mocks">Real partial mocks</a> (Since 1.8.0)</h3>
 *
 *  Finally, after many internal debates & discussions on the mailing list, partial mock support was added to Mockito.
 *  Previously we considered partial mocks as code smells. However, we found a legitimate use case for partial mocks.
 *  <p>
 *  <b>Before release 1.8</b> <code>spy()</code> was not producing real partial mocks and it was confusing for some users.
 *  Read more about spying: <a href="#13">here</a> or in javadoc for {@link Mockito#spy(Object)} method.
 *  <p>
 *  <pre class="code"><code class="java">
 *    //you can create partial mock with spy() method:
 *    List list = spy(new LinkedList());
 *
 *    //you can enable partial mock capabilities selectively on mocks:
 *    Foo mock = mock(Foo.class);
 *    //Be sure the real implementation is 'safe'.
 *    //If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
 *    when(mock.someMethod()).thenCallRealMethod();
 *  </code></pre>
 *
 * As usual you are going to read <b>the partial mock warning</b>:
 * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
 * How does partial mock fit into this paradigm? Well, it just doesn't...
 * Partial mock usually means that the complexity has been moved to a different method on the same object.
 * In most cases, this is not the way you want to design your application.
 * <p>
 * However, there are rare cases when partial mocks come handy:
 * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
 * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
 *
 *
 *
 *
 * <h3 id="17">17. <a class="meaningful_link" href="#resetting_mocks" name="resetting_mocks">Resetting mocks</a> (Since 1.8.0)</h3>
 *
 * Smart Mockito users hardly use this feature because they know it could be a sign of poor tests.
 * Normally, you don't need to reset your mocks, just create new mocks for each test method.
 * <p>
 * Instead of <code>reset()</code> please consider writing simple, small and focused test methods over lengthy, over-specified tests.
 * <b>First potential code smell is <code>reset()</code> in the middle of the test method.</b> This probably means you're testing too much.
 * Follow the whisper of your test methods: "Please keep us small & focused on single behavior".
 * There are several threads about it on mockito mailing list.
 * <p>
 * The only reason we added <code>reset()</code> method is to
 * make it possible to work with container-injected mocks.
 * For more information see FAQ (<a href="https://github.com/mockito/mockito/wiki/FAQ">here</a>).
 * <p>
 * <b>Don't harm yourself.</b> <code>reset()</code> in the middle of the test method is a code smell (you're probably testing too much).
 * <pre class="code"><code class="java">
 *   List mock = mock(List.class);
 *   when(mock.size()).thenReturn(10);
 *   mock.add(1);
 *
 *   reset(mock);
 *   //at this point the mock forgot any interactions & stubbing
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="18">18. <a class="meaningful_link" href="#framework_validation" name="framework_validation">Troubleshooting & validating framework usage</a> (Since 1.8.0)</h3>
 *
 * First of all, in case of any trouble, I encourage you to read the Mockito FAQ:
 * <a href="https://github.com/mockito/mockito/wiki/FAQ">https://github.com/mockito/mockito/wiki/FAQ</a>
 * <p>
 * In case of questions you may also post to mockito mailing list:
 * <a href="http://groups.google.com/group/mockito">http://groups.google.com/group/mockito</a>
 * <p>
 * Next, you should know that Mockito validates if you use it correctly <b>all the time</b>.
 * However, there's a gotcha so please read the javadoc for {@link Mockito#validateMockitoUsage()}
 *
 *
 *
 *
 * <h3 id="19">19. <a class="meaningful_link" href="#bdd_mockito" name="bdd_mockito">Aliases for behavior driven development</a> (Since 1.8.0)</h3>
 *
 * Behavior Driven Development style of writing tests uses <b>//given //when //then</b> comments as fundamental parts of your test methods.
 * This is exactly how we write our tests and we warmly encourage you to do so!
 * <p>
 * Start learning about BDD here: <a href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">http://en.wikipedia.org/wiki/Behavior_Driven_Development</a>
 * <p>
 * The problem is that current stubbing api with canonical role of <b>when</b> word does not integrate nicely with <b>//given //when //then</b> comments.
 * It's because stubbing belongs to <b>given</b> component of the test and not to the <b>when</b> component of the test.
 * Hence {@link BDDMockito} class introduces an alias so that you stub method calls with {@link BDDMockito#given(Object)} method.
 * Now it really nicely integrates with the <b>given</b> component of a BDD style test!
 * <p>
 * Here is how the test might look like:
 * <pre class="code"><code class="java">
 * import static org.mockito.BDDMockito.*;
 *
 * Seller seller = mock(Seller.class);
 * Shop shop = new Shop(seller);
 *
 * public void shouldBuyBread() throws Exception {
 *   //given
 *   given(seller.askForBread()).willReturn(new Bread());
 *
 *   //when
 *   Goods goods = shop.buyBread();
 *
 *   //then
 *   assertThat(goods, containBread());
 * }
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="20">20. <a class="meaningful_link" href="#serializable_mocks" name="serializable_mocks">Serializable mocks</a> (Since 1.8.1)</h3>
 *
 * Mocks can be made serializable. With this feature you can use a mock in a place that requires dependencies to be serializable.
 * <p>
 * WARNING: This should be rarely used in unit testing.
 * <p>
 * The behaviour was implemented for a specific use case of a BDD spec that had an unreliable external dependency.  This
 * was in a web environment and the objects from the external dependency were being serialized to pass between layers.
 * <p>
 * To create serializable mock use {@link MockSettings#serializable()}:
 * <pre class="code"><code class="java">
 *   List serializableMock = mock(List.class, withSettings().serializable());
 * </code></pre>
 * <p>
 * The mock can be serialized assuming all the normal <a href='http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html'>
 * serialization requirements</a> are met by the class.
 * <p>
 * Making a real object spy serializable is a bit more effort as the spy(...) method does not have an overloaded version
 * which accepts MockSettings. No worries, you will hardly ever use it.
 *
 * <pre class="code"><code class="java">
 * List&lt;Object&gt; list = new ArrayList&lt;Object&gt;();
 * List&lt;Object&gt; spy = mock(ArrayList.class, withSettings()
 *                 .spiedInstance(list)
 *                 .defaultAnswer(CALLS_REAL_METHODS)
 *                 .serializable());
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="21">21. New annotations: <a class="meaningful_link" href="#captor_annotation" name="captor_annotation"><code>&#064;Captor</code></a>,
 * <a class="meaningful_link" href="#spy_annotation" name="spy_annotation"><code>&#064;Spy</code></a>,
 * <a class="meaningful_link" href="#injectmocks_annotation" name="injectmocks_annotation"><code>&#064;InjectMocks</code></a> (Since 1.8.3)</h3>
 *
 * <p>
 * Release 1.8.3 brings new annotations that may be helpful on occasion:
 *
 * <ul>
 * <li>&#064;{@link Captor} simplifies creation of {@link ArgumentCaptor}
 * - useful when the argument to capture is a nasty generic class and you want to avoid compiler warnings
 * <li>&#064;{@link Spy} - you can use it instead {@link Mockito#spy(Object)}.
 * <li>&#064;{@link InjectMocks} - injects mock or spy fields into tested object automatically.
 * </ul>
 *
 * <p>
 * Note that &#064;{@link InjectMocks} can also be used in combination with the &#064;{@link Spy} annotation, it means
 * that Mockito will inject mocks into the partial mock under test. This complexity is another good reason why you
 * should only use partial mocks as a last resort. See point 16 about partial mocks.
 *
 * <p>
 * All new annotations are <b>*only*</b> processed on {@link MockitoAnnotations#openMocks(Object)}.
 * Just like for &#064;{@link Mock} annotation you can use the built-in runner: {@link MockitoJUnitRunner} or rule:
 * {@link MockitoRule}.
 * <p>
 *
 *
 *
 *
 * <h3 id="22">22. <a class="meaningful_link" href="#verification_timeout" name="verification_timeout">Verification with timeout</a> (Since 1.8.5)</h3>
 * <p>
 * Allows verifying with timeout. It causes a verify to wait for a specified period of time for a desired
 * interaction rather than fails immediately if had not already happened. May be useful for testing in concurrent
 * conditions.
 * <p>
 * This feature should be used rarely - figure out a better way of testing your multi-threaded system.
 * <p>
 * Not yet implemented to work with InOrder verification.
 * <p>
 * Examples:
 * <p>
 * <pre class="code"><code class="java">
 *   //passes when someMethod() is called no later than within 100 ms
 *   //exits immediately when verification is satisfied (e.g. may not wait full 100 ms)
 *   verify(mock, timeout(100)).someMethod();
 *   //above is an alias to:
 *   verify(mock, timeout(100).times(1)).someMethod();
 *
 *   //passes as soon as someMethod() has been called 2 times under 100 ms
 *   verify(mock, timeout(100).times(2)).someMethod();
 *
 *   //equivalent: this also passes as soon as someMethod() has been called 2 times under 100 ms
 *   verify(mock, timeout(100).atLeast(2)).someMethod();
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="23">23. <a class="meaningful_link" href="#automatic_instantiation" name="automatic_instantiation">Automatic instantiation of <code>&#064;Spies</code>,
 * <code>&#064;InjectMocks</code></a> and <a class="meaningful_link" href="#constructor_injection" name="constructor_injection">constructor injection goodness</a> (Since 1.9.0)</h3>
 *
 * <p>
 * Mockito will now try to instantiate &#064;{@link Spy} and will instantiate &#064;{@link InjectMocks} fields
 * using <b>constructor</b> injection, <b>setter</b> injection, or <b>field</b> injection.
 * <p>
 * To take advantage of this feature you need to use {@link MockitoAnnotations#openMocks(Object)}, {@link MockitoJUnitRunner}
 * or {@link MockitoRule}.
 * <p>
 * Read more about available tricks and the rules of injection in the javadoc for {@link InjectMocks}
 * <pre class="code"><code class="java">
 * //instead:
 * &#064;Spy BeerDrinker drinker = new BeerDrinker();
 * //you can write:
 * &#064;Spy BeerDrinker drinker;
 *
 * //same applies to &#064;InjectMocks annotation:
 * &#064;InjectMocks LocalPub;
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="24">24. <a class="meaningful_link" href="#one_liner_stub" name="one_liner_stub">One-liner stubs</a> (Since 1.9.0)</h3>
 * <p>
 * Mockito will now allow you to create mocks when stubbing.
 * Basically, it allows to create a stub in one line of code.
 * This can be helpful to keep test code clean.
 * For example, some boring stub can be created & stubbed at field initialization in a test:
 * <pre class="code"><code class="java">
 * public class CarTest {
 *   Car boringStubbedCar = when(mock(Car.class).shiftGear()).thenThrow(EngineNotStarted.class).getMock();
 *
 *   &#064;Test public void should... {}
 * </code></pre>
 *
 *
 *
 *
 * <h3 id="25">25. <a class="meaningful_link" href="#ignore_stubs_verification" name="ignore_stubs_verification">Verification ignoring stubs</a> (Since 1.9.0)</h3>
 * <p>
 * Mockito will now allow to ignore stubbing for the sake of verification.
 * Sometimes useful when coupled with <code>verifyNoMoreInteractions()</code> or verification <code>inOrder()</code>.
 * Helps avoiding redundant verification of stubbed calls - typically we're not interested in verifying stubs.
 * <p>
 * <b>Warning</b>, <code>ignoreStubs()</code> might lead to overuse of verifyNoMoreInteractions(ignoreStubs(...));
 * Bear in mind that Mockito does not recommend bombarding every test with <code>verifyNoMoreInteractions()</code>
 * for the reasons outlined in javadoc for {@link Mockito#verifyNoMoreInteractions(Object...)}
 * <p>Some examples:
 * <pre class="code"><code class="java">
 * verify(mock).foo();
 * verify(mockTwo).bar();
 *
 * //ignores all stubbed methods:
 * verifyNoMoreInteractions(ignoreStubs(mock, mockTwo));
 *
 * //creates InOrder that will ignore stubbed
 * InOrder inOrder = inOrder(ignoreStubs(mock, mockTwo));
 * inOrder.verify(mock).foo();
 * inOrder.verify(mockTwo).bar();
 * inOrder.verifyNoMoreInteractions();
 * </code></pre>
 * <p>
 * Advanced examples and more details can be found in javadoc for {@link Mockito#ignoreStubs(Object...)}
 *
 *
 *
 *
 * <h3 id="26">26. <a class="meaningful_link" href="#mocking_details" name="mocking_details">Mocking details</a> (Improved in 2.2.x)</h3>
 * <p>
 *
 * Mockito offers API to inspect the details of a mock object.
 * This API is useful for advanced users and mocking framework integrators.
 *
 * <pre class="code"><code class="java">
 *   //To identify whether a particular object is a mock or a spy:
 *   Mockito.mockingDetails(someObject).isMock();
 *   Mockito.mockingDetails(someObject).isSpy();
 *
 *   //Getting details like type to mock or default answer:
 *   MockingDetails details = mockingDetails(mock);
 *   details.getMockCreationSettings().getTypeToMock();
 *   details.getMockCreationSettings().getDefaultAnswer();
 *
 *   //Getting invocations and stubbings of the mock:
 *   MockingDetails details = mockingDetails(mock);
 *   details.getInvocations();
 *   details.getStubbings();
 *
 *   //Printing all interactions (including stubbing, unused stubs)
 *   System.out.println(mockingDetails(mock).printInvocations());
 * </code></pre>
 *
 * For more information see javadoc for {@link MockingDetails}.
 *
 * <h3 id="27">27. <a class="meaningful_link" href="#delegating_call_to_real_instance" name="delegating_call_to_real_instance">Delegate calls to real instance</a> (Since 1.9.5)</h3>
 *
 * <p>Useful for spies or partial mocks of objects <strong>that are difficult to mock or spy</strong> using the usual spy API.
 * Since Mockito 1.10.11, the delegate may or may not be of the same type as the mock.
 * If the type is different, a matching method needs to be found on delegate type otherwise an exception is thrown.
 *
 * Possible use cases for this feature:
 * <ul>
 *     <li>Final classes but with an interface</li>
 *     <li>Already custom proxied object</li>
 *     <li>Special objects with a finalize method, i.e. to avoid executing it 2 times</li>
 * </ul>
 *
 * <p>The difference with the regular spy:
 * <ul>
 *   <li>
 *     The regular spy ({@link #spy(Object)}) contains <strong>all</strong> state from the spied instance
 *     and the methods are invoked on the spy. The spied instance is only used at mock creation to copy the state from.
 *     If you call a method on a regular spy and it internally calls other methods on this spy, those calls are remembered
 *     for verifications, and they can be effectively stubbed.
 *   </li>
 *   <li>
 *     The mock that delegates simply delegates all methods to the delegate.
 *     The delegate is used all the time as methods are delegated onto it.
 *     If you call a method on a mock that delegates and it internally calls other methods on this mock,
 *     those calls are <strong>not</strong> remembered for verifications, stubbing does not have effect on them, too.
 *     Mock that delegates is less powerful than the regular spy but it is useful when the regular spy cannot be created.
 *   </li>
 * </ul>
 *
 * <p>
 * See more information in docs for {@link AdditionalAnswers#delegatesTo(Object)}.
 *
 *
 *
 *
 * <h3 id="28">28. <a class="meaningful_link" href="#mock_maker_plugin" name="mock_maker_plugin"><code>MockMaker</code> API</a> (Since 1.9.5)</h3>
 * <p>Driven by requirements and patches from Google Android guys Mockito now offers an extension point
 *   that allows replacing the proxy generation engine. By default, Mockito uses <a href="https://github.com/raphw/byte-buddy">Byte Buddy</a>
 *   to create dynamic proxies.
 * <p>The extension point is for advanced users that want to extend Mockito. For example, it is now possible
 *   to use Mockito for Android testing with a help of <a href="https://github.com/crittercism/dexmaker">dexmaker</a>.
 * <p>For more details, motivations and examples please refer to
 * the docs for {@link org.mockito.plugins.MockMaker}.
 *
 *
 *
 *
 * <h3 id="29">29. <a class="meaningful_link" href="#BDD_behavior_verification" name="BDD_behavior_verification">BDD style verification</a> (Since 1.10.0)</h3>
 *
 * Enables Behavior Driven Development (BDD) style verification by starting verification with the BDD <b>then</b> keyword.
 *
 * <pre class="code"><code class="java">
 * given(dog.bark()).willReturn(2);
 *
 * // when
 * ...
 *
 * then(person).should(times(2)).ride(bike);
 * </code></pre>
 *
 * For more information and an example see {@link BDDMockito#then(Object)}
 *
 *
 *
 *
 * <h3 id="30">30. <a class="meaningful_link" href="#spying_abstract_classes" name="spying_abstract_classes">Spying or mocking abstract classes (Since 1.10.12, further enhanced in 2.7.13 and 2.7.14)</a></h3>
 *
 * It is now possible to conveniently spy on abstract classes. Note that overusing spies hints at code design smells (see {@link #spy(Object)}).
 * <p>
 * Previously, spying was only possible on instances of objects.
 * New API makes it possible to use constructor when creating an instance of the mock.
 * This is particularly useful for mocking abstract classes because the user is no longer required to provide an instance of the abstract class.
 * At the moment, only parameter-less constructor is supported, let us know if it is not enough.
 *
 * <pre class="code"><code class="java">
 * //convenience API, new overloaded spy() method:
 * SomeAbstract spy = spy(SomeAbstract.class);
 *
 * //Mocking abstract methods, spying default methods of an interface (only available since 2.7.13)
 * Function<Foo, Bar> function = spy(Function.class);
 *
 * //Robust API, via settings builder:
 * OtherAbstract spy = mock(OtherAbstract.class, withSettings()
 *    .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
 *
 * //Mocking an abstract class with constructor arguments (only available since 2.7.14)
 * SomeAbstract spy = mock(SomeAbstract.class, withSettings()
 *   .useConstructor("arg1", 123).defaultAnswer(CALLS_REAL_METHODS));
 *
 * //Mocking a non-static inner abstract class:
 * InnerAbstract spy = mock(InnerAbstract.class, withSettings()
 *    .useConstructor().outerInstance(outerInstance).defaultAnswer(CALLS_REAL_METHODS));
 * </code></pre>
 *
 * For more information please see {@link MockSettings#useConstructor(Object...)}.
 *
 *
 *
 *
 * <h3 id="31">31. <a class="meaningful_link" href="#serilization_across_classloader" name="serilization_across_classloader">Mockito mocks can be <em>serialized</em> / <em>deserialized</em> across classloaders (Since 1.10.0)</a></h3>
 *
 * Mockito introduces serialization across classloader.
 *
 * Like with any other form of serialization, all types in the mock hierarchy have to serializable, including answers.
 * As this serialization mode require considerably more work, this is an opt-in setting.
 *
 * <pre class="code"><code class="java">
 * // use regular serialization
 * mock(Book.class, withSettings().serializable());
 *
 * // use serialization across classloaders
 * mock(Book.class, withSettings().serializable(ACROSS_CLASSLOADERS));
 * </code></pre>
 *
 * For more details see {@link MockSettings#serializable(SerializableMode)}.
 *
 *
 *
 *
 * <h3 id="32">32. <a class="meaningful_link" href="#better_generic_support_with_deep_stubs" name="better_generic_support_with_deep_stubs">Better generic support with deep stubs (Since 1.10.0)</a></h3>
 *
 * Deep stubbing has been improved to find generic information if available in the class.
 * That means that classes like this can be used without having to mock the behavior.
 *
 * <pre class="code"><code class="java">
 * class Lines extends List&lt;Line&gt; {
 *     // ...
 * }
 *
 * lines = mock(Lines.class, RETURNS_DEEP_STUBS);
 *
 * // Now Mockito understand this is not an Object but a Line
 * Line line = lines.iterator().next();
 * </code></pre>
 *
 * Please note that in most scenarios a mock returning a mock is wrong.
 *
 *
 *
 *
 * <h3 id="33">33. <a class="meaningful_link" href="#mockito_junit_rule" name="mockito_junit_rule">Mockito JUnit rule (Since 1.10.17)</a></h3>
 *
 * Mockito now offers a JUnit rule. Until now in JUnit there were two ways to initialize fields annotated by Mockito annotations
 * such as <code>&#064;{@link Mock}</code>, <code>&#064;{@link Spy}</code>, <code>&#064;{@link InjectMocks}</code>, etc.
 *
 * <ul>
 *     <li>Annotating the JUnit test class with a <code>&#064;{@link org.junit.runner.RunWith}({@link MockitoJUnitRunner}.class)</code></li>
 *     <li>Invoking <code>{@link MockitoAnnotations#openMocks(Object)}</code> in the <code>&#064;{@link org.junit.Before}</code> method</li>
 * </ul>
 *
 * Now you can choose to use a rule :
 *
 * <pre class="code"><code class="java">
 * &#064;RunWith(YetAnotherRunner.class)
 * public class TheTest {
 *     &#064;Rule public MockitoRule mockito = MockitoJUnit.rule();
 *     // ...
 * }
 * </code></pre>
 *
 * For more information see {@link MockitoJUnit#rule()}.
 *
 *
 *
 *
 * <h3 id="34">34. <a class="meaningful_link" href="#plugin_switch" name="plugin_switch">Switch <em>on</em> or <em>off</em> plugins (Since 1.10.15)</a></h3>
 *
 * An incubating feature made it's way in mockito that will allow to toggle a mockito-plugin.
 *
 * More information here {@link org.mockito.plugins.PluginSwitch}.
 *
 *
 * <h3 id="35">35. <a class="meaningful_link" href="#Custom_verification_failure_message" name="Custom_verification_failure_message">Custom verification failure message</a> (Since 2.1.0)</h3>
 * <p>
 * Allows specifying a custom message to be printed if verification fails.
 * <p>
 * Examples:
 * <p>
 * <pre class="code"><code class="java">
 *
 * // will print a custom message on verification failure
 * verify(mock, description("This will print on failure")).someMethod();
 *
 * // will work with any verification mode
 * verify(mock, times(2).description("someMethod should be called twice")).someMethod();
 * </code></pre>
 *
 * <h3 id="36">36. <a class="meaningful_link" href="#Java_8_Lambda_Matching" name="Java_8_Lambda_Matching">Java 8 Lambda Matcher Support</a> (Since 2.1.0)</h3>
 * <p>
 * You can use Java 8 lambda expressions with {@link ArgumentMatcher} to reduce the dependency on {@link ArgumentCaptor}.
 * If you need to verify that the input to a function call on a mock was correct, then you would normally
 * use the {@link ArgumentCaptor} to find the operands used and then do subsequent assertions on them. While
 * for complex examples this can be useful, it's also long-winded.<p>
 * Writing a lambda to express the match is quite easy. The argument to your function, when used in conjunction
 * with argThat, will be passed to the ArgumentMatcher as a strongly typed object, so it is possible
 * to do anything with it.
 * <p>
 * Examples:
 * <p>
 * <pre class="code"><code class="java">
 *
 * // verify a list only had strings of a certain length added to it
 * // note - this will only compile under Java 8
 * verify(list, times(2)).add(argThat(string -> string.length() < 5));
 *
 * // Java 7 equivalent - not as neat
 * verify(list, times(2)).add(argThat(new ArgumentMatcher<String>(){
 *     public boolean matches(String arg) {
 *         return arg.length() < 5;
 *     }
 * }));
 *
 * // more complex Java 8 example - where you can specify complex verification behaviour functionally
 * verify(target, times(1)).receiveComplexObject(argThat(obj -> obj.getSubObject().get(0).equals("expected")));
 *
 * // this can also be used when defining the behaviour of a mock under different inputs
 * // in this case if the input list was fewer than 3 items the mock returns null
 * when(mock.someMethod(argThat(list -> list.size()<3))).thenReturn(null);
 * </code></pre>
 *
 * <h3 id="37">37. <a class="meaningful_link" href="#Java_8_Custom_Answers" name="Java_8_Custom_Answers">Java 8 Custom Answer Support</a> (Since 2.1.0)</h3>
 * <p>
 * As the {@link Answer} interface has just one method it is already possible to implement it in Java 8 using
 * a lambda expression for very simple situations. The more you need to use the parameters of the method call,
 * the more you need to typecast the arguments from {@link org.mockito.invocation.InvocationOnMock}.
 *
 * <p>
 * Examples:
 * <p>
 * <pre class="code"><code class="java">
 * // answer by returning 12 every time
 * doAnswer(invocation -> 12).when(mock).doSomething();
 *
 * // answer by using one of the parameters - converting into the right
 * // type as your go - in this case, returning the length of the second string parameter
 * // as the answer. This gets long-winded quickly, with casting of parameters.
 * doAnswer(invocation -> ((String)invocation.getArgument(1)).length())
 *     .when(mock).doSomething(anyString(), anyString(), anyString());
 * </code></pre>
 *
 * For convenience it is possible to write custom answers/actions, which use the parameters to the method call,
 * as Java 8 lambdas. Even in Java 7 and lower these custom answers based on a typed interface can reduce boilerplate.
 * In particular, this approach will make it easier to test functions which use callbacks.
 *
 * The methods {@link AdditionalAnswers#answer(Answer1) answer} and {@link AdditionalAnswers#answerVoid(VoidAnswer1) answerVoid}
 * can be used to create the answer. They rely on the related answer interfaces in {@link org.mockito.stubbing} that
 * support answers up to 5 parameters.
 *
 * <p>
 * Examples:
 * <p>
 * <pre class="code"><code class="java">
 *
 * // Example interface to be mocked has a function like:
 * void execute(String operand, Callback callback);
 *
 * // the example callback has a function and the class under test
 * // will depend on the callback being invoked
 * void receive(String item);
 *
 * // Java 8 - style 1
 * doAnswer(AdditionalAnswers.<String,Callback>answerVoid((operand, callback) -> callback.receive("dummy"))
 *     .when(mock).execute(anyString(), any(Callback.class));
 *
 * // Java 8 - style 2 - assuming static import of AdditionalAnswers
 * doAnswer(answerVoid((String operand, Callback callback) -> callback.receive("dummy"))
 *     .when(mock).execute(anyString(), any(Callback.class));
 *
 * // Java 8 - style 3 - where mocking function to is a static member of test class
 * private static void dummyCallbackImpl(String operation, Callback callback) {
 *     callback.receive("dummy");
 * }
 *
 * doAnswer(answerVoid(TestClass::dummyCallbackImpl)
 *     .when(mock).execute(anyString(), any(Callback.class));
 *
 * // Java 7
 * doAnswer(answerVoid(new VoidAnswer2<String, Callback>() {
 *     public void answer(String operation, Callback callback) {
 *         callback.receive("dummy");
 *     }})).when(mock).execute(anyString(), any(Callback.class));
 *
 * // returning a value is possible with the answer() function
 * // and the non-void version of the functional interfaces
 * // so if the mock interface had a method like
 * boolean isSameString(String input1, String input2);
 *
 * // this could be mocked
 * // Java 8
 * doAnswer(AdditionalAnswers.<Boolean,String,String>answer((input1, input2) -> input1.equals(input2))))
 *     .when(mock).execute(anyString(), anyString());
 *
 * // Java 7
 * doAnswer(answer(new Answer2<String, String, String>() {
 *     public String answer(String input1, String input2) {
 *         return input1 + input2;
 *     }})).when(mock).execute(anyString(), anyString());
 * </code></pre>
 *
 * <h3 id="38">38. <a class="meaningful_link" href="#Meta_Data_And_Generics" name="Meta_Data_And_Generics">Meta data and generic type retention</a> (Since 2.1.0)</h3>
 *
 * <p>
 * Mockito now preserves annotations on mocked methods and types as well as generic meta data. Previously, a mock type did not preserve
 * annotations on types unless they were explicitly inherited and never retained annotations on methods. As a consequence, the following
 * conditions now hold true:
 *
 * <pre class="code"><code class="java">
 * {@literal @}{@code MyAnnotation
 *  class Foo {
 *    List<String> bar() { ... }
 *  }
 *
 *  Class<?> mockType = mock(Foo.class).getClass();
 *  assert mockType.isAnnotationPresent(MyAnnotation.class);
 *  assert mockType.getDeclaredMethod("bar").getGenericReturnType() instanceof ParameterizedType;
 * }</code></pre>
 *
 * <p>
 * When using Java 8, Mockito now also preserves type annotations. This is default behavior and might not hold <a href="#28">if an
 * alternative {@link org.mockito.plugins.MockMaker} is used</a>.
 *
 * <h3 id="39">39. <a class="meaningful_link" href="#Mocking_Final" name="Mocking_Final">Mocking final types, enums and final methods</a> (Since 2.1.0)</h3>
 *
 * Mockito now offers an {@link Incubating}, optional support for mocking final classes and methods.
 * This is a fantastic improvement that demonstrates Mockito's everlasting quest for improving testing experience.
 * Our ambition is that Mockito "just works" with final classes and methods.
 * Previously they were considered <em>unmockable</em>, preventing the user from mocking.
 * We already started discussing how to make this feature enabled by default.
 * Currently, the feature is still optional as we wait for more feedback from the community.
 *
 * <p>
 * This alternative mock maker which uses
 * a combination of both Java instrumentation API and sub-classing rather than creating a new class to represent
 * a mock. This way, it becomes possible to mock final types and methods.
 *
 * <p>
 * This mock maker is <strong>turned off by default</strong> because it is based on completely different mocking mechanism
 * that requires more feedback from the community. It can be activated explicitly by the mockito extension mechanism,
 * just create in the classpath a file <code>/mockito-extensions/org.mockito.plugins.MockMaker</code>
 * containing the value <code>mock-maker-inline</code>.
 *
 * <p>
 * As a convenience, the Mockito team provides an artifact where this mock maker is preconfigured. Instead of using the
 * <i>mockito-core</i> artifact, include the <i>mockito-inline</i> artifact in your project. Note that this artifact is
 * likely to be discontinued once mocking of final classes and methods gets integrated into the default mock maker.
 *
 * <p>
 * Some noteworthy notes about this mock maker:
 * <ul>
 *     <li>Mocking final types and enums is incompatible with mock settings like :
 *     <ul>
 *         <li>explicitly serialization support <code>withSettings().serializable()</code></li>
 *         <li>extra-interfaces <code>withSettings().extraInterfaces()</code></li>
 *     </ul>
 *     </li>
 *     <li>Some methods cannot be mocked
 *         <ul>
 *              <li>Package-visible methods of <code>java.*</code></li>
 *              <li><code>native</code> methods</li>
 *         </ul>
 *     </li>
 *     <li>This mock maker has been designed around Java Agent runtime attachment ; this require a compatible JVM,
 *     that is part of the JDK (or Java 9 VM). When running on a non-JDK VM prior to Java 9, it is however possible to
 *     manually add the <a href="http://bytebuddy.net">Byte Buddy Java agent jar</a> using the <code>-javaagent</code>
 *     parameter upon starting the JVM.
 *     </li>
 * </ul>
 *
 * <p>
 * If you are interested in more details of this feature please read the javadoc of
 * <code>org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker</code>
 *
 * <h3 id="40">40. <a class="meaningful_link" href="#strict_mockito" name="strict_mockito">
 *     Improved productivity and cleaner tests with "stricter" Mockito</a> (Since 2.+)</h3>
 *
 * To quickly find out how "stricter" Mockito can make you more productive and get your tests cleaner, see:
 * <ul>
 *     <li>Strict stubbing with JUnit Rules - {@link MockitoRule#strictness(Strictness)} with {@link Strictness#STRICT_STUBS}</li>
 *     <li>Strict stubbing with JUnit Runner - {@link MockitoJUnitRunner.StrictStubs}</li>
 *     <li>Strict stubbing if you cannot use runner/rule (like TestNG) - {@link MockitoSession}</li>
 *     <li>Unnecessary stubbing detection with {@link MockitoJUnitRunner}</li>
 *     <li>Stubbing argument mismatch warnings, documented in {@link MockitoHint}</li>
 * </ul>
 *
 * Mockito is a "loose" mocking framework by default.
 * Mocks can be interacted with without setting any expectations beforehand.
 * This is intentional and it improves the quality of tests by forcing users to be explicit about what they want to stub / verify.
 * It is also very intuitive, easy to use and blends nicely with "given", "when", "then" template of clean test code.
 * This is also different from the classic mocking frameworks of the past, they were "strict" by default.
 * <p>
 * Being "loose" by default makes Mockito tests harder to debug at times.
 * There are scenarios where misconfigured stubbing (like using a wrong argument) forces the user to run the test with a debugger.
 * Ideally, tests failures are immediately obvious and don't require debugger to identify the root cause.
 * Starting with version 2.1 Mockito has been getting new features that nudge the framework towards "strictness".
 * We want Mockito to offer fantastic debuggability while not losing its core mocking style, optimized for
 * intuitiveness, explicitness and clean test code.
 * <p>
 * Help Mockito! Try the new features, give us feedback, join the discussion about Mockito strictness at GitHub
 * <a href="https://github.com/mockito/mockito/issues/769">issue 769</a>.
 *
 * <h3 id="41">41. <a class="meaningful_link" href="#framework_integrations_api" name="framework_integrations_api">
 *      Advanced public API for framework integrations (Since 2.10.+)</a></h3>
 *
 * In Summer 2017 we decided that Mockito
 * <a href="https://www.linkedin.com/pulse/mockito-vs-powermock-opinionated-dogmatic-static-mocking-faber">
 * should offer better API
 * </a>
 * for advanced framework integrations.
 * The new API is not intended for users who want to write unit tests.
 * It is intended for other test tools and mocking frameworks that need to extend or wrap Mockito with some custom logic.
 * During the design and implementation process (<a href="https://github.com/mockito/mockito/issues/1110">issue 1110</a>)
 * we have developed and changed following public API elements:
 * <ul>
 *     <li>New {@link MockitoPlugins} -
 *      Enables framework integrators to get access to default Mockito plugins.
 *      Useful when one needs to implement custom plugin such as {@link MockMaker}
 *      and delegate some behavior to the default Mockito implementation.
 *     </li>
 *     <li>New {@link MockSettings#build(Class)} -
 *      Creates immutable view of mock settings used later by Mockito.
 *      Useful for creating invocations with {@link InvocationFactory} or when implementing custom {@link MockHandler}.
 *     </li>
 *     <li>New {@link MockingDetails#getMockHandler()} -
 *      Other frameworks may use the mock handler to programmatically simulate invocations on mock objects.
 *     </li>
 *     <li>New {@link MockHandler#getMockSettings()} -
 *      Useful to get hold of the setting the mock object was created with.
 *     </li>
 *     <li>New {@link InvocationFactory} -
 *      Provides means to create instances of {@link Invocation} objects.
 *      Useful for framework integrations that need to programmatically simulate method calls on mock objects.
 *     </li>
 *     <li>New {@link MockHandler#getInvocationContainer()} -
 *      Provides access to invocation container object which has no methods (marker interface).
 *      Container is needed to hide the internal implementation and avoid leaking it to the public API.
 *     </li>
 *     <li>Changed {@link Stubbing} -
 *      it now extends {@link Answer} interface.
 *      It is backwards compatible because Stubbing interface is not extensible (see {@link NotExtensible}).
 *      The change should be seamless to our users.
 *     </li>
 *     <li>Deprecated {@link InternalMockHandler} -
 *       In order to accommodate API changes we needed to deprecate this interface.
 *       The interface was always documented as internal, we don't have evidence it was used by the community.
 *       The deprecation should be completely seamless for our users.
 *     </li>
 *     <li>{@link NotExtensible} -
 *       Public annotation that indicates to the user that she should not provide custom implementations of given type.
 *       Helps framework integrators and our users understand how to use Mockito API safely.
 *     </li>
 * </ul>
 * Do you have feedback? Please leave comment in <a href="https://github.com/mockito/mockito/issues/1110">issue 1110</a>.
 *
 * <h3 id="42">42. <a class="meaningful_link" href="#verifiation_started_listener" name="verifiation_started_listener">
 *       New API for integrations: listening on verification start events (Since 2.11.+)</a></h3>
 *
 * Framework integrations such as <a href="https://projects.spring.io/spring-boot">Spring Boot</a> needs public API to tackle double-proxy use case
 * (<a href="https://github.com/mockito/mockito/issues/1191">issue 1191</a>).
 * We added:
 * <ul>
 *     <li>New {@link VerificationStartedListener} and {@link VerificationStartedEvent}
 *      enable framework integrators to replace the mock object for verification.
 *      The main driving use case is <a href="https://projects.spring.io/spring-boot/">Spring Boot</a> integration.
 *      For details see Javadoc for {@link VerificationStartedListener}.
 *     </li>
 *     <li>New public method {@link MockSettings#verificationStartedListeners(VerificationStartedListener...)}
 *     allows to supply verification started listeners at mock creation time.
 *     </li>
 *     <li>New handy method {@link MockingDetails#getMock()} was added to make the {@code MockingDetails} API more complete.
 *     We found this method useful during the implementation.
 *     </li>
 * </ul>
 *
 * <h3 id="43">43. <a class="meaningful_link" href="#mockito_session_testing_frameworks" name="mockito_session_testing_frameworks">
 *       New API for integrations: <code>MockitoSession</code> is usable by testing frameworks (Since 2.15.+)</a></h3>
 *
 * <p>{@link MockitoSessionBuilder} and {@link MockitoSession} were enhanced to enable reuse by testing framework
 * integrations (e.g. {@link MockitoRule} for JUnit):</p>
 * <ul>
 *     <li>{@link MockitoSessionBuilder#initMocks(Object...)} allows to pass in multiple test class instances for
 *      initialization of fields annotated with Mockito annotations like {@link org.mockito.Mock}.
 *      This method is useful for advanced framework integrations (e.g. JUnit Jupiter), when a test uses multiple,
 *      e.g. nested, test class instances.
 *     </li>
 *     <li>{@link MockitoSessionBuilder#name(String)} allows to pass a name from the testing framework to the
 *      {@link MockitoSession} that will be used for printing warnings when {@link Strictness#WARN} is used.
 *     </li>
 *     <li>{@link MockitoSessionBuilder#logger(MockitoSessionLogger)} makes it possible to customize the logger used
 *      for hints/warnings produced when finishing mocking (useful for testing and to connect reporting capabilities
 *      provided by testing frameworks such as JUnit Jupiter).
 *     </li>
 *     <li>{@link MockitoSession#setStrictness(Strictness)} allows to change the strictness of a {@link MockitoSession}
 *      for one-off scenarios, e.g. it enables configuring a default strictness for all tests in a class but makes it
 *      possible to change the strictness for a single or a few tests.
 *     </li>
 *     <li>{@link MockitoSession#finishMocking(Throwable)} was added to avoid confusion that may arise because
 *      there are multiple competing failures. It will disable certain checks when the supplied <em>failure</em>
 *      is not {@code null}.
 *     </li>
 * </ul>
 *
 * <h3 id="44">44. <a class="meaningful_link" href="#mockito_instantiator_provider_deprecation" name="mockito_instantiator_provider_deprecation">
 *       Deprecated <code>org.mockito.plugins.InstantiatorProvider</code> as it was leaking internal API. it was
 *       replaced by <code>org.mockito.plugins.InstantiatorProvider2 (Since 2.15.4)</code></a></h3>
 *
 * <p>{@link org.mockito.plugins.InstantiatorProvider} returned an internal API. Hence it was deprecated and replaced
 * by {@link org.mockito.plugins.InstantiatorProvider2}. Old {@link org.mockito.plugins.InstantiatorProvider
 * instantiator providers} will continue to work, but it is recommended to switch to the new API.</p>
 *
 * <h3 id="45">45. <a class="meaningful_link" href="#junit5_mockito" name="junit5_mockito">New JUnit Jupiter (JUnit5+) extension</a></h3>
 *
 * For integration with JUnit Jupiter (JUnit5+), use the `org.mockito:mockito-junit-jupiter` artifact.
 * For more information about the usage of the integration, see <a href="http://javadoc.io/doc/org.mockito/mockito-junit-jupiter/latest/org/mockito/junit/jupiter/MockitoExtension.html">the JavaDoc of <code>MockitoExtension</code></a>.
 *
 * <h3 id="46">46. <a class="meaningful_link" href="#mockito_lenient" name="mockito_lenient">
 *       New <code>Mockito.lenient()</code> and <code>MockSettings.lenient()</code> methods (Since 2.20.0)</a></h3>
 *
 * Strict stubbing feature is available since early Mockito 2.
 * It is very useful because it drives cleaner tests and improved productivity.
 * Strict stubbing reports unnecessary stubs, detects stubbing argument mismatch and makes the tests more DRY ({@link Strictness#STRICT_STUBS}).
 * This comes with a trade-off: in some cases, you may get false negatives from strict stubbing.
 * To remedy those scenarios you can now configure specific stubbing to be lenient, while all the other stubbings and mocks use strict stubbing:
 *
 * <pre class="code"><code class="java">
 *   lenient().when(mock.foo()).thenReturn("ok");
 * </code></pre>
 *
 * If you want all the stubbings on a given mock to be lenient, you can configure the mock accordingly:
 *
 * <pre class="code"><code class="java">
 *   Foo mock = Mockito.mock(Foo.class, withSettings().lenient());
 * </code></pre>
 *
 * For more information refer to {@link Mockito#lenient()}.
 * Let us know how do you find the new feature by opening a GitHub issue to discuss!
 *
 * <h3 id="47">47. <a class="meaningful_link" href="#clear_inline_mocks" name="clear_inline_mocks">New API for clearing mock state in inline mocking (Since 2.25.0)</a></h3>
 *
 * In certain specific, rare scenarios (issue <a href="https://github.com/mockito/mockito/pull/1619">#1619</a>)
 * inline mocking causes memory leaks.
 * There is no clean way to mitigate this problem completely.
 * Hence, we introduced a new API to explicitly clear mock state (only make sense in inline mocking!).
 * See example usage in {@link MockitoFramework#clearInlineMocks()}.
 * If you have feedback or a better idea how to solve the problem please reach out.
 *
 *
 * <h3 id="48">48. <a class="meaningful_link" href="#static_mocks" name="static_mocks">Mocking static methods</a> (since 3.4.0)</h3>
 *
 * When using the <a href="#0.2">inline mock maker</a>, it is possible to mock static method invocations within the current
 * thread and a user-defined scope. This way, Mockito assures that concurrently and sequentially running tests do not interfere.
 *
 * To make sure a static mock remains temporary, it is recommended to define the scope within a try-with-resources construct.
 * In the following example, the <code>Foo</code> type's static method would return <code>foo</code> unless mocked:
 *
 * <pre class="code"><code class="java">
 * assertEquals("foo", Foo.method());
 * try (MockedStatic<Foo> mocked = mockStatic(Foo.class)) {
 * mocked.when(Foo::method).thenReturn("bar");
 * assertEquals("bar", Foo.method());
 * mocked.verify(Foo::method);
 * }
 * assertEquals("foo", Foo.method());
 * </code></pre>
 *
 * Due to the defined scope of the static mock, it returns to its original behvior once the scope is released. To define mock
 * behavior and to verify static method invocations, use the <code>MockedStatic</code> that is returned.
 * <p>
 */
@SuppressWarnings("unchecked")
public class Mockito extends ArgumentMatchers {

    static final MockitoCore MOCKITO_CORE = new MockitoCore();

    /**
     * The default <code>Answer</code> of every mock <b>if</b> the mock was not stubbed.
     *
     * Typically it just returns some empty value.
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation first tries the global configuration and if there is no global configuration then
     * it will use a default answer that returns zeros, empty collections, nulls, etc.
     */
    public static final Answer<Object> RETURNS_DEFAULTS = Answers.RETURNS_DEFAULTS;

    /**
     * Optional <code>Answer</code> to be used with {@link Mockito#mock(Class, Answer)}.
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation can be helpful when working with legacy code.
     * Unstubbed methods often return null. If your code uses the object returned by an unstubbed call you get a NullPointerException.
     * This implementation of Answer <b>returns SmartNull instead of null</b>.
     * <code>SmartNull</code> gives nicer exception message than NPE because it points out the line where unstubbed method was called. You just click on the stack trace.
     * <p>
     * <code>ReturnsSmartNulls</code> first tries to return ordinary values (zeros, empty collections, empty string, etc.)
     * then it tries to return SmartNull. If the return type is final then plain <code>null</code> is returned.
     * <p>
     * <code>ReturnsSmartNulls</code> will be probably the default return values strategy in Mockito 4.0.0
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
     *
     *   //calling unstubbed method here:
     *   Stuff stuff = mock.getStuff();
     *
     *   //using object returned by unstubbed call:
     *   stuff.doSomething();
     *
     *   //Above doesn't yield NullPointerException this time!
     *   //Instead, SmartNullPointerException is thrown.
     *   //Exception's cause links to unstubbed <i>mock.getStuff()</i> - just click on the stack trace.
     * </code></pre>
     */
    public static final Answer<Object> RETURNS_SMART_NULLS = Answers.RETURNS_SMART_NULLS;

    /**
     * Optional <code>Answer</code> to be used with {@link Mockito#mock(Class, Answer)}
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation can be helpful when working with legacy code.
     * <p>
     * ReturnsMocks first tries to return ordinary values (zeros, empty collections, empty string, etc.)
     * then it tries to return mocks. If the return type cannot be mocked (e.g. is final) then plain <code>null</code> is returned.
     * <p>
     */
    public static final Answer<Object> RETURNS_MOCKS = Answers.RETURNS_MOCKS;

    /**
     * Optional <code>Answer</code> to be used with {@link Mockito#mock(Class, Answer)}.
     * <p>
     * Example that shows how deep stub works:
     * <pre class="code"><code class="java">
     *   Foo mock = mock(Foo.class, RETURNS_DEEP_STUBS);
     *
     *   // note that we're stubbing a chain of methods here: getBar().getName()
     *   when(mock.getBar().getName()).thenReturn("deep");
     *
     *   // note that we're chaining method calls: getBar().getName()
     *   assertEquals("deep", mock.getBar().getName());
     * </code></pre>
     * </p>
     *
     * <p>
     * <strong>WARNING: </strong>
     * This feature should rarely be required for regular clean code! Leave it for legacy code.
     * Mocking a mock to return a mock, to return a mock, (...), to return something meaningful
     * hints at violation of Law of Demeter or mocking a value object (a well known anti-pattern).
     * </p>
     *
     * <p>
     * Good quote I've seen one day on the web: <strong>every time a mock returns a mock a fairy dies</strong>.
     * </p>
     *
     * <p>
     * Please note that this answer will return existing mocks that matches the stub. This
     * behavior is ok with deep stubs and allows verification to work on the last mock of the chain.
     * <pre class="code"><code class="java">
     *   when(mock.getBar(anyString()).getThingy().getName()).thenReturn("deep");
     *
     *   mock.getBar("candy bar").getThingy().getName();
     *
     *   assertSame(mock.getBar(anyString()).getThingy().getName(), mock.getBar(anyString()).getThingy().getName());
     *   verify(mock.getBar("candy bar").getThingy()).getName();
     *   verify(mock.getBar(anyString()).getThingy()).getName();
     * </code></pre>
     * </p>
     *
     * <p>
     * Verification only works with the last mock in the chain. You can use verification modes.
     * <pre class="code"><code class="java">
     *   when(person.getAddress(anyString()).getStreet().getName()).thenReturn("deep");
     *   when(person.getAddress(anyString()).getStreet(Locale.ITALIAN).getName()).thenReturn("deep");
     *   when(person.getAddress(anyString()).getStreet(Locale.CHINESE).getName()).thenReturn("deep");
     *
     *   person.getAddress("the docks").getStreet().getName();
     *   person.getAddress("the docks").getStreet().getLongName();
     *   person.getAddress("the docks").getStreet(Locale.ITALIAN).getName();
     *   person.getAddress("the docks").getStreet(Locale.CHINESE).getName();
     *
     *   // note that we are actually referring to the very last mock in the stubbing chain.
     *   InOrder inOrder = inOrder(
     *       person.getAddress("the docks").getStreet(),
     *       person.getAddress("the docks").getStreet(Locale.CHINESE),
     *       person.getAddress("the docks").getStreet(Locale.ITALIAN)
     *   );
     *   inOrder.verify(person.getAddress("the docks").getStreet(), times(1)).getName();
     *   inOrder.verify(person.getAddress("the docks").getStreet()).getLongName();
     *   inOrder.verify(person.getAddress("the docks").getStreet(Locale.ITALIAN), atLeast(1)).getName();
     *   inOrder.verify(person.getAddress("the docks").getStreet(Locale.CHINESE)).getName();
     * </code></pre>
     * </p>
     *
     * <p>
     * How deep stub work internally?
     * <pre class="code"><code class="java">
     *   //this:
     *   Foo mock = mock(Foo.class, RETURNS_DEEP_STUBS);
     *   when(mock.getBar().getName(), "deep");
     *
     *   //is equivalent of
     *   Foo foo = mock(Foo.class);
     *   Bar bar = mock(Bar.class);
     *   when(foo.getBar()).thenReturn(bar);
     *   when(bar.getName()).thenReturn("deep");
     * </code></pre>
     * </p>
     *
     * <p>
     * This feature will not work when any return type of methods included in the chain cannot be mocked
     * (for example: is a primitive or a final class). This is because of java type system.
     * </p>
     */
    public static final Answer<Object> RETURNS_DEEP_STUBS = Answers.RETURNS_DEEP_STUBS;

    /**
     * Optional <code>Answer</code> to be used with {@link Mockito#mock(Class, Answer)}
     *
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation can be helpful when working with legacy code.
     * When this implementation is used, unstubbed methods will delegate to the real implementation.
     * This is a way to create a partial mock object that calls real methods by default.
     * <p>
     * As usual you are going to read <b>the partial mock warning</b>:
     * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
     * How does partial mock fit into this paradigm? Well, it just doesn't...
     * Partial mock usually means that the complexity has been moved to a different method on the same object.
     * In most cases, this is not the way you want to design your application.
     * <p>
     * However, there are rare cases when partial mocks come handy:
     * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
     * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     * Foo mock = mock(Foo.class, CALLS_REAL_METHODS);
     *
     * // this calls the real implementation of Foo.getSomething()
     * value = mock.getSomething();
     *
     * doReturn(fakeValue).when(mock).getSomething();
     *
     * // now fakeValue is returned
     * value = mock.getSomething();
     * </code></pre>
     *
     * <p>
     * <u>Note 1:</u> Stubbing partial mocks using <code>when(mock.getSomething()).thenReturn(fakeValue)</code>
     * syntax will call the real method. For partial mock it's recommended to use <code>doReturn</code> syntax.
     * <p>
     * <u>Note 2:</u> If the mock is serialized then deserialized, then this answer will not be able to understand
     * generics metadata.
     */
    public static final Answer<Object> CALLS_REAL_METHODS = Answers.CALLS_REAL_METHODS;

    /**
     * Optional <code>Answer</code> to be used with {@link Mockito#mock(Class, Answer)}.
     *
     * Allows Builder mocks to return itself whenever a method is invoked that returns a Type equal
     * to the class or a superclass.
     *
     * <p><b>Keep in mind this answer uses the return type of a method.
     * If this type is assignable to the class of the mock, it will return the mock.
     * Therefore if you have a method returning a superclass (for example {@code Object}) it will match and return the mock.</b></p>
     *
     * Consider a HttpBuilder used in a HttpRequesterWithHeaders.
     *
     * <pre class="code"><code class="java">
     * public class HttpRequesterWithHeaders {
     *
     *      private HttpBuilder builder;
     *
     *      public HttpRequesterWithHeaders(HttpBuilder builder) {
     *          this.builder = builder;
     *      }
     *
     *      public String request(String uri) {
     *          return builder.withUrl(uri)
     *                  .withHeader("Content-type: application/json")
     *                  .withHeader("Authorization: Bearer")
     *                  .request();
     *      }
     *  }
     *
     *  private static class HttpBuilder {
     *
     *      private String uri;
     *      private List&lt;String&gt; headers;
     *
     *      public HttpBuilder() {
     *          this.headers = new ArrayList&lt;String&gt;();
     *      }
     *
     *       public HttpBuilder withUrl(String uri) {
     *           this.uri = uri;
     *           return this;
     *       }
     *
     *       public HttpBuilder withHeader(String header) {
     *           this.headers.add(header);
     *           return this;
     *       }
     *
     *       public String request() {
     *          return uri + headers.toString();
     *       }
     *  }
     * </code></pre>
     *
     * The following test will succeed
     *
     * <pre><code>
     * &#064;Test
     *  public void use_full_builder_with_terminating_method() {
     *      HttpBuilder builder = mock(HttpBuilder.class, RETURNS_SELF);
     *      HttpRequesterWithHeaders requester = new HttpRequesterWithHeaders(builder);
     *      String response = "StatusCode: 200";
     *
     *      when(builder.request()).thenReturn(response);
     *
     *      assertThat(requester.request("URI")).isEqualTo(response);
     *  }
     * </code></pre>
     */
    public static final Answer<Object> RETURNS_SELF = Answers.RETURNS_SELF;

    /**
     * Creates mock object of given class or interface.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface to mock
     * @return mock object
     */
    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock) {
        return mock(classToMock, withSettings());
    }

    /**
     * Specifies mock name. Naming mocks can be helpful for debugging - the name is used in all verification errors.
     * <p>
     * Beware that naming mocks is not a solution for complex code which uses too many mocks or collaborators.
     * <b>If you have too many mocks then refactor the code</b> so that it's easy to test/debug without necessity of naming mocks.
     * <p>
     * <b>If you use <code>&#064;Mock</code> annotation then you've got naming mocks for free!</b> <code>&#064;Mock</code> uses field name as mock name. {@link Mock Read more.}
     * <p>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface to mock
     * @param name of the mock
     * @return mock object
     */
    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, String name) {
        return mock(classToMock, withSettings().name(name).defaultAnswer(RETURNS_DEFAULTS));
    }

    /**
     * Returns a MockingDetails instance that enables inspecting a particular object for Mockito related information.
     * Can be used to find out if given object is a Mockito mock
     * or to find out if a given mock is a spy or mock.
     * <p>
     * In future Mockito versions MockingDetails may grow and provide other useful information about the mock,
     * e.g. invocations, stubbing info, etc.
     *
     * @param toInspect - object to inspect. null input is allowed.
     * @return A {@link org.mockito.MockingDetails} instance.
     * @since 1.9.5
     */
    @CheckReturnValue
    public static MockingDetails mockingDetails(Object toInspect) {
        return MOCKITO_CORE.mockingDetails(toInspect);
    }

    /**
     * Creates mock with a specified strategy for its answers to interactions.
     * It's quite an advanced feature and typically you don't need it to write decent tests.
     * However it can be helpful when working with legacy systems.
     * <p>
     * It is the default answer so it will be used <b>only when you don't</b> stub the method call.
     *
     * <pre class="code"><code class="java">
     *   Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
     *   Foo mockTwo = mock(Foo.class, new YourOwnAnswer());
     * </code></pre>
     *
     * <p>See examples in javadoc for {@link Mockito} class</p>
     *
     * @param classToMock class or interface to mock
     * @param defaultAnswer default answer for unstubbed methods
     *
     * @return mock object
     */
    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return mock(classToMock, withSettings().defaultAnswer(defaultAnswer));
    }

    /**
     * Creates a mock with some non-standard settings.
     * <p>
     * The number of configuration points for a mock grows
     * so we need a fluent way to introduce new configuration without adding more and more overloaded Mockito.mock() methods.
     * Hence {@link MockSettings}.
     * <pre class="code"><code class="java">
     *   Listener mock = mock(Listener.class, withSettings()
     *     .name("firstListner").defaultBehavior(RETURNS_SMART_NULLS));
     *   );
     * </code></pre>
     * <b>Use it carefully and occasionally</b>. What might be reason your test needs non-standard mocks?
     * Is the code under test so complicated that it requires non-standard mocks?
     * Wouldn't you prefer to refactor the code under test so it is testable in a simple way?
     * <p>
     * See also {@link Mockito#withSettings()}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface to mock
     * @param mockSettings additional mock settings
     * @return mock object
     */
    @CheckReturnValue
    public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return MOCKITO_CORE.mock(classToMock, mockSettings);
    }

    /**
     * Creates a spy of the real object. The spy calls <b>real</b> methods unless they are stubbed.
     * <p>
     * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
     * <p>
     * As usual you are going to read <b>the partial mock warning</b>:
     * Object oriented programming tackles complexity by dividing the complexity into separate, specific, SRPy objects.
     * How does partial mock fit into this paradigm? Well, it just doesn't...
     * Partial mock usually means that the complexity has been moved to a different method on the same object.
     * In most cases, this is not the way you want to design your application.
     * <p>
     * However, there are rare cases when partial mocks come handy:
     * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
     * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *
     *   //optionally, you can stub out some methods:
     *   when(spy.size()).thenReturn(100);
     *
     *   //using the spy calls <b>real</b> methods
     *   spy.add("one");
     *   spy.add("two");
     *
     *   //prints "one" - the first element of a list
     *   System.out.println(spy.get(0));
     *
     *   //size() method was stubbed - 100 is printed
     *   System.out.println(spy.size());
     *
     *   //optionally, you can verify
     *   verify(spy).add("one");
     *   verify(spy).add("two");
     * </code></pre>
     *
     * <h4>Important gotcha on spying real objects!</h4>
     * <ol>
     * <li>Sometimes it's impossible or impractical to use {@link Mockito#when(Object)} for stubbing spies.
     * Therefore for spies it is recommended to always use <code>doReturn</code>|<code>Answer</code>|<code>Throw()</code>|<code>CallRealMethod</code>
     * family of methods for stubbing. Example:
     *
     * <pre class="code"><code class="java">
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(spy.get(0)).thenReturn("foo");
     *
     *   //You have to use doReturn() for stubbing
     *   doReturn("foo").when(spy).get(0);
     * </code></pre>
     * </li>
     *
     * <li>Mockito <b>*does not*</b> delegate calls to the passed real instance, instead it actually creates a copy of it.
     * So if you keep the real instance and interact with it, don't expect the spied to be aware of those interaction
     * and their effect on real instance state.
     * The corollary is that when an <b>*unstubbed*</b> method is called <b>*on the spy*</b> but <b>*not on the real instance*</b>,
     * you won't see any effects on the real instance.</li>
     *
     * <li>Watch out for final methods.
     * Mockito doesn't mock final methods so the bottom line is: when you spy on real objects + you try to stub a final method = trouble.
     * Also you won't be able to verify those method as well.
     * </li>
     * </ol>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * <p>Note that the spy won't have any annotations of the spied type, because CGLIB won't rewrite them.
     * It may troublesome for code that rely on the spy to have these annotations.</p>
     *
     *
     * @param object
     *            to spy on
     * @return a spy of the real object
     */
    @CheckReturnValue
    public static <T> T spy(T object) {
        return MOCKITO_CORE.mock(
                (Class<T>) object.getClass(),
                withSettings().spiedInstance(object).defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Please refer to the documentation of {@link #spy(Object)}.
     * Overusing spies hints at code design smells.
     * <p>
     * This method, in contrast to the original {@link #spy(Object)}, creates a spy based on class instead of an object.
     * Sometimes it is more convenient to create spy based on the class and avoid providing an instance of a spied object.
     * This is particularly useful for spying on abstract classes because they cannot be instantiated.
     * See also {@link MockSettings#useConstructor(Object...)}.
     * <p>
     * Examples:
     * <pre class="code"><code class="java">
     *   SomeAbstract spy = spy(SomeAbstract.class);
     *
     *   //Robust API, via settings builder:
     *   OtherAbstract spy = mock(OtherAbstract.class, withSettings()
     *      .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
     *
     *   //Mocking a non-static inner abstract class:
     *   InnerAbstract spy = mock(InnerAbstract.class, withSettings()
     *      .useConstructor().outerInstance(outerInstance).defaultAnswer(CALLS_REAL_METHODS));
     * </code></pre>
     *
     * @param classToSpy the class to spy
     * @param <T> type of the spy
     * @return a spy of the provided class
     * @since 1.10.12
     */
    @Incubating
    @CheckReturnValue
    public static <T> T spy(Class<T> classToSpy) {
        return MOCKITO_CORE.mock(
                classToSpy, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
    }

    /**
     * Creates a thread-local mock controller for all static methods of the given class or interface.
     * The returned object's {@link MockedStatic#close()} method must be called upon completing the
     * test or the mock will remain active on the current thread.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface of which static mocks should be mocked.
     * @return mock controller
     */
    @Incubating
    @CheckReturnValue
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock) {
        return mockStatic(classToMock, withSettings());
    }

    /**
     * Creates a thread-local mock controller for all static methods of the given class or interface.
     * The returned object's {@link MockedStatic#close()} method must be called upon completing the
     * test or the mock will remain active on the current thread.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface of which static mocks should be mocked.
     * @param defaultAnswer the default answer when invoking static methods.
     * @return mock controller
     */
    @Incubating
    @CheckReturnValue
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, Answer defaultAnswer) {
        return mockStatic(classToMock, withSettings().defaultAnswer(defaultAnswer));
    }

    /**
     * Creates a thread-local mock controller for all static methods of the given class or interface.
     * The returned object's {@link MockedStatic#close()} method must be called upon completing the
     * test or the mock will remain active on the current thread.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface of which static mocks should be mocked.
     * @param name the name of the mock to use in error messages.
     * @return mock controller
     */
    @Incubating
    @CheckReturnValue
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, String name) {
        return mockStatic(classToMock, withSettings().name(name));
    }

    /**
     * Creates a thread-local mock controller for all static methods of the given class or interface.
     * The returned object's {@link MockedStatic#close()} method must be called upon completing the
     * test or the mock will remain active on the current thread.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param classToMock class or interface of which static mocks should be mocked.
     * @param mockSettings the settings to use where only name and default answer are considered.
     * @return mock controller
     */
    @Incubating
    @CheckReturnValue
    public static <T> MockedStatic<T> mockStatic(Class<T> classToMock, MockSettings mockSettings) {
        return MOCKITO_CORE.mockStatic(classToMock, mockSettings);
    }

    /**
     * Enables stubbing methods. Use it when you want the mock to return particular value when particular method is called.
     * <p>
     * Simply put: "<b>When</b> the x method is called <b>then</b> return y".
     *
     * <p>
     * Examples:
     *
     * <pre class="code"><code class="java">
     * <b>when</b>(mock.someMethod()).<b>thenReturn</b>(10);
     *
     * //you can use flexible argument matchers, e.g:
     * when(mock.someMethod(<b>anyString()</b>)).thenReturn(10);
     *
     * //setting exception to be thrown:
     * when(mock.someMethod("some arg")).thenThrow(new RuntimeException());
     *
     * //you can set different behavior for consecutive method calls.
     * //Last stubbing (e.g: thenReturn("foo")) determines the behavior of further consecutive calls.
     * when(mock.someMethod("some arg"))
     *  .thenThrow(new RuntimeException())
     *  .thenReturn("foo");
     *
     * //Alternative, shorter version for consecutive stubbing:
     * when(mock.someMethod("some arg"))
     *  .thenReturn("one", "two");
     * //is the same as:
     * when(mock.someMethod("some arg"))
     *  .thenReturn("one")
     *  .thenReturn("two");
     *
     * //shorter version for consecutive method calls throwing exceptions:
     * when(mock.someMethod("some arg"))
     *  .thenThrow(new RuntimeException(), new NullPointerException();
     *
     * </code></pre>
     *
     * For stubbing void methods with throwables see: {@link Mockito#doThrow(Throwable...)}
     * <p>
     * Stubbing can be overridden: for example common stubbing can go to fixture
     * setup but the test methods can override it.
     * Please note that overridding stubbing is a potential code smell that points out too much stubbing.
     * <p>
     * Once stubbed, the method will always return stubbed value regardless
     * of how many times it is called.
     * <p>
     * Last stubbing is more important - when you stubbed the same method with
     * the same arguments many times.
     * <p>
     * Although it is possible to verify a stubbed invocation, usually <b>it's just redundant</b>.
     * Let's say you've stubbed <code>foo.bar()</code>.
     * If your code cares what <code>foo.bar()</code> returns then something else breaks(often before even <code>verify()</code> gets executed).
     * If your code doesn't care what <code>get(0)</code> returns then it should not be stubbed.
     *
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * @param methodCall method to be stubbed
     * @return OngoingStubbing object used to stub fluently.
     *         <strong>Do not</strong> create a reference to this returned object.
     */
    @CheckReturnValue
    public static <T> OngoingStubbing<T> when(T methodCall) {
        return MOCKITO_CORE.when(methodCall);
    }

    /**
     * Verifies certain behavior <b>happened once</b>.
     * <p>
     * Alias to <code>verify(mock, times(1))</code> E.g:
     * <pre class="code"><code class="java">
     *   verify(mock).someMethod("some arg");
     * </code></pre>
     * Above is equivalent to:
     * <pre class="code"><code class="java">
     *   verify(mock, times(1)).someMethod("some arg");
     * </code></pre>
     * <p>
     * Arguments passed are compared using <code>equals()</code> method.
     * Read about {@link ArgumentCaptor} or {@link ArgumentMatcher} to find out other ways of matching / asserting arguments passed.
     * <p>
     * Although it is possible to verify a stubbed invocation, usually <b>it's just redundant</b>.
     * Let's say you've stubbed <code>foo.bar()</code>.
     * If your code cares what <code>foo.bar()</code> returns then something else breaks(often before even <code>verify()</code> gets executed).
     * If your code doesn't care what <code>get(0)</code> returns then it should not be stubbed.
     *
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mock to be verified
     * @return mock object itself
     */
    @CheckReturnValue
    public static <T> T verify(T mock) {
        return MOCKITO_CORE.verify(mock, times(1));
    }

    /**
     * Verifies certain behavior happened at least once / exact number of times / never. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, times(5)).someMethod("was called five times");
     *
     *   verify(mock, atLeast(2)).someMethod("was called at least two times");
     *
     *   //you can use flexible argument matchers, e.g:
     *   verify(mock, atLeastOnce()).someMethod(<b>anyString()</b>);
     * </code></pre>
     *
     * <b>times(1) is the default</b> and can be omitted
     * <p>
     * Arguments passed are compared using <code>equals()</code> method.
     * Read about {@link ArgumentCaptor} or {@link ArgumentMatcher} to find out other ways of matching / asserting arguments passed.
     * <p>
     *
     * @param mock to be verified
     * @param mode times(x), atLeastOnce() or never()
     *
     * @return mock object itself
     */
    @CheckReturnValue
    public static <T> T verify(T mock, VerificationMode mode) {
        return MOCKITO_CORE.verify(mock, mode);
    }

    /**
     * Smart Mockito users hardly use this feature because they know it could be a sign of poor tests.
     * Normally, you don't need to reset your mocks, just create new mocks for each test method.
     * <p>
     * Instead of <code>#reset()</code> please consider writing simple, small and focused test methods over lengthy, over-specified tests.
     * <b>First potential code smell is <code>reset()</code> in the middle of the test method.</b> This probably means you're testing too much.
     * Follow the whisper of your test methods: "Please keep us small & focused on single behavior".
     * There are several threads about it on mockito mailing list.
     * <p>
     * The only reason we added <code>reset()</code> method is to
     * make it possible to work with container-injected mocks.
     * For more information see the FAQ (<a href="https://github.com/mockito/mockito/wiki/FAQ">here</a>).
     * <p>
     * <b>Don't harm yourself.</b> <code>reset()</code> in the middle of the test method is a code smell (you're probably testing too much).
     * <pre class="code"><code class="java">
     *   List mock = mock(List.class);
     *   when(mock.size()).thenReturn(10);
     *   mock.add(1);
     *
     *   reset(mock);
     *   //at this point the mock forgot any interactions & stubbing
     * </code></pre>
     *
     * @param <T> The Type of the mocks
     * @param mocks to be reset
     */
    public static <T> void reset(T... mocks) {
        MOCKITO_CORE.reset(mocks);
    }

    /**
     * Use this method in order to only clear invocations, when stubbing is non-trivial. Use-cases can be:
     * <ul>
     *     <li>You are using a dependency injection framework to inject your mocks.</li>
     *     <li>The mock is used in a stateful scenario. For example a class is Singleton which depends on your mock.</li>
     * </ul>
     *
     * <b>Try to avoid this method at all costs. Only clear invocations if you are unable to efficiently test your program.</b>
     * @param <T> The type of the mocks
     * @param mocks The mocks to clear the invocations for
     */
    public static <T> void clearInvocations(T... mocks) {
        MOCKITO_CORE.clearInvocations(mocks);
    }

    /**
     * Checks if any of given mocks has any unverified interaction.
     * <p>
     * You can use this method after you verified your mocks - to make sure that nothing
     * else was invoked on your mocks.
     * <p>
     * See also {@link Mockito#never()} - it is more explicit and communicates the intent well.
     * <p>
     * Stubbed invocations (if called) are also treated as interactions.
     * If you want stubbed invocations automatically verified, check out {@link Strictness#STRICT_STUBS} feature
     * introduced in Mockito 2.3.0.
     * If you want to ignore stubs for verification, see {@link #ignoreStubs(Object...)}.
     * <p>
     * A word of <b>warning</b>:
     * Some users who did a lot of classic, expect-run-verify mocking tend to use <code>verifyNoMoreInteractions()</code> very often, even in every test method.
     * <code>verifyNoMoreInteractions()</code> is not recommended to use in every test method.
     * <code>verifyNoMoreInteractions()</code> is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
     * Abusing it leads to overspecified, less maintainable tests.
     * <p>
     * This method will also detect unverified invocations that occurred before the test method,
     * for example: in <code>setUp()</code>, <code>&#064;Before</code> method or in constructor.
     * Consider writing nice code that makes interactions only in test methods.
     *
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     * //interactions
     * mock.doSomething();
     * mock.doSomethingUnexpected();
     *
     * //verification
     * verify(mock).doSomething();
     *
     * //following will fail because 'doSomethingUnexpected()' is unexpected
     * verifyNoMoreInteractions(mock);
     *
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mocks to be verified
     */
    public static void verifyNoMoreInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    /**
     * Verifies that no interactions happened on given mocks beyond the previously verified interactions.<br/>
     * This method has the same behavior as {@link #verifyNoMoreInteractions(Object...)}.
     *
     * @param mocks to be verified
     * @deprecated Since 3.0.1. Please migrate your code to {@link #verifyNoInteractions(Object...)}
     */
    @Deprecated
    public static void verifyZeroInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    /**
     * Verifies that no interactions happened on given mocks.
     * <pre class="code"><code class="java">
     *   verifyNoInteractions(mockOne, mockTwo);
     * </code></pre>
     * This method will also detect invocations
     * that occurred before the test method, for example: in <code>setUp()</code>, <code>&#064;Before</code> method or in constructor.
     * Consider writing nice code that makes interactions only in test methods.
     * <p>
     * See also {@link Mockito#never()} - it is more explicit and communicates the intent well.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mocks to be verified
     * @since 3.0.1
     */
    public static void verifyNoInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoInteractions(mocks);
    }

    /**
     * Use <code>doThrow()</code> when you want to stub the void method with an exception.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler
     * does not like void methods inside brackets...
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     *   doThrow(new RuntimeException()).when(mock).someVoidMethod();
     * </code></pre>
     *
     * @param toBeThrown to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    @CheckReturnValue
    public static Stubber doThrow(Throwable... toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
    }

    /**
     * Use <code>doThrow()</code> when you want to stub the void method with an exception.
     * <p>
     * A new exception instance will be created for each method invocation.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler
     * does not like void methods inside brackets...
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     *   doThrow(RuntimeException.class).when(mock).someVoidMethod();
     * </code></pre>
     *
     * @param toBeThrown to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     * @since 2.1.0
     */
    @CheckReturnValue
    public static Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown);
    }

    /**
     * Same as {@link #doThrow(Class)} but sets consecutive exception classes to be thrown. Remember to use
     * <code>doThrow()</code> when you want to stub the void method to throw several exception of specified class.
     * <p>
     * A new exception instance will be created for each method invocation.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler
     * does not like void methods inside brackets...
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     *   doThrow(RuntimeException.class, BigFailure.class).when(mock).someVoidMethod();
     * </code></pre>
     *
     * @param toBeThrown to be thrown when the stubbed method is called
     * @param toBeThrownNext next to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     * @since 2.1.0
     */
    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array
    // creation
    @SuppressWarnings({"unchecked", "varargs"})
    @CheckReturnValue
    public static Stubber doThrow(
            Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... toBeThrownNext) {
        return MOCKITO_CORE.stubber().doThrow(toBeThrown, toBeThrownNext);
    }

    /**
     * Use <code>doCallRealMethod()</code> when you want to call the real implementation of a method.
     * <p>
     * As usual you are going to read <b>the partial mock warning</b>:
     * Object oriented programming is more less tackling complexity by dividing the complexity into separate, specific, SRPy objects.
     * How does partial mock fit into this paradigm? Well, it just doesn't...
     * Partial mock usually means that the complexity has been moved to a different method on the same object.
     * In most cases, this is not the way you want to design your application.
     * <p>
     * However, there are rare cases when partial mocks come handy:
     * dealing with code you cannot change easily (3rd party interfaces, interim refactoring of legacy code etc.)
     * However, I wouldn't use partial mocks for new, test-driven & well-designed code.
     * <p>
     * See also javadoc {@link Mockito#spy(Object)} to find out more about partial mocks.
     * <b>Mockito.spy() is a recommended way of creating partial mocks.</b>
     * The reason is it guarantees real methods are called against correctly constructed object because you're responsible for constructing the object passed to spy() method.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   Foo mock = mock(Foo.class);
     *   doCallRealMethod().when(mock).someVoidMethod();
     *
     *   // this will call the real implementation of Foo.someVoidMethod()
     *   mock.someVoidMethod();
     * </code></pre>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return stubber - to select a method for stubbing
     * @since 1.9.5
     */
    @CheckReturnValue
    public static Stubber doCallRealMethod() {
        return MOCKITO_CORE.stubber().doCallRealMethod();
    }

    /**
     * Use <code>doAnswer()</code> when you want to stub a void method with generic {@link Answer}.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler does not like void methods inside brackets...
     * <p>
     * Example:
     *
     * <pre class="code"><code class="java">
     *  doAnswer(new Answer() {
     *      public Object answer(InvocationOnMock invocation) {
     *          Object[] args = invocation.getArguments();
     *          Mock mock = invocation.getMock();
     *          return null;
     *      }})
     *  .when(mock).someMethod();
     * </code></pre>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param answer to answer when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    @CheckReturnValue
    public static Stubber doAnswer(Answer answer) {
        return MOCKITO_CORE.stubber().doAnswer(answer);
    }

    /**
     * Use <code>doNothing()</code> for setting void methods to do nothing. <b>Beware that void methods on mocks do nothing by default!</b>
     * However, there are rare situations when doNothing() comes handy:
     * <p>
     * <ol>
     * <li>Stubbing consecutive calls on a void method:
     * <pre class="code"><code class="java">
     *   doNothing().
     *   doThrow(new RuntimeException())
     *   .when(mock).someVoidMethod();
     *
     *   //does nothing the first time:
     *   mock.someVoidMethod();
     *
     *   //throws RuntimeException the next time:
     *   mock.someVoidMethod();
     * </code></pre>
     * </li>
     * <li>When you spy real objects and you want the void method to do nothing:
     * <pre class="code"><code class="java">
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *
     *   //let's make clear() do nothing
     *   doNothing().when(spy).clear();
     *
     *   spy.add("one");
     *
     *   //clear() does nothing, so the list still contains "one"
     *   spy.clear();
     * </code></pre>
     * </li>
     * </ol>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return stubber - to select a method for stubbing
     */
    @CheckReturnValue
    public static Stubber doNothing() {
        return MOCKITO_CORE.stubber().doNothing();
    }

    /**
     * Use <code>doReturn()</code> in those rare occasions when you cannot use {@link Mockito#when(Object)}.
     * <p>
     * <b>Beware that {@link Mockito#when(Object)} is always recommended for stubbing because it is argument type-safe
     * and more readable</b> (especially when stubbing consecutive calls).
     * <p>
     * Here are those rare occasions when doReturn() comes handy:
     * <p>
     *
     * <ol>
     * <li>When spying real objects and calling real methods on a spy brings side effects
     *
     * <pre class="code"><code class="java">
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(spy.get(0)).thenReturn("foo");
     *
     *   //You have to use doReturn() for stubbing:
     *   doReturn("foo").when(spy).get(0);
     * </code></pre>
     * </li>
     *
     * <li>Overriding a previous exception-stubbing:
     * <pre class="code"><code class="java">
     *   when(mock.foo()).thenThrow(new RuntimeException());
     *
     *   //Impossible: the exception-stubbed foo() method is called so RuntimeException is thrown.
     *   when(mock.foo()).thenReturn("bar");
     *
     *   //You have to use doReturn() for stubbing:
     *   doReturn("bar").when(mock).foo();
     * </code></pre>
     * </li>
     * </ol>
     *
     * Above scenarios shows a tradeoff of Mockito's elegant syntax. Note that the scenarios are very rare, though.
     * Spying should be sporadic and overriding exception-stubbing is very rare. Not to mention that in general
     * overridding stubbing is a potential code smell that points out too much stubbing.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param toBeReturned to be returned when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    @CheckReturnValue
    public static Stubber doReturn(Object toBeReturned) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned);
    }

    /**
     * Same as {@link #doReturn(Object)} but sets consecutive values to be returned. Remember to use
     * <code>doReturn()</code> in those rare occasions when you cannot use {@link Mockito#when(Object)}.
     * <p>
     * <b>Beware that {@link Mockito#when(Object)} is always recommended for stubbing because it is argument type-safe
     * and more readable</b> (especially when stubbing consecutive calls).
     * <p>
     * Here are those rare occasions when doReturn() comes handy:
     * <p>
     *
     * <ol>
     * <li>When spying real objects and calling real methods on a spy brings side effects
     *
     * <pre class="code"><code class="java">
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(spy.get(0)).thenReturn("foo", "bar", "qix");
     *
     *   //You have to use doReturn() for stubbing:
     *   doReturn("foo", "bar", "qix").when(spy).get(0);
     * </code></pre>
     * </li>
     *
     * <li>Overriding a previous exception-stubbing:
     * <pre class="code"><code class="java">
     *   when(mock.foo()).thenThrow(new RuntimeException());
     *
     *   //Impossible: the exception-stubbed foo() method is called so RuntimeException is thrown.
     *   when(mock.foo()).thenReturn("bar", "foo", "qix");
     *
     *   //You have to use doReturn() for stubbing:
     *   doReturn("bar", "foo", "qix").when(mock).foo();
     * </code></pre>
     * </li>
     * </ol>
     *
     * Above scenarios shows a trade-off of Mockito's elegant syntax. Note that the scenarios are very rare, though.
     * Spying should be sporadic and overriding exception-stubbing is very rare. Not to mention that in general
     * overridding stubbing is a potential code smell that points out too much stubbing.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param toBeReturned to be returned when the stubbed method is called
     * @param toBeReturnedNext to be returned in consecutive calls when the stubbed method is called
     * @return stubber - to select a method for stubbing
     * @since 2.1.0
     */
    @SuppressWarnings({"unchecked", "varargs"})
    @CheckReturnValue
    public static Stubber doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return MOCKITO_CORE.stubber().doReturn(toBeReturned, toBeReturnedNext);
    }

    /**
     * Creates {@link org.mockito.InOrder} object that allows verifying mocks in order.
     *
     * <pre class="code"><code class="java">
     *   InOrder inOrder = inOrder(firstMock, secondMock);
     *
     *   inOrder.verify(firstMock).add("was called first");
     *   inOrder.verify(secondMock).add("was called second");
     * </code></pre>
     *
     * Verification in order is flexible - <b>you don't have to verify all interactions</b> one-by-one
     * but only those that you are interested in testing in order.
     * <p>
     * Also, you can create InOrder object passing only mocks that are relevant for in-order verification.
     * <p>
     * <code>InOrder</code> verification is 'greedy', but you will hardly ever notice it.
     * If you want to find out more, read
     * <a href="https://github.com/mockito/mockito/wiki/Greedy-algorithm-of-verification-InOrder">this wiki page</a>.
     * <p>
     * As of Mockito 1.8.4 you can verifyNoMoreInteractions() in order-sensitive way. Read more: {@link InOrder#verifyNoMoreInteractions()}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @param mocks to be verified in order
     *
     * @return InOrder object to be used to verify in order
     */
    @CheckReturnValue
    public static InOrder inOrder(Object... mocks) {
        return MOCKITO_CORE.inOrder(mocks);
    }

    /**
     * Ignores stubbed methods of given mocks for the sake of verification.
     * Please consider using {@link Strictness#STRICT_STUBS} feature which eliminates the need for <code>ignoreStubs()</code>
     * and provides other benefits.
     * <p>
     * <code>ignoreStubs()</code> is sometimes useful when coupled with <code>verifyNoMoreInteractions()</code> or verification <code>inOrder()</code>.
     * Helps avoiding redundant verification of stubbed calls - typically we're not interested in verifying stubs.
     * <p>
     * <b>Warning</b>, <code>ignoreStubs()</code> might lead to overuse of <code>verifyNoMoreInteractions(ignoreStubs(...));</code>
     * Bear in mind that Mockito does not recommend bombarding every test with <code>verifyNoMoreInteractions()</code>
     * for the reasons outlined in javadoc for {@link Mockito#verifyNoMoreInteractions(Object...)}
     * Other words: all <b>*stubbed*</b> methods of given mocks are marked <b>*verified*</b> so that they don't get in a way during verifyNoMoreInteractions().
     * <p>
     * This method <b>changes the input mocks</b>! This method returns input mocks just for convenience.
     * <p>
     * Ignored stubs will also be ignored for verification inOrder, including {@link org.mockito.InOrder#verifyNoMoreInteractions()}.
     * See the second example.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *  //mocking lists for the sake of the example (if you mock List in real you will burn in hell)
     *  List mock1 = mock(List.class), mock2 = mock(List.class);
     *
     *  //stubbing mocks:
     *  when(mock1.get(0)).thenReturn(10);
     *  when(mock2.get(0)).thenReturn(20);
     *
     *  //using mocks by calling stubbed get(0) methods:
     *  System.out.println(mock1.get(0)); //prints 10
     *  System.out.println(mock2.get(0)); //prints 20
     *
     *  //using mocks by calling clear() methods:
     *  mock1.clear();
     *  mock2.clear();
     *
     *  //verification:
     *  verify(mock1).clear();
     *  verify(mock2).clear();
     *
     *  //verifyNoMoreInteractions() fails because get() methods were not accounted for.
     *  try { verifyNoMoreInteractions(mock1, mock2); } catch (NoInteractionsWanted e);
     *
     *  //However, if we ignore stubbed methods then we can verifyNoMoreInteractions()
     *  verifyNoMoreInteractions(ignoreStubs(mock1, mock2));
     *
     *  //Remember that ignoreStubs() <b>*changes*</b> the input mocks and returns them for convenience.
     * </code></pre>
     * Ignoring stubs can be used with <b>verification in order</b>:
     * <pre class="code"><code class="java">
     *  List list = mock(List.class);
     *  when(list.get(0)).thenReturn("foo");
     *
     *  list.add(0);
     *  list.clear();
     *  System.out.println(list.get(0)); //we don't want to verify this
     *
     *  InOrder inOrder = inOrder(ignoreStubs(list));
     *  inOrder.verify(list).add(0);
     *  inOrder.verify(list).clear();
     *  inOrder.verifyNoMoreInteractions();
     * </code></pre>
     * Stubbed invocations are automatically verified with {@link Strictness#STRICT_STUBS} feature
     * and it eliminates the need for <code>ignoreStubs()</code>. Example below uses JUnit Rules:
     * <pre class="code"><code class="java">
     *  &#064;Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
     *
     *  List list = mock(List.class);
     *  when(list.get(0)).thenReturn("foo");
     *
     *  list.size();
     *  verify(list).size();
     *
     *  list.get(0); // Automatically verified by STRICT_STUBS
     *  verifyNoMoreInteractions(list); // No need of ignoreStubs()
     * </code></pre>
     *
     * @since 1.9.0
     * @param mocks input mocks that will be changed
     * @return the same mocks that were passed in as parameters
     */
    public static Object[] ignoreStubs(Object... mocks) {
        return MOCKITO_CORE.ignoreStubs(mocks);
    }

    /**
     * Allows verifying exact number of invocations. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, times(2)).someMethod("some arg");
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param wantedNumberOfInvocations wanted number of invocations
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode times(int wantedNumberOfInvocations) {
        return VerificationModeFactory.times(wantedNumberOfInvocations);
    }

    /**
     * Alias to <code>times(0)</code>, see {@link Mockito#times(int)}
     * <p>
     * Verifies that interaction did not happen. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, never()).someMethod();
     * </code></pre>
     *
     * <p>
     * If you want to verify there were NO interactions with the mock
     * check out {@link Mockito#verifyZeroInteractions(Object...)}
     * or {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode never() {
        return times(0);
    }

    /**
     * Allows at-least-once verification. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, atLeastOnce()).someMethod("some arg");
     * </code></pre>
     * Alias to <code>atLeast(1)</code>.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode atLeastOnce() {
        return VerificationModeFactory.atLeastOnce();
    }

    /**
     * Allows at-least-x verification. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, atLeast(3)).someMethod("some arg");
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param minNumberOfInvocations minimum number of invocations
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return VerificationModeFactory.atLeast(minNumberOfInvocations);
    }

    /**
     * Allows at-most-once verification. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, atMostOnce()).someMethod("some arg");
     * </code></pre>
     * Alias to <code>atMost(1)</code>.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode atMostOnce() {
        return VerificationModeFactory.atMostOnce();
    }

    /**
     * Allows at-most-x verification. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, atMost(3)).someMethod("some arg");
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param maxNumberOfInvocations max number of invocations
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return VerificationModeFactory.atMost(maxNumberOfInvocations);
    }

    /**
     * Allows non-greedy verification in order.  For example
     * <pre class="code"><code class="java">
     *   inOrder.verify( mock, calls( 2 )).someMethod( "some arg" );
     * </code></pre>
     * <ul>
     * <li>will not fail if the method is called 3 times, unlike times( 2 )</li>
     * <li>will not mark the third invocation as verified, unlike atLeast( 2 )</li>
     * </ul>
     * This verification mode can only be used with in order verification.
     * @param wantedNumberOfInvocations number of invocations to verify
     * @return  verification mode
     */
    @CheckReturnValue
    public static VerificationMode calls(int wantedNumberOfInvocations) {
        return VerificationModeFactory.calls(wantedNumberOfInvocations);
    }

    /**
     * Allows checking if given method was the only one invoked. E.g:
     * <pre class="code"><code class="java">
     *   verify(mock, only()).someMethod();
     *   //above is a shorthand for following 2 lines of code:
     *   verify(mock).someMethod();
     *   verifyNoMoreInteractions(mock);
     * </code></pre>
     *
     * <p>
     * See also {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return verification mode
     */
    @CheckReturnValue
    public static VerificationMode only() {
        return VerificationModeFactory.only();
    }

    /**
     * Verification will be triggered over and over until the given amount of millis, allowing testing of async code.
     * Useful when interactions with the mock object did not happened yet.
     * Extensive use of {@code timeout()} method can be a code smell - there are better ways of testing concurrent code.
     * <p>
     * See also {@link #after(long)} method for testing async code.
     * Differences between {@code timeout()} and {@code after} are explained in Javadoc for {@link #after(long)}.
     *
     * <pre class="code"><code class="java">
     *   //passes when someMethod() is called no later than within 100 ms
     *   //exits immediately when verification is satisfied (e.g. may not wait full 100 ms)
     *   verify(mock, timeout(100)).someMethod();
     *   //above is an alias to:
     *   verify(mock, timeout(100).times(1)).someMethod();
     *
     *   //passes as soon as someMethod() has been called 2 times under 100 ms
     *   verify(mock, timeout(100).times(2)).someMethod();
     *
     *   //equivalent: this also passes as soon as someMethod() has been called 2 times under 100 ms
     *   verify(mock, timeout(100).atLeast(2)).someMethod();
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param millis - duration in milliseconds
     *
     * @return object that allows fluent specification of the verification (times(x), atLeast(y), etc.)
     */
    @CheckReturnValue
    public static VerificationWithTimeout timeout(long millis) {
        return new Timeout(millis, VerificationModeFactory.times(1));
    }

    /**
     * Verification will be triggered after given amount of millis, allowing testing of async code.
     * Useful when interactions with the mock object did not happened yet.
     * Extensive use of {@code after()} method can be a code smell - there are better ways of testing concurrent code.
     * <p>
     * Not yet implemented to work with InOrder verification.
     * <p>
     * See also {@link #timeout(long)} method for testing async code.
     * Differences between {@code timeout()} and {@code after()} are explained below.
     *
     * <pre class="code"><code class="java">
     *   //passes after 100ms, if someMethod() has only been called once at that time.
     *   verify(mock, after(100)).someMethod();
     *   //above is an alias to:
     *   verify(mock, after(100).times(1)).someMethod();
     *
     *   //passes if someMethod() is called <b>*exactly*</b> 2 times, as tested after 100 millis
     *   verify(mock, after(100).times(2)).someMethod();
     *
     *   //passes if someMethod() has not been called, as tested after 100 millis
     *   verify(mock, after(100).never()).someMethod();
     *
     *   //verifies someMethod() after a given time span using given verification mode
     *   //useful only if you have your own custom verification modes.
     *   verify(mock, new After(100, yourOwnVerificationMode)).someMethod();
     * </code></pre>
     *
     * <strong>timeout() vs. after()</strong>
     * <ul>
     *     <li>timeout() exits immediately with success when verification passes</li>
     *     <li>after() awaits full duration to check if verification passes</li>
     * </ul>
     * Examples:
     * <pre class="code"><code class="java">
     *   //1.
     *   mock.foo();
     *   verify(mock, after(1000)).foo();
     *   //waits 1000 millis and succeeds
     *
     *   //2.
     *   mock.foo();
     *   verify(mock, timeout(1000)).foo();
     *   //succeeds immediately
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito} class
     *
     * @param millis - duration in milliseconds
     *
     * @return object that allows fluent specification of the verification
     */
    @CheckReturnValue
    public static VerificationAfterDelay after(long millis) {
        return new After(millis, VerificationModeFactory.times(1));
    }

    /**
     * First of all, in case of any trouble, I encourage you to read the Mockito FAQ: <a href="https://github.com/mockito/mockito/wiki/FAQ">https://github.com/mockito/mockito/wiki/FAQ</a>
     * <p>
     * In case of questions you may also post to mockito mailing list: <a href="http://groups.google.com/group/mockito">http://groups.google.com/group/mockito</a>
     * <p>
     * <code>validateMockitoUsage()</code> <b>explicitly validates</b> the framework state to detect invalid use of Mockito.
     * However, this feature is optional <b>because Mockito validates the usage all the time...</b> but there is a gotcha so read on.
     * <p>
     * Examples of incorrect use:
     * <pre class="code"><code class="java">
     * //Oops, thenReturn() part is missing:
     * when(mock.get());
     *
     * //Oops, verified method call is inside verify() where it should be on the outside:
     * verify(mock.execute());
     *
     * //Oops, missing method to verify:
     * verify(mock);
     * </code></pre>
     *
     * Mockito throws exceptions if you misuse it so that you know if your tests are written correctly.
     * The gotcha is that Mockito does the validation <b>next time</b> you use the framework (e.g. next time you verify, stub, call mock etc.).
     * But even though the exception might be thrown in the next test,
     * the exception <b>message contains a navigable stack trace element</b> with location of the defect.
     * Hence you can click and find the place where Mockito was misused.
     * <p>
     * Sometimes though, you might want to validate the framework usage explicitly.
     * For example, one of the users wanted to put <code>validateMockitoUsage()</code> in his <code>&#064;After</code> method
     * so that he knows immediately when he misused Mockito.
     * Without it, he would have known about it not sooner than <b>next time</b> he used the framework.
     * One more benefit of having <code>validateMockitoUsage()</code> in <code>&#064;After</code> is that jUnit runner and rule will always fail in the test method with defect
     * whereas ordinary 'next-time' validation might fail the <b>next</b> test method.
     * But even though JUnit might report next test as red, don't worry about it
     * and just click at navigable stack trace element in the exception message to instantly locate the place where you misused mockito.
     * <p>
     * <b>Both built-in runner: {@link MockitoJUnitRunner} and rule: {@link MockitoRule}</b> do validateMockitoUsage() after each test method.
     * <p>
     * Bear in mind that <b>usually you don't have to <code>validateMockitoUsage()</code></b>
     * and framework validation triggered on next-time basis should be just enough,
     * mainly because of enhanced exception message with clickable location of defect.
     * However, I would recommend validateMockitoUsage() if you already have sufficient test infrastructure
     * (like your own runner or base class for all tests) because adding a special action to <code>&#064;After</code> has zero cost.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     */
    public static void validateMockitoUsage() {
        MOCKITO_CORE.validateMockitoUsage();
    }

    /**
     * Allows mock creation with additional mock settings.
     * <p>
     * Don't use it too often.
     * Consider writing simple tests that use simple mocks.
     * Repeat after me: simple tests push simple, KISSy, readable & maintainable code.
     * If you cannot write a test in a simple way - refactor the code under test.
     * <p>
     * Examples of mock settings:
     * <pre class="code"><code class="java">
     *   //Creates mock with different default answer & name
     *   Foo mock = mock(Foo.class, withSettings()
     *       .defaultAnswer(RETURNS_SMART_NULLS)
     *       .name("cool mockie"));
     *
     *   //Creates mock with different default answer, descriptive name and extra interfaces
     *   Foo mock = mock(Foo.class, withSettings()
     *       .defaultAnswer(RETURNS_SMART_NULLS)
     *       .name("cool mockie")
     *       .extraInterfaces(Bar.class));
     * </code></pre>
     * {@link MockSettings} has been introduced for two reasons.
     * Firstly, to make it easy to add another mock settings when the demand comes.
     * Secondly, to enable combining different mock settings without introducing zillions of overloaded mock() methods.
     * <p>
     * See javadoc for {@link MockSettings} to learn about possible mock settings.
     * <p>
     *
     * @return mock settings instance with defaults.
     */
    @CheckReturnValue
    public static MockSettings withSettings() {
        return new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
    }

    /**
     * Adds a description to be printed if verification fails.
     * <pre class="code"><code class="java">
     * verify(mock, description("This will print on failure")).someMethod("some arg");
     * </code></pre>
     * @param description The description to print on failure.
     * @return verification mode
     * @since 2.1.0
     */
    @CheckReturnValue
    public static VerificationMode description(String description) {
        return times(1).description(description);
    }

    /**
     * @deprecated - please use {@link MockingDetails#printInvocations()} instead.
     * An instance of {@code MockingDetails} can be retrieved via {@link #mockingDetails(Object)}.
     */
    @Deprecated
    @CheckReturnValue
    static MockitoDebugger debug() {
        return new MockitoDebuggerImpl();
    }

    /**
     * For advanced users or framework integrators. See {@link MockitoFramework} class.
     *
     * @since 2.1.0
     */
    @Incubating
    @CheckReturnValue
    public static MockitoFramework framework() {
        return new DefaultMockitoFramework();
    }

    /**
     * {@code MockitoSession} is an optional, highly recommended feature
     * that helps driving cleaner tests by eliminating boilerplate code and adding extra validation.
     * <p>
     * For more information, including use cases and sample code, see the javadoc for {@link MockitoSession}.
     *
     * @since 2.7.0
     */
    @Incubating
    @CheckReturnValue
    public static MockitoSessionBuilder mockitoSession() {
        return new DefaultMockitoSessionBuilder();
    }

    /**
     * Lenient stubs bypass "strict stubbing" validation (see {@link Strictness#STRICT_STUBS}).
     * When stubbing is declared as lenient, it will not be checked for potential stubbing problems such as
     * 'unnecessary stubbing' ({@link UnnecessaryStubbingException}) or for 'stubbing argument mismatch' {@link PotentialStubbingProblem}.
     *
     * <pre class="code"><code class="java">
     *   lenient().when(mock.foo()).thenReturn("ok");
     * </code></pre>
     *
     * Most mocks in most tests don't need leniency and should happily prosper with {@link Strictness#STRICT_STUBS}.
     * <ul>
     *     <li>If a specific stubbing needs to be lenient - use this method</li>
     *     <li>If a specific mock need to have stubbings lenient - use {@link MockSettings#lenient()}</li>
     *     <li>If a specific test method / test class needs to have all stubbings lenient
     *          - configure strictness using our JUnit support ({@link MockitoJUnit} or Mockito Session ({@link MockitoSession})</li>
     *
     * <h3>Elaborate example</h3>
     *
     * In below example, 'foo.foo()' is a stubbing that was moved to 'before()' method to avoid duplication.
     * Doing so makes one of the test methods ('test3()') fail with 'unnecessary stubbing'.
     * To resolve it we can configure 'foo.foo()' stubbing in 'before()' method to be lenient.
     * Alternatively, we can configure entire 'foo' mock as lenient.
     * <p>
     * This example is simplified and not realistic.
     * Pushing stubbings to 'before()' method may cause tests to be less readable.
     * Some repetition in tests is OK, use your own judgement to write great tests!
     * It is not desired to eliminate all possible duplication from the test code
     * because it may add complexity and conceal important test information.
     *
     * <pre class="code"><code class="java">
     * public class SomeTest {
     *
     *     &#064;Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(STRICT_STUBS);
     *
     *     &#064;Mock Foo foo;
     *     &#064;Mock Bar bar;
     *
     *     &#064;Before public void before() {
     *         when(foo.foo()).thenReturn("ok");
     *
     *         // it is better to configure the stubbing to be lenient:
     *         // lenient().when(foo.foo()).thenReturn("ok");
     *
     *         // or the entire mock to be lenient:
     *         // foo = mock(Foo.class, withSettings().lenient());
     *     }
     *
     *     &#064;Test public void test1() {
     *         foo.foo();
     *     }
     *
     *     &#064;Test public void test2() {
     *         foo.foo();
     *     }
     *
     *     &#064;Test public void test3() {
     *         bar.bar();
     *     }
     * }
     * </code></pre>
     *
     * @since 2.20.0
     */
    @Incubating
    public static LenientStubber lenient() {
        return MOCKITO_CORE.lenient();
    }
}
