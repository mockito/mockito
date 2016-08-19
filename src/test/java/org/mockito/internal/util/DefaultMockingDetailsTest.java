package org.mockito.internal.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class DefaultMockingDetailsTest {

    @Mock private Foo foo;
    @Mock private Bar bar;
    @Mock private IMethods mock;
    @Spy private Gork gork;

    @Test
    public void should_check_that_a_mock_is_indeed_a_mock() throws Exception {
        assertEquals(true, mockingDetails(foo).isMock());
        assertEquals(true, mockingDetails(bar).isMock());
        assertEquals(true, mockingDetails(gork).isMock());
        assertEquals(false, mockingDetails("no a mock").isMock());
    }

    @Test
    public void should_check_that_a_spy_is_indeed_a_spy() throws Exception {
        assertEquals(true, mockingDetails(gork).isSpy());
        assertEquals(false, mockingDetails(foo).isSpy());
        assertEquals(false, mockingDetails(bar).isSpy());
        assertEquals(false, mockingDetails("not a spy").isSpy());
    }

    @Test
    public void should_check_that_a_spy_is_also_a_mock() throws Exception {
        assertEquals(true, mockingDetails(gork).isMock());
    }

    @Test
    public void should_get_mocked_type() throws Exception {
        assertEquals(Bar.class, mockingDetails(bar).getMockedType());
    }

    @Test(expected = NotAMockException.class)
    public void should_report_when_not_a_mockito_mock_on_getMockedType() throws Exception {
        mockingDetails("not a mock").getMockedType();
    }

    @Test
    public void should_get_extra_interfaces() throws Exception {
        Bar loup = mock(Bar.class, withSettings().extraInterfaces(List.class, Observer.class));
        assertEquals(setOf(Observer.class, List.class), mockingDetails(loup).getExtraInterfaces());
    }

    @Test(expected = NotAMockException.class)
    public void should_report_when_not_a_mockito_mock_on_getExtraInterfaces() throws Exception {
        mockingDetails("not a mock").getExtraInterfaces();
    }

    @Test
    public void provides_invocations() {
        //when
        mock.simpleMethod(10);
        mock.otherMethod();

        //then
        assertEquals(0, mockingDetails(foo).getInvocations().size());
        assertEquals("[mock.simpleMethod(10);, mock.otherMethod();]", mockingDetails(mock).getInvocations().toString());
    }

    @Test
    public void fails_when_getting_invocations_when_null() {
        try {
            //when
            mockingDetails(null).getInvocations();
            //then
            fail();
        } catch (NotAMockException e) {
            TestCase.assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void fails_when_getting_invocations_when_not_mock() {
        try {
            //when
            mockingDetails(new Object()).getInvocations();
            //then
            fail();
        } catch (NotAMockException e) {
            TestCase.assertEquals("Argument passed to Mockito.mockingDetails() should be a mock, but is an instance of class java.lang.Object!", e.getMessage());
        }
    }

    private <T> Set<T> setOf(T... items) {
        return new HashSet<T>(Arrays.asList(items));
    }

    public class Foo { }
    public interface Bar { }
    public static class Gork { }
}
