/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("unchecked")
public class MocksCreationTest extends TestBase {

    private class HasPrivateConstructor {}

    @Test
    public void should_create_mock_when_constructor_is_private() {
        assertNotNull(Mockito.mock(HasPrivateConstructor.class));
    }

    @Test
    public void should_combine_mock_name_and_smart_nulls() {
        //given
        IMethods mock = mock(IMethods.class, withSettings()
            .defaultAnswer(RETURNS_SMART_NULLS)
            .name("great mockie"));

        //when
        IMethods smartNull = mock.iMethodsReturningMethod();
        String name = mock.toString();

        //then
        assertThat(name).contains("great mockie");
        //and
        try {
            smartNull.simpleMethod();
            fail();
        } catch(SmartNullPointerException e) {}
    }

    @Test
    public void should_combine_mock_name_and_extra_interfaces() {
        //given
        IMethods mock = mock(IMethods.class, withSettings()
                .extraInterfaces(List.class)
                .name("great mockie"));

        //when
        String name = mock.toString();

        //then
        assertThat(name).contains("great mockie");
        //and
        assertTrue(mock instanceof List);
    }

    @Test
    public void should_specify_mock_name_via_settings() {
        //given
        IMethods mock = mock(IMethods.class, withSettings().name("great mockie"));

        //when
        String name = mock.toString();

        //then
        assertThat(name).contains("great mockie");
    }

    @Test
    public void should_scream_when_spy_created_with_wrong_type() {
        //given
        List list = new LinkedList();
        try {
            //when
            mock(List.class, withSettings().spiedInstance(list));
            fail();
            //then
        } catch (MockitoException e) {}
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void should_allow_creating_spies_with_correct_type() {
        List list = new LinkedList();
        mock(LinkedList.class, withSettings().spiedInstance(list));
    }

    @Test
    public void should_allow_inline_mock_creation() {
        when(mock(Set.class).isEmpty()).thenReturn(false);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface SomeAnnotation {}

    @SomeAnnotation static class Foo {}

    @Test
    public void should_strip_annotations() {
        Foo withAnnotations = mock(Foo.class);
        Foo withoutAnnotations = mock(Foo.class, withSettings().withoutAnnotations());

        //expect:
        assertTrue(withAnnotations.getClass().isAnnotationPresent(SomeAnnotation.class));
        assertFalse(withoutAnnotations.getClass().isAnnotationPresent(SomeAnnotation.class));
    }
}
