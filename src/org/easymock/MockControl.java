/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

import static org.easymock.EasyMock.expect;

import org.easymock.internal.AlwaysMatcher;
import org.easymock.internal.ArrayMatcher;
import org.easymock.internal.EqualsMatcher;
import org.easymock.internal.MocksControl;
import org.easymock.internal.Range;

/**
 * A <code>MockControl</code> object controls the behavior of its associated
 * mock object. For more information, see the EasyMock documentation.
 * 
 * @deprecated Since EasyMock 2.0, static methods on <code>EasyMock</code> are
 * used to create and control mock objects.
 */
public class MockControl<T> {
    private final T mock;

    private final MocksControl ctrl;

    protected MockControl(MocksControl ctrl, Class<T> toMock) {
        this.ctrl = ctrl;
        this.mock = ctrl.createMock(toMock);
    }

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionError</code>.
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static <T> MockControl<T> createControl(Class<T> toMock) {
        return new MockControl<T>((MocksControl) EasyMock.createControl(),
                toMock);
    }

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will check the
     * order of expected method calls. An unexpected method call on the mock
     * object will lead to an <code>AssertionError</code>.
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static <T> MockControl<T> createStrictControl(Class<T> toMock) {
        return new MockControl<T>(
                (MocksControl) EasyMock.createStrictControl(), toMock);
    }

    /**
     * Creates a mock control object for the specified interface. The
     * <code>MockControl</code> and its associated mock object will not check
     * the order of expected method calls. An unexpected method call on the mock
     * object will return an empty value (0, null, false).
     * 
     * @param toMock
     *            the class of the interface to mock.
     * @return the mock control.
     */
    public static <T> MockControl<T> createNiceControl(Class<T> toMock) {
        return new MockControl<T>((MocksControl) EasyMock.createNiceControl(),
                toMock);
    }

    /**
     * Returns the mock object.
     * 
     * @return the mock object of this control
     */
    public T getMock() {
        return mock;
    }

    /**
     * Resets the mock control and the mock object to the state directly after
     * creation.
     */
    public final void reset() {
        ctrl.reset();
    }

    /**
     * Switches the mock object from record state to replay state. For more
     * information, see the EasyMock documentation.
     * 
     * @throws IllegalStateException
     *             if the mock object already is in replay state.
     */
    public void replay() {
        ctrl.replay();
    }

    /**
     * Verifies that all expectations have been met. For more information, see
     * the EasyMock documentation.
     * 
     * @throws IllegalStateException
     *             if the mock object is in record state.
     * @throws AssertionError
     *             if any expectation has not been met.
     */
    public void verify() {
        ctrl.verify();
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning silently.
     * 
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable() {
        expectLastCall(
                "method call on the mock needed before setting void callable")
                .once();
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable) {
        expectLastCall(
                "method call on the mock needed before setting Throwable")
                .andThrow(throwable).once();
    }

    /**
     * Records that the mock object will expect the last method call once, and
     * will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(Object value) {
        expectLastCall(
                "method call on the mock needed before setting return value")
                .andReturn(value).once();
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning silently.
     * 
     * @param times
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable(int times) {
        expectLastCall(
                "method call on the mock needed before setting void callable")
                .times(times);
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @param times
     *            the number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable, int times) {
        expectLastCall(
                "method call on the mock needed before setting Throwable")
                .andThrow(throwable).times(times);
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param times
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(Object value, int times) {
        expectLastCall(
                "method call on the mock needed before setting return value")
                .andReturn(value).times(times);
    }

    /**
     * Records that the mock object will expect the last method call a fixed
     * number of times, and will react by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param range
     *            the number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(Object value, Range range) {
        IExpectationSetters setter = expectLastCall(
                "method call on the mock needed before setting return value")
                .andReturn(value);
        callWithConvertedRange(setter, range);
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call.
     * 
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setDefaultVoidCallable() {
        ((MocksControl) expectLastCall("method call on the mock needed before setting default void callable"))
                .setLegacyDefaultVoidCallable();
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by throwing the provided
     * Throwable.
     * 
     * @param throwable
     *            throwable the throwable to be thrown
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     * @exception IllegalStateException
     *                if the mock object is in replay state, or if no method was
     *                called on the mock object before.
     */
    public void setDefaultThrowable(Throwable throwable) {
        ctrl.setLegacyDefaultThrowable(throwable);
    }

    /**
     * Records that the mock object will by default allow the last method
     * specified by a method call, and will react by returning the provided
     * return value.
     * 
     * @param value
     *            the return value.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setDefaultReturnValue(Object value) {
        ctrl.setLegacyDefaultReturnValue(value);
    }

    /**
     * Sets the ArgumentsMatcher for the last method called on the mock object.
     * The matcher must be set before any behavior for the method is defined.
     * 
     * @throws IllegalStateException
     *             if called in replay state, or if no method was called on the
     *             mock object before.
     */
    public void setMatcher(ArgumentsMatcher matcher) {
        ctrl.setLegacyMatcher(matcher);
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning silently.
     * 
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state, if no method was
     *                called on the mock object before, or if the last method
     *                called on the mock was no void method.
     */
    public void setVoidCallable(int minCount, int maxCount) {
        expectLastCall(
                "method call on the mock needed before setting void callable")
                .times(minCount, maxCount);
    }

    public void setVoidCallable(Range range) {
        IExpectationSetters setter = expectLastCall("method call on the mock needed before setting void callable");
        callWithConvertedRange(setter, range);
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by throwing the provided Throwable.
     * 
     * @param throwable
     *            the Throwable to throw.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @exception IllegalStateException
     *                if the mock object is in replay state or if no method was
     *                called on the mock object before.
     * @exception IllegalArgumentException
     *                if the last method called on the mock cannot throw the
     *                provided Throwable.
     * @exception NullPointerException
     *                if throwable is null.
     */
    public void setThrowable(Throwable throwable, int minCount, int maxCount) {
        expectLastCall(
                "method call on the mock needed before setting Throwable")
                .andThrow(throwable).times(minCount, maxCount);
    }

    public void setThrowable(Throwable throwable, Range range) {
        IExpectationSetters setter = expectLastCall(
                "method call on the mock needed before setting Throwable")
                .andThrow(throwable);
        callWithConvertedRange(setter, range);
    }

    /**
     * Records that the mock object will expect the last method call between
     * <code>minCount</code> and <code>maxCount</code> times, and will react
     * by returning the provided return value.
     * 
     * @param value
     *            the return value.
     * @param minCount
     *            the minimum number of times that the call is expected.
     * @param maxCount
     *            the maximum number of times that the call is expected.
     * @throws IllegalStateException
     *             if the mock object is in replay state, if no method was
     *             called on the mock object before. or if the last method
     *             called on the mock does not return <code>boolean</code>.
     */
    public void setReturnValue(Object value, int minCount, int maxCount) {
        expectLastCall(
                "method call on the mock needed before setting return value")
                .andReturn(value).times(minCount, maxCount);
    }

    /**
     * Exactly one call.
     */
    public static final Range ONE = MocksControl.ONCE;

    /**
     * One or more calls.
     */
    public static final Range ONE_OR_MORE = MocksControl.AT_LEAST_ONCE;

    /**
     * Zero or more calls.
     */
    public static final Range ZERO_OR_MORE = MocksControl.ZERO_OR_MORE;

    /**
     * Matches if each expected argument is equal to the corresponding actual
     * argument.
     */
    public static final ArgumentsMatcher EQUALS_MATCHER = new EqualsMatcher();

    /**
     * Matches always.
     */
    public static final ArgumentsMatcher ALWAYS_MATCHER = new AlwaysMatcher();

    /**
     * Matches if each expected argument is equal to the corresponding actual
     * argument for non-array arguments; array arguments are compared with the
     * appropriate <code>java.util.Arrays.equals()</code> -method.
     */
    public static final ArgumentsMatcher ARRAY_MATCHER = new ArrayMatcher();

    /**
     * Sets the default ArgumentsMatcher for all methods of the mock object. The
     * matcher must be set before any behavior is defined on the mock object.
     * 
     * @throws IllegalStateException
     *             if called in replay state, or if any behavior is already
     *             defined on the mock object.
     */
    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        ctrl.setLegacyDefaultMatcher(matcher);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object)}. For explanation, see
     * "Convenience Methods for Return Values" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public <V1, V2 extends V1> void expectAndReturn(V1 ignored, V2 value) {
        EasyMock.expectLastCall().andReturn(value).once();
    }

    public void expectAndReturn(int ignored, int value) {
        this.expectAndReturn((Object) ignored, (Object) value);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, Range)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public <V1, V2 extends V1> void expectAndReturn(V1 ignored, V2 value,
            Range range) {
        IExpectationSetters expectAndReturn = EasyMock.expectLastCall()
                .andReturn(value);
        callWithConvertedRange(expectAndReturn, range);
    }

    public void expectAndReturn(int ignored, int value, Range range) {
        this.expectAndReturn((Object) ignored, (Object) value, range);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public <V1, V2 extends V1> void expectAndReturn(V1 ignored, V2 value,
            int count) {
        EasyMock.expectLastCall().andReturn(value).times(count);
    }

    public void expectAndReturn(int ignored, int value, int count) {
        this.expectAndReturn((Object) ignored, (Object) value, count);
    }

    /**
     * Same as {@link MockControl#setReturnValue(Object, int, int)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public <V1, V2 extends V1> void expectAndReturn(V1 ignored, V2 value,
            int min, int max) {
        EasyMock.expectLastCall().andReturn(value).times(min, max);
    }

    public void expectAndReturn(int ignored, int value, int min, int max) {
        this.expectAndReturn((Object) ignored, (Object) value, min, max);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable)}. For explanation,
     * see "Convenience Methods for Throwables" in the EasyMock documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable) {
        EasyMock.expect(ignored).andThrow(throwable).once();
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, Range)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, Range range) {
        IExpectationSetters setter = EasyMock.expect(ignored).andThrow(
                throwable);
        callWithConvertedRange(setter, range);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, int count) {
        expect(ignored).andThrow(throwable).times(count);
    }

    /**
     * Same as {@link MockControl#setThrowable(Throwable, int, int)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndThrow(Object ignored, Throwable throwable, int min,
            int max) {
        expect(ignored).andThrow(throwable).times(min, max);
    }

    /**
     * Same as {@link MockControl#setDefaultReturnValue(Object)}. For
     * explanation, see "Convenience Methods for Return Values" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public <V1, V2 extends V1> void expectAndDefaultReturn(V1 ignored, V2 value) {
        EasyMock.expectLastCall().andStubReturn(value);
    }

    /**
     * Same as {@link MockControl#setDefaultThrowable(Throwable)}. For
     * explanation, see "Convenience Methods for Throwables" in the EasyMock
     * documentation.
     * 
     * @param ignored
     *            an ignored value.
     */
    public void expectAndDefaultThrow(Object ignored, Throwable throwable) {
        expectLastCall(
                "method call on the mock needed before setting default Throwable")
                .andStubThrow(throwable);
    }

    private IExpectationSetters<Object> expectLastCall(String failureMessage) {
        try {
            return EasyMock.expectLastCall();
        } catch (IllegalStateException e) {
            throw new IllegalStateException(failureMessage);
        }
    }

    private void callWithConvertedRange(IExpectationSetters setter, Range range) {
        if (range == ONE) {
            setter.once();
        } else if (range == ONE_OR_MORE) {
            setter.atLeastOnce();
        } else if (range == ZERO_OR_MORE) {
            setter.anyTimes();
        } else {
            throw new IllegalArgumentException("Unexpected Range");
        }
    }

}