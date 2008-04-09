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
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;

/**
 * Enables mocks creation, verification and stubbing.
 * <p>
 * Following examples mock List, because everyone knows its interface (methods like add(), get(), clear() will be used). 
 * <br>You probably wouldn't mock List class 'in real'.  
 * 
 * <h3>Let's verify some behaviour!</h3>
 * 
 * <pre>
 * //Let's import Mockito statically so that code looks clearer
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
 * Once created, mock will remember all invocations. Then you can
 * selectively verify whatever interaction you are interested in.
 * 
 * <h3>How about some stubbing?</h3>
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
 * //if you really want you can still verify stubbed invocation. In most cases it's not necessary, though. 
 * verify(mockedList).get(0);
 * </pre>
 * 
 * <ul>
 * <li>
 * By default, for all methods that return value, mock returns null,
 * an empty collection or appropriate primitive/primitive wrapper value 
 * (e.g: 0, false, ... for int/Integer, boolean/Boolean, ...).
 * </li>
 * <li>
 * Stubbing can be overridden: for example common stubbing can go to fixture setup
 * but test methods can override it.
 * </li>
 * <li>
 * Once stubbed, mocked method will always return stubbed value regardless of how many times it is called.
 * </li>
 * <li>
 * Last stubbing is more important - when you stubbed the same method with the same arguments many times.
 * </li>
 * <li>
 * Although it's possible to verify stubbed methods it's a good pattern to focus on testing that stubbed value is used correctly.
 * </li> 
 * </ul>
 * 
 * <h3>Argument matchers</h3>
 * 
 * <pre>
 *  //stubbing using built-in anyInt() argument matcher
 *  stub(mockedList.get(anyInt())).toReturn("element");
 *  
 *  //stubbing using hamcrest (let's say isValid() returns your own hamcrest matcher):
 *  stub(mockedList.contains(argThat(isValid()))).toReturn("element");
 *  
 *  //following prints "element"
 *  System.out.println(mockedList.get(999));
 *  
 *  //<b>you can also verify using argument matcher</b>
 *  verify(mockedList).get(anyInt());
 * </pre>
 * 
 * <p>
 * Argument matchers allow flexible verification or stubbing. 
 * See the <b>whole library of</b> {@link Matchers} including examples of <b>custom argument matchers / hamcrest matchers</b>. 
 * <p>
 * <b>Warning:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided by matchers.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * <pre>
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *   
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </pre>
 * 
 * <h3>Verifying exact number of invocations / at least once / never</h3>
 *
 *<pre>
 *  //using mock 
 *  mockedList.add("once");
 *  
 *  mockedList.add("twice");
 *  mockedList.add("twice");
 *  
 *  mockedList.add("three times");
 *  mockedList.add("three times");
 *  mockedList.add("three times");
 *  
 *  //following two verifications work exactly the same - times(1) is used by default
 *  verify(mockedList).add("once");
 *  verify(mockedList, times(1)).add("once");
 *  
 *  //exact number of invocations verification
 *  verify(mockedList, times(2)).add("twice");
 *  verify(mockedList, times(3)).add("three times");
 *  
 *  //verification using never(). never() is an alias to times(0)
 *  verify(mockedList, never()).add("never happened");
 *  
 *  //verification using atLeastOnce()
 *  verify(mockedList, atLeastOnce()).add("three times");
 *  
 * </pre>
 * 
 * <p>
 * <b>times(1) is the default.</b> Therefore using times(1) explicitly can be omitted.
 * 
 * <h3>Stubbing void methods with an exceptions</h3>
 * 
 * <pre>
 *   stubVoid(mockedList).toThrow(new RuntimeException()).on().clear();
 *   
 *   //following throws exception
 *   mockedList.clear();
 * </pre>
 *
 * <h3>Verification in order</h3>
 * 
 * <pre>
 *   List firstMock = mock(List.class);
 *   List secondMock = mock(List.class);
 *   
 *   //using mocks
 *   firstMock.add("was called first");
 *   secondMock.add("was called second");
 *   
 *   //create inOrder object passing any mocks that need to be verified in order
 *   InOrder inOrder = inOrder(firstMock, secondMock);
 *   
 *   //following will make sure that firstMock was called before secondMock
 *   inOrder.verify(firstMock).add("was called first");
 *   inOrder.verify(secondMock).add("was called second");
 * </pre>
 * 
 * Verification in order is flexible - <b>you don't have to verify all interactions</b> one-by-one
 * but only those that you are interested in testing in order. 
 * <p>
 * Also, you can create InOrder object passing only mocks that relevant for in-order verification.  
 *
 * <h3>Making sure interaction(s) never happened on mock</h3>
 * 
 * <pre>
 *   //using mocks - only mockOne is interacted
 *   mockOne.add("one");
 *   
 *   //ordinary verification
 *   verify(mockOne).add("one");
 *   
 *   //verify that method was never called on a mock
 *   verify(mockOne, never()).add("two"); 
 *   
 *   //verify that other mocks were not interacted
 *   verifyZeroInteractions(mockTwo, mockThree);
 *   
 *   //following works exactly the same as above
 *   verifyNoMoreInteractions(mockTwo, mockThree);
 * </pre>
 *
 * See more {@link Mockito#verifyNoMoreInteractions}
 * 
 * <p>
 * Instead of verifyZeroInteractions() you can call verifyNoMoreInteractions() but 
 * the first one is more explicit and can read better.
 *
 * <h3>Finding redundant invocations</h3>
 * 
 * <pre>
 *   //using mocks
 *   mockedList.add("one");
 *   mockedList.add("two");
 *   
 *   verify(mockedList).add("one");
 *   
 *   //following verification will fail 
 *   verifyNoMoreInteractions(mockedList);
 * </pre>
 * 
 * Remember that usually it's not necessary to call verifyNoMoreInteractions() all the time.
 * See also {@link Mockito#never()} - it is more explicit and communicates an intent well. 
 * <p>
 * 
 * <h3>Shorthand for mocks creation - &#064;Mock annotation</h3>
 * 
 * <ul>
 * <li>Minimizes repetitive mock creation code.</li> 
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because <b>field name</b> is used to identify the mock.</li>
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
 * <b>Important!</b> This needs to be somewhere in the base class or a test runner:
 *   
 * <pre>
 *   MockitoAnnotations.initMocks(testClass);
 * </pre>
 * 
 * Examples how to write a junit test runner are in Mockito test code (mockito/test/org/mockitousage/examples/junitrunner package);
 * <p>
 * Read more here: {@link MockitoAnnotations}
 */
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
        return MockUtil.createMock(classToMock, null, MOCKING_PROGRESS);
    }
    
    static <T> T mock(Class<T> classToMock, String name) {
        return MockUtil.createMock(classToMock, name, MOCKING_PROGRESS);
    }

    /**
     * Stubs with return value or exception. E.g:
     * <pre>
     *   stub(mock.someMethod()).toReturn(10);
     *   
     *   //you can use flexible argument matchers, e.g:
     *   stub(mock.someMethod(<b>anyString()</b>)).toReturn(10);
     *   
     *   //setting exception to be thrown:
     *   stub(mock.someMethod("some arg")).toThrow(new RuntimeException());
     * </pre>
     *
     * For stubbing void methods with throwables see: {@link Mockito#stubVoid}
     * <p>
     * Stubbing can be overridden: for example common stubbing can go to fixture setup
     * but test methods can override it.
     * <p>
     * Once stubbed, mocked method will always return stubbed value regardless of how many times it is called.
     * <p>
     * Last stubbing is more important - when you stubbed the same method with the same arguments many times.
     * <p>
     * Although it's possible to verify stubbed methods it's a good pattern to focus on testing that stubbed value is used correctly.
     * <p>
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param methodCall method call
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
        if (!MockUtil.isMock(mock)) {
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
     * Usually it's not necessary to call verifyNoMoreInteractions() all the time.
     * See also {@link Mockito#never()} - it is more explicit and communicates an intent well.
     * <p>
     * Stubbed invocations (if called) are also treated as interactions.
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
     * Stubs void method with an exception. E.g:
     * <pre>
     *   stubVoid(mock).toThrow(new RuntimeException()).on().someMethod("some arg");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to stub
     * @return stubbable object that allows stubbing with throwable
     */
    public static <T> VoidMethodStubbable<T> stubVoid(T mock) {
        MockHandler<T> handler = MockUtil.getMockHandler(mock);
        MOCKING_PROGRESS.stubbingStarted();
        return handler.voidMethodStubbable();
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
     * Also, you can create InOrder object passing only mocks that relevant for in-order verification.  
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
            if (!MockUtil.isMock(mock)) {
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