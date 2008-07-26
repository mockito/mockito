/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.Arrays;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.OngoingStubbing;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockito.internal.stubbing.Returns;
import org.mockito.internal.stubbing.DoesNothing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.stubbing.ThrowsException;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

/**
 * Enables mocks creation, verification and stubbing.
 * 
 * <h1>Contents</h1>
 * 
 * <b> 
 *      1. Let's verify some behaviour! <br/> 
 *      2. How about some stubbing? <br/>
 *      3. Argument matchers <br/> 
 *      4. Verifying exact number of invocations / at least once / never <br/> 
 *      5. Stubbing void methods with exceptions <br/> 
 *      6. Verification in order <br/> 
 *      7. Making sure interaction(s) never happened on mock <br/> 
 *      8. Finding redundant invocations <br/> 
 *      9. Shorthand for mocks creation - &#064;Mock annotation <br/> 
 *      10. (**New**) Stubbing consecutive calls (iterator-style stubbing) <br/> 
 *      11. (**Totally New**) Stubbing with callbacks <br/>
 *      12. (**Totally New**) doThrow()|doAnswer()|doNothing()|doReturn() family of methods mostly for stubbing voids <br/>
 *      13. (**Totally New**) Spying on real objects <br/>
 * </b>
 * 
 * <p>
 * Following examples mock List, because everyone knows its interface (methods
 * like add(), get(), clear() will be used). <br>
 * You probably wouldn't mock List class 'in real'.
 * 
 * <h3>1. Let's verify some behaviour!</h3>
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
 * Once created, mock will remember all invocations. Then you can selectively
 * verify whatever interaction you are interested in.
 * 
 * <h3>2. How about some stubbing?</h3>
 * 
 * <pre>
 * //You can mock concrete classes, not only interfaces
 * LinkedList mockedList = mock(LinkedList.class);
 * 
 * //stubbing
 * stub(mockedList.get(0)).toReturn("first");
 * stub(mockedList.get(1)).toThrow(new RuntimeException());
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
 * fixture setup but test methods can override it. </li>
 * 
 * <li> Once stubbed, mocked method will always return stubbed value regardless
 * of how many times it is called. </li>
 * 
 * <li> Last stubbing is more important - when you stubbed the same method with
 * the same arguments many times. </li>
 * 
 * </ul>
 * 
 * <h3>3. Argument matchers</h3>
 * 
 * <pre>
 * //stubbing using built-in anyInt() argument matcher
 * stub(mockedList.get(anyInt())).toReturn("element");
 * 
 * //stubbing using hamcrest (let's say isValid() returns your own hamcrest matcher):
 * stub(mockedList.contains(argThat(isValid()))).toReturn("element");
 * 
 * //following prints "element"
 * System.out.println(mockedList.get(999));
 * 
 * //<b>you can also verify using argument matcher</b>
 * verify(mockedList).get(anyInt());
 * </pre>
 * 
 * <p>
 * Argument matchers allow flexible verification or stubbing. See the <b>whole
 * library of</b> {@link Matchers} including examples of <b>custom argument
 * matchers / hamcrest matchers</b>.
 * <p>
 * <b>Warning:</b>
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
 *   //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </pre>
 * 
 * <h3>4. Verifying exact number of invocations / at least once / never</h3>
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
 * //verification using atLeastOnce()
 * verify(mockedList, atLeastOnce()).add("three times");
 * 
 * </pre>
 * 
 * <p>
 * <b>times(1) is the default.</b> Therefore using times(1) explicitly can be
 * omitted.
 * 
 * <h3>5. Stubbing void methods with exceptions</h3>
 * 
 * {@link Mockito#doThrow(Throwable)} replaces {@link Mockito#stubVoid(Object)} because of improved readability and consistency with the family of doAnswer() methods. 
 * <p>
 * See paragraph 12.
 * 
 * <h3>6. Verification in order</h3>
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
 * <h3>7. Making sure interaction(s) never happened on mock</h3>
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
 * <p>
 * Instead of verifyZeroInteractions() you can call verifyNoMoreInteractions()
 * but the first one is more explicit and can read better.
 * 
 * <h3>8. Finding redundant invocations</h3>
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
 * Some users who did a lot of classic, expect-run-verify mocking tend to use verifyNoMoreInteractions() very often, even in every test method. 
 * verifyNoMoreInteractions() is not recommended to use in every test method. 
 * verifyNoMoreInteractions() is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
 * Abusing it leads to overspecified, less maintainable tests. You can find further reading 
 * <a href="http://monkeyisland.pl/2008/07/12/should-i-worry-about-the-unexpected/">here</a>.
 * 
 * <p>   
 * 
 *  See also {@link Mockito#never()} - it is more explicit and
 * communicates an intent well.
 * <p>
 * 
 * <h3>9. Shorthand for mocks creation - &#064;Mock annotation</h3>
 * 
 * <ul>
 * <li>Minimizes repetitive mock creation code.</li>
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because <b>field name</b>
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
 * Examples how to write a junit test runner are in Mockito test code
 * (mockito/test/org/mockitousage/examples/junitrunner package);
 * <p>
 * Read more here: {@link MockitoAnnotations}
 * 
 * <h3> 10. (**New**) Stubbing consecutive calls (iterator-style stubbing)</h3>
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
 * stub(mock.someMethod("some arg"))
 *   .toThrow(new RuntimeException())
 *   .toReturn("foo");
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
 * <h3> 11. (**Totally New**) Stubbing with callbacks</h3>
 * 
 * Allows stubbing with generic {@link Answer} interface.
*  <p>
 * Yet another controversial feature which was not included in Mockito
 * originally. We recommend using simple stubbing with toReturn() or
 * toThrow() only. Those two should be <b>just enough</b> to test/test-drive
 * any decent (clean & simple) code.
 * 
 * <pre>
 * stub(mock.someMethod(anyString())).toAnswer(new Answer() {
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
 * <h3> 12. (**Totally New**) doThrow()|doAnswer()|doNothing()|doReturn() family of methods for stubbing voids (mostly)</h3>
 * 
 * Stubbing voids requires different approach from {@link Mockito#stub(Object)} because the compiler does not like void methods inside brackets...
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
 * <h3> 13. (**Totally New**) Spying on real objects</h3>
 * 
 * You can create spies of real objects. When you use the spy then the <b>real</b> methods are called (unless a method was stubbed).
 * <p>
 * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
 * <p>
 * Spying on real objects is associated with "partial mocking" concept. 
 * 
 * <pre>
 *   List list = new LinkedList();
 *   List spy = spy(list);
 * 
 *   //optionally, you can stub out some methods:
 *   stub(spy.size()).toReturn(100);
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
 * Sometimes it's impossible to use {@link Mockito#stub(Object)} for stubbing spies. Example:
 * 
 * <pre>
 *   List list = new LinkedList();
 *   List spy = spy(list);
 *   
 *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
 *   stub(spy.get(0)).toReturn("foo");
 *   
 *   //You have to use doReturn() for stubbing
 *   doReturn("foo").when(spy).get(0);
 * </pre>
 */
@SuppressWarnings("unchecked")
public class Mockito extends Matchers {
    
    private static final Reporter REPORTER = new Reporter();
    static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();

    /**
     * Creates mock object of given class or interface.
     * <p>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param classToMock class or interface to mock
     * @return mock object
     */
    public static <T> T mock(Class<T> classToMock) {
        return mock(classToMock, null);
    }
    
    /**
     * Creates mock with a name. Naming mocks can be helpful for debugging. 
     * <p>
     * Beware that naming mocks is not a solution for complex code which uses too many mocks or collaborators. In that case we recommend merciless refactoring.
     * <p>
     * If you use &#064;Mock annotation then you've got naming mocks for free. &#064;Mock uses field name as mock name.
     * <p>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param classToMock class or interface to mock
     * @return mock object
     */
    public static <T> T mock(Class<T> classToMock, String name) {
        return MockUtil.createMock(classToMock, MOCKING_PROGRESS, name, null);
    }

    /**
     * Creates a spy of the real object. The spy calls <b>real</b> methods unless they are stubbed.
     * <p>
     * Real spies should be used <b>carefully and occasionally</b>, for example when dealing with legacy code.
     * <p>
     * Spying on real objects is associated with "partial mocking" concept.
     * <p>
     * Example:
     * 
     * <pre>
     *   List list = new LinkedList();
     *   List spy = spy(list);
     * 
     *   //optionally, you can stub out some methods:
     *   stub(spy.size()).toReturn(100);
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
     * Sometimes it's impossible to use {@link Mockito#stub(Object)} for stubbing spies. Example:
     * 
     * <pre>
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *   
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   stub(spy.get(0)).toReturn("foo");
     *   
     *   //You have to use doReturn() for stubbing
     *   doReturn("foo").when(spy).get(0);
     * </pre>
     * 
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param object
     *            to spy on
     * @return a spy of the real object
     */
    public static <T> T spy(T object) {
        return MockUtil.createMock((Class<T>) object.getClass(), MOCKING_PROGRESS, null, object);
    }

    /**
     * Stubs with return value or exception. E.g:
     * 
     * <pre>
     *   stub(mock.someMethod()).toReturn(10);
     *   
     *   //you can use flexible argument matchers, e.g:
     *   stub(mock.someMethod(<b>anyString()</b>)).toReturn(10);
     *   
     *   //setting exception to be thrown:
     *   stub(mock.someMethod("some arg")).toThrow(new RuntimeException());
     *   
     *   //you can stub with different behavior for consecutive calls.
     *   //Last stubbing (e.g: toReturn("foo")) determines the behavior for further consecutive calls.   
     *   stub(mock.someMethod("some arg"))
     *    .toThrow(new RuntimeException())
     *    .toReturn("foo");
     *   
     * </pre>
     * 
     * For stubbing void methods with throwables see: {@link Mockito#stubVoid}
     * <p>
     * Stubbing can be overridden: for example common stubbing can go to fixture
     * setup but test methods can override it.
     * <p>
     * Once stubbed, mocked method will always return stubbed value regardless
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
     * 
     * @param methodCall
     *            method call
     * @return OngoingStubbing object to set stubbed value/exception
     */
    @SuppressWarnings("unchecked")
    public static <T> OngoingStubbing<T> stub(T methodCall) {
        MOCKING_PROGRESS.stubbingStarted();

        OngoingStubbing stubbable = MOCKING_PROGRESS.pullOngoingStubbing();
        if (stubbable == null) {
            REPORTER.missingMethodInvocation();
        }
        return stubbable;
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
     * 
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
        return verify(mock, times(1));
    }

    /**
     * Verifies certain behavior happened at least once / exact number of times / never. E.g:
     * <pre>
     *   verify(mock, times(5)).someMethod("was called five times");
     *   
     *   verify(mock, atLeastOnce()).someMethod("was called at least once");
     *   
     *   //you can use flexible argument matchers, e.g:
     *   verify(mock, atLeastOnce()).someMethod(<b>anyString()</b>);
     * </pre>
     * 
     * <b>times(1) is the default</b> and can be omitted
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to be verified
     * @param mode times(x), atLeastOnce() or never()
     * 
     * @return mock object itself
     */
    public static <T> T verify(T mock, VerificationMode mode) {
        if (mock == null) {
            REPORTER.nullPassedToVerify();
        } else if (!MockUtil.isMock(mock)) {
            REPORTER.notAMockPassedToVerify();
        }
        MOCKING_PROGRESS.verificationStarted(mode);
        return mock;
    }

    /**
     * Throws an AssertionError if any of given mocks has any unverified
     * interaction.
     * <p>
     * You can use this method after you verified your mocks - to make sure that nothing
     * else was invoked on your mocks.
     * <p>
     * See also {@link Mockito#never()} - it is more explicit and communicates an intent well.
     * <p>
     * Stubbed invocations (if called) are also treated as interactions.
     * <p>
     * Some users who did a lot of classic, expect-run-verify mocking tend to use verifyNoMoreInteractions() very often, even in every test method. 
     * verifyNoMoreInteractions() is not recommended to use in every test method. 
     * verifyNoMoreInteractions() is a handy assertion from the interaction testing toolkit. Use it only when it's relevant.
     * Abusing it leads to overspecified, less maintainable tests. You can find further reading 
     * <a href="http://monkeyisland.pl/2008/07/12/should-i-worry-about-the-unexpected/">here</a>.
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
        assertMocksNotEmpty(mocks);
        MOCKING_PROGRESS.validateState();
        for (Object mock : mocks) {
            try {
                if (mock == null) {
                    REPORTER.nullPassedToVerifyNoMoreInteractions();
                }
                MockUtil.getMockHandler(mock).verifyNoMoreInteractions();
            } catch (NotAMockException e) {
                REPORTER.notAMockPassedToVerifyNoMoreInteractions();
            }
        }
    }

    /**
     * Verifies that no interactions happened on given mocks.
     * <pre>
     *   verifyZeroInteractions(mockOne, mockTwo);
     * </pre>
     * 
     * Instead of verifyZeroInteractions() you can call verifyNoMoreInteractions() but 
     * the first one is more explicit and can read better.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mocks to be verified
     */
    public static void verifyZeroInteractions(Object... mocks) {
        verifyNoMoreInteractions(mocks);
    }

    private static void assertMocksNotEmpty(Object[] mocks) {
        if (mocks == null || mocks.length == 0) {
            REPORTER.mocksHaveToBePassedToVerifyNoMoreInteractions();
        }
    }

    /**
     * <b>Deprecated</b>
     * 
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
     * stub(mock)
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
        MockHandler<T> handler = MockUtil.getMockHandler(mock);
        MOCKING_PROGRESS.stubbingStarted();
        return handler.voidMethodStubbable(mock);
    }
    
    /**
     * Use doThrow() when you want to stub the void method with an exception.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#stub(Object)} because the compiler does not like void methods inside brackets...
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
        return doAnswer(new ThrowsException(toBeThrown));
    }
    
    /**
     * Use doAnswer() when you want to stub a void method with generic {@link Answer}.
     * <p>
     * Stubbing voids requires different approach from {@link Mockito#stub(Object)} because the compiler does not like void methods inside brackets...
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
     * 
     * @param answer to answer when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doAnswer(Answer answer) {
        MOCKING_PROGRESS.stubbingStarted();
        return new StubberImpl().doAnswer(answer);
    }  
    
    /**
     * Use doNothing() for setting void methods to do nothing. <b>Beware</b> that void methods do nothing by default! 
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
     *   
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doNothing() {
        return doAnswer(new DoesNothing());
    }    
    
    /**
     * Use doReturn() in those rare occasions when you cannot use {@link Mockito#stub(Object)}.
     * <p>
     * Beware that {@link Mockito#stub(Object)} is <b>always recommended</b> for stubbing because it is argument type-safe 
     * and more readable (especially when stubbing consecutive calls). 
     * <p>
     * However, there are occasions when doReturn() comes handy:
     * <p>
     * 
     * 1. When spying real objects and calling real methods on a spy brings side effects  
     * 
     * <pre>
     *   List list = new LinkedList();
     *   List spy = spy(list);
     *   
     *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
     *   stub(spy.get(0)).toReturn("foo");
     *   
     *   //You have to use doReturn() for stubbing:
     *   doReturn("foo").when(spy).get(0);
     * </pre>
     * 
     * 2. Overriding a previous exception-stubbing:
     * 
     * <pre>
     *   stub(mock.foo()).toThrow(new RuntimeException());
     *   
     *   //Impossible: the exception-stubbed foo() method is really called so RuntimeException is thrown. 
     *   stub(mock.foo()).toReturn("bar");
     *   
     *   //You have to use doReturn() for stubbing:
     *   doReturn("bar").when(mock).foo();
     * </pre>
     * 
     * Above scenario shows a tradeoff of Mockito's ellegant syntax. The scenario is very rare, though.
     * 
     * @param toBeReturned to be returned when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    public static Stubber doReturn(Object toBeReturned) {
        return doAnswer(new Returns(toBeReturned));
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
     *
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mocks to be verified in order
     * 
     * @return InOrder object to be used to verify in order
     */
    public static InOrder inOrder(Object... mocks) {
        if (mocks == null || mocks.length == 0) {
            REPORTER.mocksHaveToBePassedWhenCreatingInOrder();
        }
        for (Object mock : mocks) {
            if (mock == null) {
                REPORTER.nullPassedWhenCreatingInOrder();
            } else if (!MockUtil.isMock(mock)) {
                REPORTER.notAMockPassedWhenCreatingInOrder();
            }
        }
        InOrder inOrderVerifier = new InOrderVerifier(Arrays.asList(mocks));
        return inOrderVerifier;
    }

    /**
     * Allows at-least-once verification. E.g:
     * <pre>
     *   verify(mock, atLeastOnce()).someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public static VerificationMode atLeastOnce() {
        return VerificationModeImpl.atLeastOnce();
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
        return VerificationModeImpl.times(wantedNumberOfInvocations);
    }
    
    /**
     * Alias to times(0), see {@link Mockito#times(int)}
     * <p>
     * Verifies that interaction did not happen
     * <pre>
     *   verify(mock, never()).someMethod();
     * </pre>
     * 
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @return verification mode
     */
    public static VerificationMode never() {
        return times(0);
    }
}