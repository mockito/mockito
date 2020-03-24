/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.*;

import org.junit.Assume;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

public class ReturnsEmptyValuesTest extends TestBase {

    private final ReturnsEmptyValues values = new ReturnsEmptyValues();

    @Test
    public void should_return_empty_collections_or_null_for_non_collections() {
        assertTrue(((Collection<?>) values.returnValueFor(Collection.class)).isEmpty());

        assertTrue(((Set<?>) values.returnValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet<?>) values.returnValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet<?>) values.returnValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet<?>) values.returnValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet<?>) values.returnValueFor(LinkedHashSet.class)).isEmpty());

        assertTrue(((List<?>) values.returnValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList<?>) values.returnValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList<?>) values.returnValueFor(LinkedList.class)).isEmpty());

        assertTrue(((Map<?, ?>) values.returnValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap<?, ?>) values.returnValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap<?, ?>) values.returnValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap<?, ?>) values.returnValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap<?, ?>) values.returnValueFor(LinkedHashMap.class)).isEmpty());

        assertNull(values.returnValueFor(String.class));
    }

    @Test
    public void should_return_empty_iterable() throws Exception {
        assertFalse(((Iterable<?>) values.returnValueFor(Iterable.class)).iterator().hasNext());
    }

    @Test
    public void should_return_primitive() {
        assertEquals(false, values.returnValueFor(Boolean.TYPE));
        assertEquals((char) 0, values.returnValueFor(Character.TYPE));
        assertEquals((byte) 0, values.returnValueFor(Byte.TYPE));
        assertEquals((short) 0, values.returnValueFor(Short.TYPE));
        assertEquals(0, values.returnValueFor(Integer.TYPE));
        assertEquals(0L, values.returnValueFor(Long.TYPE));
        assertEquals(0F, values.returnValueFor(Float.TYPE));
        assertEquals(0D, values.returnValueFor(Double.TYPE));
    }

    @Test
    public void should_return_non_zero_for_compareTo_method() {
        //
        // given
        Date d = mock(Date.class);
        d.compareTo(new Date());
        Invocation compareTo = this.getLastInvocation();

        //when
        Object result = values.answer(compareTo);

        //then
        assertTrue(result != (Object) 0);
    }

    @SuppressWarnings("SelfComparison")
    @Test
    public void should_return_zero_if_mock_is_compared_to_itself() {
        //given
        Date d = mock(Date.class);
        d.compareTo(d);
        Invocation compareTo = this.getLastInvocation();

        //when
        Object result = values.answer(compareTo);

        //then
        assertEquals(0, result);
    }

    @Test
    public void should_return_empty_Optional() throws Exception {
        verify_empty_Optional_is_returned("java.util.stream.Stream", "java.util.Optional");
    }

    @Test
    public void should_return_empty_OptionalDouble() throws Exception {
        verify_empty_Optional_is_returned("java.util.stream.DoubleStream", "java.util.OptionalDouble");
    }

    @Test
    public void should_return_empty_OptionalInt() throws Exception {
        verify_empty_Optional_is_returned("java.util.stream.IntStream", "java.util.OptionalInt");
    }

    @Test
    public void should_return_empty_OptionalLong() throws Exception {
        verify_empty_Optional_is_returned("java.util.stream.LongStream", "java.util.OptionalLong");
    }

    private void verify_empty_Optional_is_returned(String streamFqcn, String optionalFqcn) throws Exception {
        Class<?> streamType = getClassOrSkipTest(streamFqcn);

        //given
        Object stream = mock(streamType);
        Object optional = streamType.getMethod("findAny").invoke(stream);
        assertNotNull(optional);
        assertFalse((Boolean) Class.forName(optionalFqcn).getMethod("isPresent").invoke(optional));

        Invocation findAny = this.getLastInvocation();

        //when
        Object result = values.answer(findAny);

        //then
        assertEquals(optional, result);
    }

    @Test
    public void should_return_empty_Stream() throws Exception {
        verify_empty_Stream_is_returned("java.util.stream.Stream");
    }

    @Test
    public void should_return_empty_DoubleStream() throws Exception {
        verify_empty_Stream_is_returned("java.util.stream.DoubleStream");
    }

    @Test
    public void should_return_empty_IntStream() throws Exception {
        verify_empty_Stream_is_returned("java.util.stream.IntStream");
    }

    @Test
    public void should_return_empty_LongStream() throws Exception {
        verify_empty_Stream_is_returned("java.util.stream.LongStream");
    }

    private void verify_empty_Stream_is_returned(String streamFqcn) throws Exception {
        // given
        Class<?> streamType = getClassOrSkipTest(streamFqcn);

        // when
        Object stream = values.returnValueFor(streamType);
        long count = (Long) streamType.getMethod("count").invoke(stream);

        // then
        assertEquals("count of empty " + streamFqcn, 0L, count);
    }

    @Test
    public void should_return_empty_duration() throws Exception {
        //given
        final String fqcn = "java.time.Duration";
        final Class<?> durationClass = getClassOrSkipTest(fqcn);

        // when
        final Object duration = values.returnValueFor(durationClass);
        final int nano = (Integer) durationClass.getMethod("getNano").invoke(duration);
        final long seconds = (Long) durationClass.getMethod("getSeconds").invoke(duration);

        // then
        assertEquals("nano of empty " + fqcn, 0, nano);
        assertEquals("seconds of empty " + fqcn, 0L, seconds);
    }

    @Test
    public void should_return_empty_period() throws Exception {
        //given
        final String fqcn = "java.time.Period";
        final Class<?> periodClass = getClassOrSkipTest(fqcn);

        // when
        final Object period = values.returnValueFor(periodClass);
        final int years = (Integer) periodClass.getMethod("getYears").invoke(period);
        final int months = (Integer) periodClass.getMethod("getMonths").invoke(period);
        final int days = (Integer) periodClass.getMethod("getDays").invoke(period);

        // then
        assertEquals("years of empty " + fqcn, 0, years);
        assertEquals("months of empty " + fqcn, 0, months);
        assertEquals("days of empty " + fqcn, 0, days);
    }

    @Test
    public void should_return_empty_instant() throws Exception {
        //given
        final String fqcn = "java.time.Instant";
        final Class<?> instantClass = getClassOrSkipTest(fqcn);

        // when
        final Object instant = values.returnValueFor(instantClass);
        final int nano = (Integer) instantClass.getMethod("getNano").invoke(instant);
        final long epochSecond = (Long) instantClass.getMethod("getEpochSecond").invoke(instant);

        // then
        assertEquals("nano of empty " + fqcn, 0, nano);
        assertEquals("epoch second of empty " + fqcn, 0L, epochSecond);
    }

    @Test
    public void should_return_empty_local_date() throws Exception {
        //given
        final String fqcn = "java.time.LocalDate";
        final Class<?> localDateClass = getClassOrSkipTest(fqcn);

        // when
        final Object localDate = values.returnValueFor(localDateClass);
        final int year = (Integer) localDateClass.getMethod("getYear").invoke(localDate);
        final int month = (Integer) localDateClass.getMethod("getMonthValue").invoke(localDate);
        final int dayOfMonth = (Integer) localDateClass.getMethod("getDayOfMonth").invoke(localDate);

        // then
        verifyYearMonthDayIsEpoch(fqcn, year, month, dayOfMonth);
    }

    @Test
    public void should_return_empty_local_date_time() throws Exception {
        //given
        final String fqcn = "java.time.LocalDateTime";
        final Class<?> localDateTimeClass = getClassOrSkipTest(fqcn);

        // when
        final Object localDateTime = values.returnValueFor(localDateTimeClass);
        final int year = (Integer) localDateTimeClass.getMethod("getYear").invoke(localDateTime);
        final int month = (Integer) localDateTimeClass.getMethod("getMonthValue").invoke(localDateTime);
        final int dayOfMonth = (Integer) localDateTimeClass.getMethod("getDayOfMonth").invoke(localDateTime);
        final int hour = (Integer) localDateTimeClass.getMethod("getHour").invoke(localDateTime);
        final int minute = (Integer) localDateTimeClass.getMethod("getMinute").invoke(localDateTime);
        final int second = (Integer) localDateTimeClass.getMethod("getSecond").invoke(localDateTime);
        final int nano = (Integer) localDateTimeClass.getMethod("getNano").invoke(localDateTime);

        // then
        verifyYearMonthDayIsEpoch(fqcn, year, month, dayOfMonth);
        verifyHourMinuteSecondNanoIsZero(fqcn, hour, minute, second, nano);
    }

    @Test
    public void should_return_empty_local_time() throws Exception {
        //given
        final String fqcn = "java.time.LocalTime";
        final Class<?> localTimeClass = getClassOrSkipTest(fqcn);

        // when
        final Object localTime = values.returnValueFor(localTimeClass);
        final int hour = (Integer) localTimeClass.getMethod("getHour").invoke(localTime);
        final int minute = (Integer) localTimeClass.getMethod("getMinute").invoke(localTime);
        final int second = (Integer) localTimeClass.getMethod("getSecond").invoke(localTime);
        final int nano = (Integer) localTimeClass.getMethod("getNano").invoke(localTime);

        // then
        verifyHourMinuteSecondNanoIsZero(fqcn, hour, minute, second, nano);
    }

    @Test
    public void should_return_empty_offset_date_time() throws Exception {
        //given
        final String fqcn = "java.time.OffsetDateTime";
        final Class<?> offsetDateTimeClass = getClassOrSkipTest(fqcn);

        // when
        final Object offsetDateTime = values.returnValueFor(offsetDateTimeClass);
        final int year = (Integer) offsetDateTimeClass.getMethod("getYear").invoke(offsetDateTime);
        final int month = (Integer) offsetDateTimeClass.getMethod("getMonthValue").invoke(offsetDateTime);
        final int dayOfMonth = (Integer) offsetDateTimeClass.getMethod("getDayOfMonth").invoke(offsetDateTime);
        final int hour = (Integer) offsetDateTimeClass.getMethod("getHour").invoke(offsetDateTime);
        final int minute = (Integer) offsetDateTimeClass.getMethod("getMinute").invoke(offsetDateTime);
        final int second = (Integer) offsetDateTimeClass.getMethod("getSecond").invoke(offsetDateTime);
        final int nano = (Integer) offsetDateTimeClass.getMethod("getNano").invoke(offsetDateTime);
        final Object zoneOffset = offsetDateTimeClass.getMethod("getOffset").invoke(offsetDateTime);
        final String zoneOffsetIdString = (String) zoneOffset.getClass().getMethod("getId").invoke(zoneOffset);

        // then
        verifyYearMonthDayIsEpoch(fqcn, year, month, dayOfMonth);
        verifyHourMinuteSecondNanoIsZero(fqcn, hour, minute, second, nano);
        assertEquals("zone id of empty " + fqcn, "Z", zoneOffsetIdString);
    }

    @Test
    public void should_return_empty_offset_time() throws Exception {
        //given
        final String fqcn = "java.time.OffsetTime";
        final Class<?> offsetTimeClass = getClassOrSkipTest(fqcn);

        // when
        final Object offsetTime = values.returnValueFor(offsetTimeClass);
        final int hour = (Integer) offsetTimeClass.getMethod("getHour").invoke(offsetTime);
        final int minute = (Integer) offsetTimeClass.getMethod("getMinute").invoke(offsetTime);
        final int second = (Integer) offsetTimeClass.getMethod("getSecond").invoke(offsetTime);
        final int nano = (Integer) offsetTimeClass.getMethod("getNano").invoke(offsetTime);
        final Object zoneOffset = offsetTimeClass.getMethod("getOffset").invoke(offsetTime);
        final String zoneOffsetIdString = (String) zoneOffset.getClass().getMethod("getId").invoke(zoneOffset);

        // then
        verifyHourMinuteSecondNanoIsZero(fqcn, hour, minute, second, nano);
        assertEquals("zone id of empty " + fqcn, "Z", zoneOffsetIdString);
    }

    @Test
    public void should_return_empty_zoned_date_time() throws Exception {
        //given
        final String fqcn = "java.time.ZonedDateTime";
        final Class<?> zonedDateTimeClass = getClassOrSkipTest(fqcn);

        // when
        final Object zonedDateTime = values.returnValueFor(zonedDateTimeClass);
        final int year = (Integer) zonedDateTimeClass.getMethod("getYear").invoke(zonedDateTime);
        final int month = (Integer) zonedDateTimeClass.getMethod("getMonthValue").invoke(zonedDateTime);
        final int dayOfMonth = (Integer) zonedDateTimeClass.getMethod("getDayOfMonth").invoke(zonedDateTime);
        final int hour = (Integer) zonedDateTimeClass.getMethod("getHour").invoke(zonedDateTime);
        final int minute = (Integer) zonedDateTimeClass.getMethod("getMinute").invoke(zonedDateTime);
        final int second = (Integer) zonedDateTimeClass.getMethod("getSecond").invoke(zonedDateTime);
        final int nano = (Integer) zonedDateTimeClass.getMethod("getNano").invoke(zonedDateTime);
        final Object zoneOffset = zonedDateTimeClass.getMethod("getOffset").invoke(zonedDateTime);
        final String zoneOffsetIdString = (String) zoneOffset.getClass().getMethod("getId").invoke(zoneOffset);

        // then
        verifyYearMonthDayIsEpoch(fqcn, year, month, dayOfMonth);
        verifyHourMinuteSecondNanoIsZero(fqcn, hour, minute, second, nano);
        assertEquals("zone id of empty " + fqcn, "Z", zoneOffsetIdString);
    }

    private void verifyYearMonthDayIsEpoch(final String fqcn, final int year, final int month, final int day) {
        assertEquals("year of empty " + fqcn, 1970, year);
        assertEquals("month of empty " + fqcn, 1, month);
        assertEquals("year of empty " + fqcn, 1, day);
    }

    private void verifyHourMinuteSecondNanoIsZero(String fqcn, int hour, int minute, int second, int nano) {
        assertEquals("hour of empty " + fqcn, 0, hour);
        assertEquals("minute of empty " + fqcn, 0, minute);
        assertEquals("second of empty " + fqcn, 0, second);
        assertEquals("nano of empty " + fqcn, 0, nano);
    }

    /**
     * Tries to load the given class. If the class is not found, the complete test is skipped.
     */
    private Class<?> getClassOrSkipTest(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Assume.assumeNoException("JVM does not support " + className, e);
            return null;
        }
    }

}
