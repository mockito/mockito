/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class JUnitFailureHackerTest extends TestBase {

    JUnitFailureHacker hacker = new JUnitFailureHacker();

    @Test
    public void shouldReplaceException() throws Exception {
        //given
        RuntimeException actualExc = new RuntimeException("foo");
        Failure failure = new Failure(Description.EMPTY, actualExc);

        //when
        hacker.appendWarnings(failure, "unused stubbing");

        //then
        assertEquals(ExceptionIncludingMockitoWarnings.class, failure.getException().getClass());
        assertEquals(actualExc, failure.getException().getCause());
        Assertions.assertThat(actualExc.getStackTrace()).isEqualTo(failure.getException().getStackTrace());
    }

    @Test
    public void shouldAppendWarning() throws Exception {
        Failure failure = new Failure(Description.EMPTY, new RuntimeException("foo"));

        //when
        hacker.appendWarnings(failure, "unused stubbing blah");

        //then
        assertThat(failure.getException()).hasMessageContaining("unused stubbing blah");
    }

    @Test
    public void shouldNotAppendWhenNoWarnings() throws Exception {
        RuntimeException ex = new RuntimeException("foo");
        Failure failure = new Failure(Description.EMPTY, ex);

        //when
        hacker.appendWarnings(failure, "");

        //then
        assertEquals(ex, failure.getException());
    }

    @Test
    public void shouldNotAppendWhenNullWarnings() throws Exception {
        RuntimeException ex = new RuntimeException("foo");
        Failure failure = new Failure(Description.EMPTY, ex);

        //when
        hacker.appendWarnings(failure, null);

        //then
        assertEquals(ex, failure.getException());
    }

    @Test
    public void shouldPrintTheWarningSoICanSeeIt() throws Exception {
        Failure failure = new Failure(Description.EMPTY, new RuntimeException("foo"));

        //when
        hacker.appendWarnings(failure, "unused stubbing blah");

        //then
        System.out.println(failure.getException());
    }
}
