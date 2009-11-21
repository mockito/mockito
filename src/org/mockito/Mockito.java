/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.debugging.MockitoDebuggerImpl;
import org.mockito.internal.stubbing.answers.*;
import org.mockito.internal.stubbing.defaultanswers.*;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.*;

/**
 * <p align="left"><img src="logo.jpg"/></p>
 * Mockito library enables mocks creation, verification and stubbing.
 * <p>
 * This javadoc content is also available on the <a href="http://mockito.org">http://mockito.org</a> web page. 
 * All documentation is kept in javadocs because it guarantees consistency between what's on the web and what's in the source code.
 * Also, it makes possible to access documentation straight from the IDE even if you work offline.   
 * 
 * <h1>Contents</h1>
 * 
 * <b> 
 *      <a href="#1">1. Let's verify some behaviour! </a><br/> 
 *      <a href="#2">2. How about some stubbing? </a><br/>
 *      <a href="#3">3. Argument matchers </a><br/>
 *      <a href="#4">4. Verifying exact number of invocations / at least once / never </a><br/> 
 *      <a href="#5">5. Stubbing void methods with exceptions </a><br/> 
 *      <a href="#6">6. Verification in order </a><br/> 
 *      <a href="#7">7. Making sure interaction(s) never happened on mock </a><br/> 
 *      <a href="#8">8. Finding redundant invocations </a><br/> 
 *      <a href="#9">9. Shorthand for mocks creation - &#064;Mock annotation </a><br/> 
 *      <a href="#10">10. Stubbing consecutive calls (iterator-style stubbing) </a><br/> 
 *      <a href="#11">11. Stubbing with callbacks </a><br/>
 *      <a href="#12">12. doThrow()|doAnswer()|doNothing()|doReturn() family of methods mostly for stubbing voids </a><br/>
 *      <a href="#13">13. Spying on real objects </a><br/>
 *      <a href="#14">14. Changing default return values of unstubbed invocations (Since 1.7) </a><br/>
 *      <a href="#15">15. Capturing arguments for further assertions (Since 1.8.0) </a><br/>
 *      <a href="#16">16. Real partial mocks (Since 1.8.0) </a><br/>
 *      <a href="#17">17. Resetting mocks (Since 1.8.0) </a><br/>
 *      <a href="#18">18. Troubleshooting & validating framework usage (Since 1.8.0) </a><br/>
 *      <a href="#19">19. Aliases for behavior driven development (Since 1.8.0) </a><br/>
 *      <a href="#20">20. (**New**) Serializable mocks (Since 1.8.1) </a><br/>
 * </b>
 * 
 * <p>
 * Following examples mock a List, because everyone knows its interface (methods
 * like add(), get(), clear() will be used). <br>
 * You probably wouldn't mock List class 'in real'.
 * 
 * <h3 id="1">1. Let's verify some behaviour!</h3>
 * 
 * <pre>
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
 * </pre>
 * 
 * <p>
 * Once created, mock will remember all interactions. Then you can selectively
 * verify whatever interaction you are interested in.
 * 
 * <h3 id="2">2. How about some stubbing?</h3>
 * 
 * <pre>
 * //You can mock concrete classes, not only interfaces
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
 * //If your code cares what get(0) returns then something else breaks (often before even verify() gets executed).
 * //If your code doesn't care what get(0) returns then it should not be stubbed. Not convinced? See <a href="http://monkeyisland.pl/2008/04/26/asking-and-telling">here</a>.
 * verify(mockedList).get(0);
 * </pre>
 * 
 * <ul>
 * <li> By default, for all methods that return value, mock returns null, an
 * empty collection or appropriate primitive/primitive wrapper value (e.g: 0,
 * false, ... for int/Integer, boolean/Boolean, ...). </li>
 * 
 * <li> Stubbing can be overridden: for example common stubbing can go to
 * fixture setup but the test methods can override it.
 * Please note that overridding stubbing is a potential code smell that points out too much stubbing</li>
 * 
 * <li> Once stubbed, the method will always return stubbed value regardless
 * of how many times it is called. </li>
 * 
 * <li> Last stubbing is more important - when you stubbed the same method with
 * the same arguments many times. </li>
 * 
 * </ul>
 * 
 * <h3 id="3">3. Argument matchers</h3>
 * 
 * Mockito verifies argument values in natural java style: by using an equals() method.
 * Sometimes, when extra flexibility is required then you might use argument matchers:  
 * 
 * <pre>
 * //stubbing using built-in anyInt() argument matcher
 * when(mockedList.get(anyInt())).thenReturn("element");
 * 
 * //stubbing using hamcrest (let's say isValid() returns your own hamcrest matcher):
 * when(mockedList.contains(argThat(isValid()))).thenReturn("element");
 * 
 * //following prints "element"
 * System.out.println(mockedList.get(999));
 * 
 * //<b>you can also verify using an argument matcher</b>
 * verify(mockedList).get(anyInt());
 * </pre>
 * 
 * <p>
 * Argument matchers allow flexible verification or stubbing. 
 * {@link Matchers Click here to see} more built-in matchers 
 * and examples of <b>custom argument matchers / hamcrest matchers</b>.
 * <p>
 * For information solely on <b>custom argument matchers</b> check out javadoc for {@link ArgumentMatcher} class.
 * <p>
 * Be reasonable with using complicated argument matching.
 * The natural matching style using equals() with occasional anyX() matchers tend to give clean & simple tests.
 * Sometimes it's just better to refactor the code to allow equals() matching or even implement equals() method to help out with testing.
 * <p>
 * Also, read <a href="#15">section 15</a> or javadoc for {@link ArgumentCaptor} class.
 * {@link ArgumentCaptor} is a special implementation of an argument matcher that captures argument values for further assertions.  
 * <p>
 * <b>Warning on argument matchers:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided
 * by matchers.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * 
 * <pre>
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *   
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without an argument matcher.
 * </pre>
 * 
 * <h3 id="4">4. Verifying exact number of invocations / at least x / never</h3>
 * 
 * <pre>
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
 * verify(mockedList, atLeastOnce()).add("three times");
 * verify(mockedList, atLeast(2)).add("five times");
 * verify(mockedList, atMost(5)).add("three times");
 * 
 * </pre>
 * 
 * <p>
 * <b>times(1) is the default.</b> Therefore using times(1) explicitly can be
 * omitted.
 * 
 * <h3 id="5">5. Stubbing void methods with exceptions</h3>
 * 
 * <pre>
 *   doThrow(new RuntimeException()).when(mockedList).clear();
 *   
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </pre>
 * 
 * Read more about doThrow|doAnswer family of methods in paragraph 12.
 * <p>
 * Initially, {@link Mockito#stubVoid(Object)} was used for stubbing voids.
 * Currently stubVoid() is deprecated in favor of {@link Mockito#doThrow(Throwable)}.
 * This is because of improved readability and consistency with the family of {@link Mockito#doAnswer(Answer)} methods. 
 * 
 * <h3 id="6">6. Verification in order</h3>
 * 
 * <pre>
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
 * </pre>
 * 
 * Verification in order is flexible - <b>you don't have to verify all
 * interactions</b> one-by-one but only those that you are interested in
 * testing in order.
 * <p>
 * Also, you can create InOrder object passing only mocks that are relevant for
 * in-order verification.
 * 
 * <h3 id="7">7. Making sure interaction(s) never happened on mock</h3>
 * 
 * <pre>
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
 * </pre>
 * 
 * <h3 id="8">8. Finding redundant invocations</h3>
 * 
 * <pre>
 * //using mocks
 * mockedList.add("one");
 * mockedList.add("two");
 * 
 * verify(mockedList).add("one");
 * 
 * //following verification will fail 
 * verifyNoMoreInteractions(mockedList);
 * </pre>
 * 
 * A word of <b>warning</b>: 
 * Some users who did a lot of classic, expect-run-verify mocking tend to use verifyNoMoreInteractions() very often, even in every test method. 
 * verifyNoMoreInteractions() is not recommended to use in every test method. 
 * verifyNoMoreInteractions() is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
 * Abusing it leads to overspecified, less maintainable tests. You can find further reading 
 * <a href="http://monkeyisland.pl/2008/07/12/should-i-worry-about-the-unexpected/">here</a>.
 * 
 * <p>   
 * See also {@link Mockito#never()} - it is more explicit and
 * communicates the intent well.
 * <p>
 * 
 * <h3 id="9">9. Shorthand for mocks creation - &#064;Mock annotation</h3>
 * 
 * <ul>
 * <li>Minimizes repetitive mock creation code.</li>
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because the <b>field name</b>
 * is used to identify the mock.</li>
 * </ul>
 * 
 * <pre>
 *   public class ArticleManagerTest { 
 *     
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Mock private UserProvider userProvider;
 *     
 *       private ArticleManager manager;
 * </pre>
 * 
 * <b>Important!</b> This needs to be somewhere in the base class or a test
 * runner:
 * 
 * <pre>
 * MockitoAnnotations.initMocks(testClass);
 * </pre>
 * 
 * You can use built-in runner: {@link MockitoJUnitRunner}.
 * <p>
 * Read more here: {@link MockitoAnnotations}
 * 
 * <h3 id="10"> 10. Stubbing consecutive calls (iterator-style stubbing)</h3>
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
 * <pre>
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
 * </pre>
 * 
 * Alternative, shorter version of consecutive stubbing:
 * 
 * <pre>
 * when(mock.someMethod("some arg"))
 *   .thenReturn("one", "two", "three");
 * </pre>
 * 
 * <h3 id="11"> 11. Stubbing with callbacks</h3>
 * 
 * Allows stubbing with generic {@link Answer} interface.
*  <p>
 * Yet another controversial feature which was not included in Mockito
 * originally. We recommend using simple stubbing with thenReturn() or
 * thenThrow() only. Those two should be <b>just enough</b> to test/test-drive
 * any clean & simple code.
 * 
 * <pre>
 * when(mock.someMethod(anyString())).thenAnswer(new Answer() {
 *     Object answer(InvocationOnMock invocation) {
 *         Object[] args = invocation.getArguments();
 *         Object mock = invocation.getMock();
 *         return "called with arguments: " + args;
 *     }
 * });
 * 
 * //Following prints "called with arguments: foo"
 * System.out.println(mock.someMethod("foo"));
 * </pre>
 * 
 * <h3 id="12"> 12. doThrow()|doAnswer()|doNothing()|doReturn() family of methods for stubbing voids (mostly)</h3>
 * 
 * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler does not like void methods inside brackets...
 * <p>
 * {@link Mockito#doThrow(Throwable)} replaces the {@link Mockito#stubVoid(Object)} method for stubbing voids. 
 * The main reason is improved readability and consistency with the family of doAnswer() methods.
 * <p>
 * Use doThrow() when you want to stub a void method with an exception:
 * <pre>
 *   doThrow(new RuntimeException()).when(mockedList).clear();
 *   
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </pre>
 * 
 * Read more about other methods:
 * <p>
 * {@link Mockito#doThrow(Throwable)}
 * <p>
 * {@link Mockito#doAnswer(Answer)}
 * <p>
 * {@link Mockito#doNothing()}
 * <p>
 * {@link Mockito#doReturn(Object)}
 * 
 * <h3 id="13"> 13. Spying on real objects</h3>
 * 
 * You can create spies of real objects. When you use the spy then the <b>real</b> methods are called (unless a method was stubbed).
 * <p>
 * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
 * 
 * <p>
 * Spying on real objects can be associated with "partial mocking" concept. 
 * <b>Before the release 1.8</b>, Mockito spies were not real partial mocks. 
 * The reason was we thought partial mock is a code smell. 
 * At some point we found legitimate use cases for partial mocks 
 * (3rd party interfaces, interim refactoring of legacy code, the full article is <a href=
 * "http://monkeyisland.pl/2009/01/13/subclass-and-override-vs-partial-mocking-vs-refactoring"
 * >here</a>)
 * <p>
 *
 * <pre>
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
 * </pre>
 * 
 * <h4>Important gotcha on spying real objects!</h4>
 * 
 * 1. Sometimes it's impossible to use {@link Mockito#when(Object)} for stubbing spies. Example:
 * 
 * <pre>
 *   List list = new LinkedList();
 *   List spy = spy(list);
 *   
 *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
 *   when(spy.get(0)).thenReturn("foo");
 *   
 *   //You have to use doReturn() for stubbing
 *   doReturn("foo").when(spy).get(0);
 * </pre>
 * 
 * 2. Watch out for final methods. 
 * Mockito doesn't mock final methods so the bottom line is: when you spy on real objects + you try to stub a final method = trouble.
 * What will happen is the real method will be called *on mock* but *not on the real instance* you passed to the spy() method.
 * Typically you may get a NullPointerException because mock instances don't have fields initiated.
 * 
 * <h3 id="14">14. Changing default return values of unstubbed invocations (Since 1.7) </h3>
 * 
 * You can create a mock with specified strategy for its return values.
 * It's quite advanced feature and typically you don't need it to write decent tests.
 * However, it can be helpful for working with <b>legacy systems</b>.
 * <p>
 * It is the default answer so it will be used <b>only when you don't</b> stub the method call.
 * 
 * <pre>
 *   Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
 *   Foo mockTwo = mock(Foo.class, new YourOwnAnswer()); 
 * </pre>
 * 
 * <p>
 * Read more about this interesting implementation of <i>Answer</i>: {@link Mockito#RETURNS_SMART_NULLS}
 * 
 * <h3 id="15">15. Capturing arguments for further assertions (Since 1.8.0) </h3>
 * 
 * Mockito verifies argument values in natural java style: by using an equals() method.
 * This is also the recommended way of matching arguments because it makes tests clean & simple.
 * In some situations though, it is helpful to assert on certain arguments after the actual verification.
 * For example:
 * <pre>
 *   ArgumentCaptor&lt;Person&gt; argument = ArgumentCaptor.forClass(Person.class);
 *   verify(mock).doSomething(argument.capture());
 *   assertEquals("John", argument.getValue().getName());
 * </pre>
 * 
 * <b>Warning:</b> it is recommended to use ArgumentCaptor with verification <b>but not</b> with stubbing.
 * Using ArgumentCaptor with stubbing may decrease test readability because captor is created outside of assert (aka verify or 'then') block.
 * Also it may reduce defect localization because if stubbed method was not called then no argument is captured.
 * <p>
 * In a way ArgumentCaptor is related to custom argument matchers (see javadoc for {@link ArgumentMatcher} class).
 * Both techniques can be used for making sure certain arguments where passed to mocks. 
 * However, ArgumentCaptor may be a better fit if:
 * <ul>  
 * <li>custom argument matcher is not likely to be reused</li>
 * <li>you just need it to assert on argument values to complete verification</li>
 * </ul>
 * Custom argument matchers via {@link ArgumentMatcher} are usually better for stubbing.
 * 
 * <h3 id="16">16. Real partial mocks (Since 1.8.0) </h3>
 *  
 *  Finally, after many internal debates & discussions on the mailing list, partial mock support was added to Mockito.
 *  Previously we considered partial mocks as code smells. However, we found a legitimate use case for partial mocks - more reading:
 *  <a href="http://monkeyisland.pl/2009/01/13/subclass-and-override-vs-partial-mocking-vs-refactoring">here</a>
 *  <p>
 *  <b>Before release 1.8</b> spy() was not producing real partial mocks and it was confusing for some users.
 *  Read more about spying: <a href="#13">here</a> or in javadoc for {@link Mockito#spy(Object)} method. 
 *  <p>
 *  <pre>
 *    //you can create partial mock with spy() method:    
 *    List list = spy(new LinkedList());
 *    
 *    //you can enable partial mock capabilities selectively on mocks:
 *    Foo mock = mock(Foo.class);
 *    //Be sure the real implementation is 'safe'.
 *    //If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
 *    when(mock.someMethod()).thenCallRealMethod();
 *  </pre>
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
 * <h3 id="17">17. Resetting mocks (Since 1.8.0) </h3>
 *  
 * Smart Mockito users hardly use this feature because they know it could be a sign of poor tests.
 * Normally, you don't need to reset your mocks, just create new mocks for each test method. 
 * <p>
 * Instead of reset() please consider writing simple, small and focused test methods over lengthy, over-specified tests.
 * <b>First potential code smell is reset() in the middle of the test method.</b> This probably means you're testing too much.
 * Follow the whisper of your test methods: "Please keep us small & focused on single behavior". 
 * There are several threads about it on mockito mailing list.
 * <p>
 * The only reason we added reset() method is to
 * make it possible to work with container-injected mocks.
 * See issue 55 (<a href="http://code.google.com/p/mockito/issues/detail?id=55">here</a>)
 * or FAQ (<a href="http://code.google.com/p/mockito/wiki/FAQ">here</a>).
 * <p>
 * <b>Don't harm yourself.</b> reset() in the middle of the test method is a code smell (you're probably testing too much). 
 * <pre>
 *   List mock = mock(List.class);
 *   when(mock.size()).thenReturn(10);
 *   mock.add(1);
 *   
 *   reset(mock);
 *   //at this point the mock forgot any interactions & stubbing
 * </pre>
 *  
 * <h3 id="18">18. Troubleshooting & validating framework usage (Since 1.8.0) </h3>
 * 
 * First of all, in case of any trouble, I encourage you to read the Mockito FAQ: 
 * <a href="http://code.google.com/p/mockito/wiki/FAQ">http://code.google.com/p/mockito/wiki/FAQ</a>
 * <p>
 * In case of questions you may also post to mockito mailing list: 
 * <a href="http://groups.google.com/group/mockito">http://groups.google.com/group/mockito</a>
 * <p>
 * Next, you should know that Mockito validates if you use it correctly <b>all the time</b>. 
 * However, there's a gotcha so please read the javadoc for {@link Mockito#validateMockitoUsage()}
 * 
 * <h3 id="19">19. Aliases for behavior driven development (Since 1.8.0) </h3>
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
 * <pre>
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
 * </pre>
 * 
 * <h3 id="20">20. (**New**) Serializable mocks (Since 1.8.1) </h3>
 * 
 * Mocks can be made serializable. With this feature you can use a mock in a place that requires dependencies to be serializable.
 * <p>
 * WARNING: This should be rarely used in unit testing. 
 * <p>
 * The behaviour was implemented for a specific use case of a BDD spec that had an unreliable external dependency.  This
 * was in a web environment and the objects from the external dependency were being serialized to pass between layers. 
 * <p>
 * To create serializable mock use {@link MockSettings#serializable()}:
 * <pre>
 *   List serializableMock = mock(List.class, withSettings().serializable());
 * </pre>
 * <p>
 * The mock can be serialized assuming all the normal <a href='http://java.sun.com/j2se/1.5.0/docs/api/java/io/Serializable.html'>
 * serialization requirements</a> are met by the class.
 * <p>
 * Making a real object spy serializable is a bit more effort as the spy(...) method does not have an overloaded version 
 * which accepts MockSettings. No worries, you will hardly ever use it.
 * 
 * <pre>
 * List<Object> list = new ArrayList<Object>();
 * List<Object> spy = mock(ArrayList.class, withSettings()
 *                 .spiedInstance(list)
 *                 .defaultAnswer(CALLS_REAL_METHODS)
 *                 .serializable());
 * </pre>
 */
@SuppressWarnings("unchecked")
public class Mockito extends Matchers {
    
    private static final MockitoCore MOCKITO_CORE = new MockitoCore();
    
    /**
     * The default Answer of every mock <b>if</b> the mock was not stubbed. 
     * Typically it just returns some empty value. 
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations. 
     * <p>
     * This implementation first tries the global configuration. 
     * If there is no global configuration then it uses {@link ReturnsEmptyValues} (returns zeros, empty collections, nulls, etc.)
     */
    public static final Answer<Object> RETURNS_DEFAULTS = new GloballyConfiguredAnswer();
    
    /**
     * Optional Answer to be used with {@link Mockito#mock(Class, Answer)}
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation can be helpful when working with legacy code.
     * Unstubbed methods often return null. If your code uses the object returned by an unstubbed call you get a NullPointerException.
     * This implementation of Answer <b>returns SmartNull instead of null</b>.
     * SmartNull gives nicer exception message than NPE because it points out the line where unstubbed method was called. You just click on the stack trace.
     * <p>
     * ReturnsSmartNulls first tries to return ordinary return values (see {@link ReturnsMoreEmptyValues})
     * then it tries to return SmartNull. If the return type is final then plain null is returned.
     * <p>
     * ReturnsSmartNulls will be probably the default return values strategy in Mockito 2.0
     * <p>
     * Example:
     * <pre>
     *   Foo mock = (Foo.class, RETURNS_SMART_NULLS);
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
     * </pre>
     */
    public static final Answer<Object> RETURNS_SMART_NULLS = new ReturnsSmartNulls();
    
    /**
     * Optional Answer to be used with {@link Mockito#mock(Class, Answer)}
     * <p>
     * {@link Answer} can be used to define the return values of unstubbed invocations.
     * <p>
     * This implementation can be helpful when working with legacy code. 
     * <p>
     * ReturnsMocks first tries to return ordinary return values (see {@link ReturnsMoreEmptyValues})
     * then it tries to return mocks. If the return type cannot be mocked (e.g. is final) then plain null is returned.
     * <p>
     */
    public static final Answer<Object> RETURNS_MOCKS = new ReturnsMocks();

    /**
     * Optional Answer to be used with {@link Mockito#mock(Class, Answer)}
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
     * <pre>
     * Foo mock = mock(Foo.class, CALLS_REAL_METHODS);
     *
     * // this calls the real implementation of Foo.getSomething()
     * value = mock.getSomething();
     *
     * when(mock.getSomething()).thenReturn(fakeValue);
     *
     * // now fakeValue is returned
     * value = mock.getSomething();
     * </pre>
     */
    public static final Answer<Object> CALLS_REAL_METHODS = new CallsRealMethods();
    
    /**
     * Creates mock object of given class or interface.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param classToMock class or interface to mock
     * @return mock object
     */
    public static <T> T mock(Class<T> classToMock) {
        return mock(classToMock, withSettings().defaultAnswer(RETURNS_DEFAULTS));
    }
    
    /**
     * Specifies mock name. Naming mocks can be helpful for debugging - the name is used in all verification errors. 
     * <p>
     * Beware that naming mocks is not a solution for complex code which uses too many mocks or collaborators. 
     * <b>If you have too many mocks then refactor the code</b> so that it's easy to test/debug without necessity of naming mocks.
     * <p>
     * <b>If you use &#064;Mock annotation then you've got naming mocks for free!</b> &#064;Mock uses field name as mock name. {@link Mock Read more.}
     * <p>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param classToMock class or interface to mock
     * @param name of the mock 
     * @return mock object
     */
    public static <T> T mock(Class<T> classToMock, String name) {
        return mock(classToMock, withSettings()
                .name(name)
                .defaultAnswer(RETURNS_DEFAULTS));
    }
    
    /**
     * @deprecated
     * <b>Please use mock(Foo.class, defaultAnswer);</b>
     * <p>
     * See {@link Mockito#mock(Class, Answer)}
     * <p>
     * Why it is deprecated? ReturnValues is being replaced by Answer
     * for better consistency & interoperability of the framework. 
     * Answer interface has been in Mockito for a while and it has the same responsibility as ReturnValues.
     * There's no point in mainting exactly the same interfaces.
     * <p>
     * Creates mock with a specified strategy for its return values. 
     * It's quite advanced feature and typically you don't need it to write decent tests.
     * However it can be helpful when working with legacy systems.
     * <p>
     * Obviously return values are used only when you don't stub the method call.
     *
     * <pre>
     *   Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
     *   Foo mockTwo = mock(Foo.class, new YourOwnReturnValues()); 
     * </pre>
     * 
     * <p>See examples in javadoc for {@link Mockito} class</p>
     * 
     * @param classToMock class or interface to mock
     * @param returnValues default return values for unstubbed methods
     *
     * @return mock object
     */
    @Deprecated
    public static <T> T mock(Class<T> classToMock, ReturnValues returnValues) {
        return mock(classToMock, withSettings().defaultAnswer(new AnswerReturnValuesAdapter(returnValues)));
    }
    
    /**
     * Creates mock with a specified strategy for its answers to interactions. 
     * It's quite advanced feature and typically you don't need it to write decent tests.
     * However it can be helpful when working with legacy systems.
     * <p>
     * It is the default answer so it will be used <b>only when you don't</b> stub the method call.
     *
     * <pre>
     *   Foo mock = mock(Foo.class, RETURNS_SMART_NULLS);
     *   Foo mockTwo = mock(Foo.class, new YourOwnAnswer()); 
     * </pre>
     * 
     * <p>See examples in javadoc for {@link Mockito} class</p>
     * 
     * @param classToMock class or interface to mock
     * @param defaultAnswer default answer for unstubbed methods
     *
     * @return mock object
     */
    public static <T> T mock(Class<T> classToMock, Answer defaultAnswer) {
        return mock(classToMock, withSettings().defaultAnswer(defaultAnswer));
    }
    
    /**
     * Creates a mock with some non-standard settings.
     * <p>
     * The number of configuration points for a mock grows 
     * so we need a fluent way to introduce new configuration without adding more and more overloaded Mockito.mock() methods. 
     * Hence {@link MockSettings}.
     * <pre>
     *   Listener mock = mock(Listener.class, withSettings()
     *     .name("firstListner").defaultBehavior(RETURNS_SMART_NULLS));
     *   );  
     * </pre>
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
    public static <T> T mock(Class<T> classToMock, MockSettings mockSettings) {
        return MOCKITO_CORE.mock(classToMock, mockSettings);
    }
    
    /**
     * Creates a spy of the real object. The spy calls <b>real</b> methods unless they are stubbed.
     * <p>
     * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
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
     * 
     * <pre>
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
     * </pre>
     * 
     * <h4>Important gotcha on spying real objects!</h4>
     * 
     * 1. Sometimes it's impossible to use {@link Mockito#when(Object)} for stubbing spies. Example:
     * 
     * <pre>
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *   
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(spy.get(0)).thenReturn("foo");
     *   
     *   //You have to use doReturn() for stubbing
     *   doReturn("foo").when(spy).get(0);
     * </pre>
     * 
     * 2. Watch out for final methods. 
     * Mockito doesn't mock final methods so the bottom line is: when you spy on real objects + you try to stub a final method = trouble.
     * What will happen is the real method will be called *on mock* but *not on the real instance* you passed to the spy() method.
     * Typically you may get a NullPointerException because mock instances don't have fields initiated.
     * 
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param object
     *            to spy on
     * @return a spy of the real object
     */
    public static <T> T spy(T object) {
        return MOCKITO_CORE.mock((Class<T>) object.getClass(), withSettings()
                .spiedInstance(object)
                .defaultAnswer(CALLS_REAL_METHODS)); 
    }

    /**
     * <pre>
     *   //Instead of:
     *   stub(mock.count()).toReturn(10);
     * 
     *   //Please do:
     *   when(mock.count()).thenReturn(10);
     * </pre> 
     * 
     * Many users found stub() confusing therefore stub() has been deprecated in favor of {@link Mockito#when(Object)} 
     * <p>
     * How to fix deprecation warnings? Typically it's just few minutes of search & replace job:
     * <pre>
     *   Mockito.stub;  <i>replace with:</i>  Mockito.when;
     *   stub(          <i>replace with:</i>  when(
     *   .toReturn(     <i>replace with:</i>  .thenReturn(
     *   .toThrow(      <i>replace with:</i>  .thenThrow(
     *   .toAnswer(     <i>replace with:</i>  .thenAnswer(
     * </pre>
     * If you're an existing user then sorry for making your code littered with deprecation warnings. 
     * This change was required to make Mockito better.
     * 
     * @param methodCall
     *            method call
     * @return DeprecatedOngoingStubbing object to set stubbed value/exception
     */
    @Deprecated
    public static <T> DeprecatedOngoingStubbing<T> stub(T methodCall) {
        return MOCKITO_CORE.stub(methodCall);
    }
    
    /**
     * Enables stubbing methods. Use it when you want the mock to return particular value when particular method is called. 
     * <p>
     * Simply put: "<b>When</b> the x method is called <b>then</b> return y".
     * <p>
     * <b>when() is a successor of deprecated {@link Mockito#stub(Object)}</b>
     * <p>
     * Examples:
     * 
     * <pre>
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
     * </pre>
     * 
     * For stubbing void methods with throwables see: {@link Mockito#doThrow(Throwable)}
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
     * Let's say you've stubbed foo.bar(). 
     * If your code cares what foo.bar() returns then something else breaks(often before even verify() gets executed).
     * If your code doesn't care what get(0) returns then it should not be stubbed. 
     * Not convinced? See <a href="http://monkeyisland.pl/2008/04/26/asking-and-telling">here</a>.
     * 
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * @param methodCall method to be stubbed
     */
    public static <T> OngoingStubbing<T> when(T methodCall) {
        return MOCKITO_CORE.when(methodCall);
    }

    /**
     * Verifies certain behavior <b>happened once</b> 
     * <p>
     * Alias to <code>verify(mock, times(1))</code> E.g:
     * <pre>
     *   verify(mock).someMethod("some arg");
     * </pre>
     * Above is equivalent to:
     * <pre>
     *   verify(mock, times(1)).someMethod("some arg");
     * </pre>
     * <p>
     * Arguments passed are compared using equals() method.
     * Read about {@link ArgumentCaptor} or {@link ArgumentMatcher} to find out other ways of matching / asserting arguments passed.
     * <p>
     * Although it is possible to verify a stubbed invocation, usually <b>it's just redundant</b>.
     * Let's say you've stubbed foo.bar(). 
     * If your code cares what foo.bar() returns then something else breaks(often before even verify() gets executed).
     * If your code doesn't care what get(0) returns then it should not be stubbed. 
     * Not convinced? See <a href="http://monkeyisland.pl/2008/04/26/asking-and-telling">here</a>.
     * 
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to be verified
     * @return mock object itself
     */
    public static <T> T verify(T mock) {
        return MOCKITO_CORE.verify(mock, times(1));
    }

    /**
     * Verifies certain behavior happened at least once / exact number of times / never. E.g:
     * <pre>
     *   verify(mock, times(5)).someMethod("was called five times");
     *
     *   verify(mock, atLeast(2)).someMethod("was called at least two times");
     *
     *   //you can use flexible argument matchers, e.g:
     *   verify(mock, atLeastOnce()).someMethod(<b>anyString()</b>);
     * </pre>
     *
     * <b>times(1) is the default</b> and can be omitted
     * <p>
     * Arguments passed are compared using equals() method.
     * Read about {@link ArgumentCaptor} or {@link ArgumentMatcher} to find out other ways of matching / asserting arguments passed.
     * <p>
     *
     * @param mock to be verified
     * @param mode times(x), atLeastOnce() or never()
     *
     * @return mock object itself
     */
    public static <T> T verify(T mock, VerificationMode mode) {
        return MOCKITO_CORE.verify(mock, mode);
    }

    /**
     * Smart Mockito users hardly use this feature because they know it could be a sign of poor tests.
     * Normally, you don't need to reset your mocks, just create new mocks for each test method.
     * <p>
     * Instead of reset() please consider writing simple, small and focused test methods over lengthy, over-specified tests.
     * <b>First potential code smell is reset() in the middle of the test method.</b> This probably means you're testing too much.
     * Follow the whisper of your test methods: "Please keep us small & focused on single behavior".
     * There are several threads about it on mockito mailing list.
     * <p>
     * The only reason we added reset() method is to
     * make it possible to work with container-injected mocks.
     * See issue 55 (<a href="http://code.google.com/p/mockito/issues/detail?id=55">here</a>)
     * or FAQ (<a href="http://code.google.com/p/mockito/wiki/FAQ">here</a>).
     * <p>
     * <b>Don't harm yourself.</b> reset() in the middle of the test method is a code smell (you're probably testing too much).
     * <pre>
     *   List mock = mock(List.class);
     *   when(mock.size()).thenReturn(10);
     *   mock.add(1);
     *
     *   reset(mock);
     *   //at this point the mock forgot any interactions & stubbing
     * </pre>
     *
     * @param <T>
     * @param mocks to be reset
     */
    public static <T> void reset(T ... mocks) {
        MOCKITO_CORE.reset(mocks);
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
     * <p>
     * A word of <b>warning</b>: 
     * Some users who did a lot of classic, expect-run-verify mocking tend to use verifyNoMoreInteractions() very often, even in every test method. 
     * verifyNoMoreInteractions() is not recommended to use in every test method. 
     * verifyNoMoreInteractions() is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
     * Abusing it leads to overspecified, less maintainable tests. You can find further reading 
     * <a href="http://monkeyisland.pl/2008/07/12/should-i-worry-about-the-unexpected/">here</a>.
     * <p>
     * This method will also detect unverified invocations that occurred before the test method,
     * for example: in setUp(), &#064;Before method or in constructor.
     * Consider writing nice code that makes interactions only in test methods.
     * 
     * <p>
     * Example:
     * 
     * <pre>
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
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mocks to be verified
     */
    public static void verifyNoMoreInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    /**
     * Verifies that no interactions happened on given mocks.
     * <pre>
     *   verifyZeroInteractions(mockOne, mockTwo);
     * </pre>
     * This method will also detect invocations 
     * that occurred before the test method, for example: in setUp(), &#064;Before method or in constructor.
     * Consider writing nice code that makes interactions only in test methods.  
     * <p>
     * See also {@link Mockito#never()} - it is more explicit and communicates the intent well.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mocks to be verified
     */
    public static void verifyZeroInteractions(Object... mocks) {
        MOCKITO_CORE.verifyNoMoreInteractions(mocks);
    }

    /**
     * <pre>
     *   //Instead of:
     *   stubVoid(mock).toThrow(e).on().someVoidMethod();
     * 
     *   //Please do:
     *   doThrow(e).when(mock).someVoidMethod();
     * </pre> 
     * 
     * doThrow() replaces stubVoid() because of improved readability and consistency with the family of doAnswer() methods. 
     * <p>
     * Originally, stubVoid() was used for stubbing void methods with exceptions. E.g:
     * 
     * <pre>
     * stubVoid(mock).toThrow(new RuntimeException()).on().someMethod();
     * 
     * //you can stub with different behavior for consecutive calls.
     * //Last stubbing (e.g. toReturn()) determines the behavior for further consecutive calls.   
     * stubVoid(mock)
     *   .toThrow(new RuntimeException())
     *   .toReturn()
     *   .on().someMethod();
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @deprecated Use {@link Mockito#doThrow(Throwable)} method for stubbing voids
     * 
     * @param mock
     *            to stub
     * @return stubbable object that allows stubbing with throwable
     */
    public static <T> VoidMethodStubbable<T> stubVoid(T mock) {
        return MOCKITO_CORE.stubVoid(mock);
    }
    
    /**
     * Use doThrow() when you want to stub the void method with an exception.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler does not like void methods inside brackets...
     * <p>
     * Example:
     * 
     * <pre>
     *   doThrow(new RuntimeException()).when(mock).someVoidMethod();
     * </pre>
     * 
     * @param toBeThrown to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doThrow(Throwable toBeThrown) {
        return MOCKITO_CORE.doAnswer(new ThrowsException(toBeThrown));
    }

    /**
     * Use doCallRealMethod() when you want to call the real implementation of a method.
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
     * <pre>
     *   Foo mock = mock(Foo.class);
     *   doCallRealMethod().when(mock).someVoidMethod();
     *
     *   // this will call the real implementation of Foo.someVoidMethod()
     *   mock.someVoidMethod();
     * </pre>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doCallRealMethod() {
        return MOCKITO_CORE.doAnswer(new CallsRealMethods());
    }
    
    /**
     * Use doAnswer() when you want to stub a void method with generic {@link Answer}.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#when(Object)} because the compiler does not like void methods inside brackets...
     * <p>
     * Example:
     * 
     * <pre>
     *  doAnswer(new Answer() {
     *      public Object answer(InvocationOnMock invocation) {
     *          Object[] args = invocation.getArguments();
     *          Mock mock = invocation.getMock();
     *          return null;
     *      }})
     *  .when(mock).someMethod();
     * </pre>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param answer to answer when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doAnswer(Answer answer) {
        return MOCKITO_CORE.doAnswer(answer);
    }  
    
    /**
     * Use doNothing() for setting void methods to do nothing. <b>Beware that void methods on mocks do nothing by default!</b> 
     * However, there are rare situations when doNothing() comes handy:  
     * <p>
     * 1. Stubbing consecutive calls on a void method:
     * <pre>
     *   doNothing().
     *   doThrow(new RuntimeException())
     *   .when(mock).someVoidMethod();
     *   
     *   //does nothing the first time:
     *   mock.someVoidMethod();
     *   
     *   //throws RuntimeException the next time:
     *   mock.someVoidMethod();
     * </pre>
     * 
     * 2. When you spy real objects and you want the void method to do nothing:
     * <pre>
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
     * </pre>
     * <p>
     * See examples in javadoc for {@link Mockito} class
     *   
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doNothing() {
        return MOCKITO_CORE.doAnswer(new DoesNothing());
    }    
    
    /**
     * Use doReturn() in those rare occasions when you cannot use {@link Mockito#when(Object)}.
     * <p>
     * <b>Beware that {@link Mockito#when(Object)} is always recommended for stubbing because it is argument type-safe 
     * and more readable</b> (especially when stubbing consecutive calls). 
     * <p>
     * Here are those rare occasions when doReturn() comes handy:
     * <p>
     * 
     * 1. When spying real objects and calling real methods on a spy brings side effects  
     * 
     * <pre>
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *   
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   when(spy.get(0)).thenReturn("foo");
     *   
     *   //You have to use doReturn() for stubbing:
     *   doReturn("foo").when(spy).get(0);
     * </pre>
     * 
     * 2. Overriding a previous exception-stubbing:
     * 
     * <pre>
     *   when(mock.foo()).thenThrow(new RuntimeException());
     *   
     *   //Impossible: the exception-stubbed foo() method is called so RuntimeException is thrown. 
     *   when(mock.foo()).thenReturn("bar");
     *   
     *   //You have to use doReturn() for stubbing:
     *   doReturn("bar").when(mock).foo();
     * </pre>
     * 
     * Above scenarios shows a tradeoff of Mockito's ellegant syntax. Note that the scenarios are very rare, though. 
     * Spying should be sporadic and overriding exception-stubbing is very rare. Not to mention that in general
     * overridding stubbing is a potential code smell that points out too much stubbing.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param toBeReturned to be returned when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doReturn(Object toBeReturned) {
        return MOCKITO_CORE.doAnswer(new Returns(toBeReturned));
    }
 
    /**
     * Creates InOrder object that allows verifying mocks in order.
     * 
     * <pre>
     *   InOrder inOrder = inOrder(firstMock, secondMock);
     *   
     *   inOrder.verify(firstMock).add("was called first");
     *   inOrder.verify(secondMock).add("was called second");
     * </pre>
     * 
     * Verification in order is flexible - <b>you don't have to verify all interactions</b> one-by-one
     * but only those that you are interested in testing in order.
     * <p>
     * Also, you can create InOrder object passing only mocks that are relevant for in-order verification.  
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mocks to be verified in order
     * 
     * @return InOrder object to be used to verify in order
     */
    public static InOrder inOrder(Object... mocks) {
        return MOCKITO_CORE.inOrder(mocks);
    }
  
    /**
     * Allows verifying exact number of invocations. E.g:
     * <pre>
     *   verify(mock, times(2)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param wantedNumberOfInvocations wanted number of invocations 
     * 
     * @return verification mode
     */
    public static VerificationMode times(int wantedNumberOfInvocations) {
        return VerificationModeFactory.times(wantedNumberOfInvocations);
    }
    
    /**
     * Alias to times(0), see {@link Mockito#times(int)}
     * <p>
     * Verifies that interaction did not happen. E.g:
     * <pre>
     *   verify(mock, never()).someMethod();
     * </pre>
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
    public static VerificationMode never() {
        return times(0);
    }
    
    /**
     * Allows at-least-once verification. E.g:
     * <pre>
     *   verify(mock, atLeastOnce()).someMethod("some arg");
     * </pre>
     * Alias to atLeast(1)
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public static VerificationMode atLeastOnce() {
        return VerificationModeFactory.atLeastOnce();
    }

    /**
     * Allows at-least-x verification. E.g:
     * <pre>
     *   verify(mock, atLeast(3)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param minNumberOfInvocations minimum number of invocations 
     * 
     * @return verification mode
     */
    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return VerificationModeFactory.atLeast(minNumberOfInvocations);
    }

    /**
     * Allows at-most-x verification. E.g:
     * <pre>
     *   verify(mock, atMost(3)).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param maxNumberOfInvocations max number of invocations 
     * 
     * @return verification mode
     */
    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return VerificationModeFactory.atMost(maxNumberOfInvocations);
    }

    /**
     * Allows checking if given method was the only one invoked. E.g:
     * <pre>
     *   verify(mock, only()).someMethod();
     *   //above is a shorthand for following 2 lines of code:
     *   verify(mock).someMethod();
     *   verifyNoMoreInvocations(mock);
     * </pre>
     * 
     * <p>
     * See also {@link Mockito#verifyNoMoreInteractions(Object...)}
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public static VerificationMode only() {
    	return VerificationModeFactory.only();
    }
    
    /**
     * First of all, in case of any trouble, I encourage you to read the Mockito FAQ: <a href="http://code.google.com/p/mockito/wiki/FAQ">http://code.google.com/p/mockito/wiki/FAQ</a>
     * <p>
     * In case of questions you may also post to mockito mailing list: <a href="http://groups.google.com/group/mockito">http://groups.google.com/group/mockito</a> 
     * <p>
     * validateMockitoUsage() <b>explicitly validates</b> the framework state to detect invalid use of Mockito.
     * However, this feature is optional <b>because Mockito validates the usage all the time...</b> but there is a gotcha so read on.
     * <p>
     * Examples of incorrect use:
     * <pre>
     * //Oups, someone forgot thenReturn() part:
     * when(mock.get());
     * 
     * //Oups, someone put the verified method call inside verify() where it should be outside:
     * verify(mock.execute());
     * 
     * //Oups, someone has used EasyMock for too long and forgot to specify the method to verify:
     * verify(mock);
     * </pre>
     * 
     * Mockito throws exceptions if you misuse it so that you know if your tests are written correctly. 
     * The gotcha is that Mockito does the validation <b>next time</b> you use the framework (e.g. next time you verify, stub, call mock etc.). 
     * But even though the exception might be thrown in the next test, 
     * the exception <b>message contains a navigable stack trace element</b> with location of the defect. 
     * Hence you can click and find the place where Mockito was misused.
     * <p>
     * Sometimes though, you might want to validate the framework usage explicitly. 
     * For example, one of the users wanted to put validateMockitoUsage() in his &#064;After method
     * so that he knows immediately when he misused Mockito. 
     * Without it, he would have known about it not sooner than <b>next time</b> he used the framework.
     * One more benefit of having validateMockitoUsage() in &#064;After is that jUnit runner will always fail in the test method with defect
     * whereas ordinary 'next-time' validation might fail the <b>next</b> test method. 
     * But even though JUnit might report next test as red, don't worry about it 
     * and just click at navigable stack trace element in the exception message to instantly locate the place where you misused mockito.   
     * <p>
     * <b>Built-in runner: {@link MockitoJUnitRunner}</b> does validateMockitoUsage() after each test method.
     * <p>
     * Bear in mind that <b>usually you don't have to validateMockitoUsage()</b> 
     * and framework validation triggered on next-time basis should be just enough,
     * mainly because of enhanced exception message with clickable location of defect.
     * However, I would recommend validateMockitoUsage() if you already have sufficient test infrastructure
     * (like your own runner or base class for all tests) because adding a special action to &#064;After has zero cost.
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
     * <pre>
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
     * </pre>
     * {@link MockSettings} has been introduced for two reasons. 
     * Firstly, to make it easy to add another mock settings when the demand comes.
     * Secondly, to enable combining different mock settings without introducing zillions of overloaded mock() methods.
     * <p>
     * See javadoc for {@link MockSettings} to learn about possible mock settings.
     * <p>
     * 
     * @return mock settings instance with defaults.
     */
    public static MockSettings withSettings() {
        return new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
    }

    /*
     * Helps debugging failing tests.
     * <p>
     * TODO: add more info & examples.
     */
    public static MockitoDebugger debug() {
        return new MockitoDebuggerImpl();
    }
}