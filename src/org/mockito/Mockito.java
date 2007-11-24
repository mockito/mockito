package org.mockito;

import org.mockito.exceptions.MissingMethodInvocationException;
import org.mockito.internal.*;

@SuppressWarnings("unchecked")
public class Mockito extends Matchers {

    public final static VerifyingMode anyTimes = VerifyingMode.anyTimes();
    
    public static VerifyingMode anyTimes() {
        return anyTimes;
    }
    
    public static <T> T mock(Class<T> classToMock) {
        MockFactory<T> proxyFactory = new MockFactory<T>();
        MockControl<T> mockControl = new MockControl<T>(MockitoState.instance(), LastArguments.instance());
        return proxyFactory.createMock(classToMock, new ObjectMethodsFilter<MockControl>(
                classToMock, mockControl));
    }

    public static <T> MockitoExpectation<T> stub(T methodCallToStub) {
//        MockitoState.instance().stubbingStarted();
        
        //TODO increment number of stubs
        MockitoExpectation controlToStub = MockitoState.instance().pullControlToBeStubbed();
        if (controlToStub == null) {
            throw new MissingMethodInvocationException();
        }
        return controlToStub;
    }
    
    public static <T> T verify(T mock) {
        return verify(mock, VerifyingMode.anyTimes());
    }
    
    public static <T> T verify(T mock, int expectedNumberOfInvocations) {
        return verify(mock, VerifyingMode.times(expectedNumberOfInvocations));
    }
    
    //TODO should not be public
    public static <T> T verify(T mock, VerifyingMode mode) {
        MockUtil.validateMock(mock);
        MockitoState.instance().verifyingStarted(mode);
        return mock;
    }

	/**
	 * <pre>
	 * Throws an AssertionError if any of given mocks has any unverified interaction.
     * 
     * Use this method after you verified all your mocks - to make sure that nothing 
     * else was invoked on your mocks.
     * 
     * It's a good pattern not to use this method in every test method.
     * Sometimes test method focuses on different behavior/interaction 
     * and it's not necessary to call verifyNoMoreInteractions()
     * 
     * Stubbed invocations are also treated as interactions.
     * 
     * Example:
	 * 
	 *    <code>
	 *         //interactions
	 *         mock.doSomething();
	 *         mock.doSomethingUnexpected();
	 *         
	 *         //verification
	 *         verify(mock).doSomething();
	 *         
	 *         //throws error: 'doSomethingUnexpected()' is unexpected
	 *         verifyNoMoreInteractions(mock);
	 *    </code>
	 *</pre>
	 *
	 * @param mocks
	 */
	public static void verifyNoMoreInteractions(Object ... mocks) {
	    MockitoState.instance().checkForUnfinishedVerification();
	    for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyNoMoreInteractions();
        }
	}

    public static void verifyZeroInteractions(Object ... mocks) {
        MockitoState.instance().checkForUnfinishedVerification();
        for (Object mock : mocks) {
            MockUtil.getControl(mock).verifyZeroInteractions();
        }
    }
    
    public static <T> VoidMethodExpectation<T> stubVoid(T mock) {
//        MockitoState.instance().reportControlForStubbing(mockControl)
        return MockUtil.getControl(mock);
    }

    public static StrictOrderVerifier strictOrderVerifier(Object ... mocks) {
        StrictOrderVerifier strictOrderVerifier = new StrictOrderVerifier();
        for (Object mock : mocks) {
            MockUtil.validateMock(mock);
            strictOrderVerifier.addMock(mock);
        }
        return strictOrderVerifier;
    }
}