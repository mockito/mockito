/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("rawtypes")
public class MocksCreationTest extends TestBase {

    private class HasPrivateConstructor {};
    
    @Test
    public void shouldCreateMockWhenConstructorIsPrivate() {
        assertNotNull(Mockito.mock(HasPrivateConstructor.class));
    }
    
    @Test
    public void shouldCombineMockNameAndSmartNulls() {
        //given
        final IMethods mock = mock(IMethods.class, withSettings()
            .defaultAnswer(RETURNS_SMART_NULLS)
            .name("great mockie"));    
        
        //when
        final IMethods smartNull = mock.iMethodsReturningMethod();
        final String name = mock.toString();
        
        //then
        assertContains("great mockie", name);
        //and
        try {
            smartNull.simpleMethod();
            fail();
        } catch(final SmartNullPointerException e) {}
    }
    
    @Test
    public void shouldCombineMockNameAndExtraInterfaces() {
        //given
        final IMethods mock = mock(IMethods.class, withSettings()
                .extraInterfaces(List.class)
                .name("great mockie"));
        
        //when
        final String name = mock.toString();
        
        //then
        assertContains("great mockie", name);
        //and
        assertThat(mock, is(List.class));
    }
    
    @Test
    public void shouldSpecifyMockNameViaSettings() {
        //given
        final IMethods mock = mock(IMethods.class, withSettings().name("great mockie"));

        //when
        final String name = mock.toString();
        
        //then
        assertContains("great mockie", name);
    }
    
    @Test
    public void shouldScreamWhenSpyCreatedWithWrongType() {
        //given
        final List list = new LinkedList();
        try {
            //when
            mock(List.class, withSettings().spiedInstance(list));
            fail();
            //then
        } catch (final MockitoException e) {}
    }

    @Test
    public void shouldAllowCreatingSpiesWithCorrectType() {
        final List list = new LinkedList();
        mock(LinkedList.class, withSettings().spiedInstance(list));
    }

    @Test
    public void shouldAllowInlineMockCreation() throws Exception {
        when(mock(Set.class).isEmpty()).thenReturn(false);
    }

}