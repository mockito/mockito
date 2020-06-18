/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.internal.util.ObjectMethodsGuru.isCompareToMethod;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.io.Serializable;
import java.util.*;

import org.mockito.internal.util.JavaEightUtil;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

/**
 * Default answer of every Mockito mock.
 * <ul>
 * <li>
 * Returns appropriate primitive for primitive-returning methods
 * </li>
 * <li>
 * Returns consistent values for primitive wrapper classes (e.g. int-returning method returns 0 <b>and</b> Integer-returning method returns 0, too)
 * </li>
 * <li>
 * Returns empty collection for collection-returning methods (works for most commonly used collection types)
 * </li>
 * <li>
 * Returns description of mock for toString() method
 * </li>
 * <li>
 * Returns zero if references are equals otherwise non-zero for Comparable#compareTo(T other) method (see issue 184)
 * </li>
 * <li>
 * Returns an {@code java.util.Optional#empty() empty Optional} for Optional. Similarly for primitive optional variants.
 * </li>
 * <li>
 * Returns an {@code java.util.stream.Stream#empty() empty Stream} for Stream. Similarly for primitive stream variants.
 * </li>
 * <li>
 * Returns an {@code java.time.Duration.ZERO zero Duration} for empty Duration and {@code java.time.Period.ZERO zero Period} for empty Period.
 * </li>
 * <li>
 * Returns null for everything else
 * </li>
 * </ul>
 */
public class ReturnsEmptyValues implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 1998191268711234347L;

    /* (non-Javadoc)
     * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
     */
    public Object answer(InvocationOnMock invocation) {
        if (isToStringMethod(invocation.getMethod())) {
            Object mock = invocation.getMock();
            MockName name = MockUtil.getMockName(mock);
            if (name.isDefault()) {
                return "Mock for "
                        + MockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()
                        + ", hashCode: "
                        + mock.hashCode();
            } else {
                return name.toString();
            }
        } else if (isCompareToMethod(invocation.getMethod())) {
            // see issue 184.
            // mocks by default should return 0 if references are the same, otherwise some other
            // value because they are not the same. Hence we return 1 (anything but 0 is good).
            // Only for compareTo() method by the Comparable interface
            return invocation.getMock() == invocation.getArgument(0) ? 0 : 1;
        }

        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }

    Object returnValueFor(Class<?> type) {
        if (Primitives.isPrimitiveOrWrapper(type)) {
            return Primitives.defaultValue(type);
            // new instances are used instead of Collections.emptyList(), etc.
            // to avoid UnsupportedOperationException if code under test modifies returned
            // collection
        } else if (type == Iterable.class) {
            return new ArrayList<Object>(0);
        } else if (type == Collection.class) {
            return new LinkedList<Object>();
        } else if (type == Set.class) {
            return new HashSet<Object>();
        } else if (type == HashSet.class) {
            return new HashSet<Object>();
        } else if (type == SortedSet.class) {
            return new TreeSet<Object>();
        } else if (type == TreeSet.class) {
            return new TreeSet<Object>();
        } else if (type == LinkedHashSet.class) {
            return new LinkedHashSet<Object>();
        } else if (type == List.class) {
            return new LinkedList<Object>();
        } else if (type == LinkedList.class) {
            return new LinkedList<Object>();
        } else if (type == ArrayList.class) {
            return new ArrayList<Object>();
        } else if (type == Map.class) {
            return new HashMap<Object, Object>();
        } else if (type == HashMap.class) {
            return new HashMap<Object, Object>();
        } else if (type == SortedMap.class) {
            return new TreeMap<Object, Object>();
        } else if (type == TreeMap.class) {
            return new TreeMap<Object, Object>();
        } else if (type == LinkedHashMap.class) {
            return new LinkedHashMap<Object, Object>();
        } else if ("java.util.Optional".equals(type.getName())) {
            return JavaEightUtil.emptyOptional();
        } else if ("java.util.OptionalDouble".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalDouble();
        } else if ("java.util.OptionalInt".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalInt();
        } else if ("java.util.OptionalLong".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalLong();
        } else if ("java.util.stream.Stream".equals(type.getName())) {
            return JavaEightUtil.emptyStream();
        } else if ("java.util.stream.DoubleStream".equals(type.getName())) {
            return JavaEightUtil.emptyDoubleStream();
        } else if ("java.util.stream.IntStream".equals(type.getName())) {
            return JavaEightUtil.emptyIntStream();
        } else if ("java.util.stream.LongStream".equals(type.getName())) {
            return JavaEightUtil.emptyLongStream();
        } else if ("java.time.Duration".equals(type.getName())) {
            return JavaEightUtil.emptyDuration();
        } else if ("java.time.Period".equals(type.getName())) {
            return JavaEightUtil.emptyPeriod();
        }

        // Let's not care about the rest of collections.
        return null;
    }
}
