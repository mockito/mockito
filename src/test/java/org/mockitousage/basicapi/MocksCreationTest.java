/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class MocksCreationTest extends TestBase {

    private class HasPrivateConstructor {}
    
    @Test
    public void shouldCreateMockWhenConstructorIsPrivate() {
        assertNotNull(Mockito.mock(HasPrivateConstructor.class));
    }
    
    @Test
    public void shouldCombineMockNameAndSmartNulls() {
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
    public void shouldCombineMockNameAndExtraInterfaces() {
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
    public void shouldSpecifyMockNameViaSettings() {
        //given
        IMethods mock = mock(IMethods.class, withSettings().name("great mockie"));

        //when
        String name = mock.toString();
        
        //then
        assertThat(name).contains("great mockie");
    }
    
    @Test
    public void shouldScreamWhenSpyCreatedWithWrongType() {
        //given
        List list = new LinkedList();
        try {
            //when
            mock(List.class, withSettings().spiedInstance(list));
            fail();
            //then
        } catch (MockitoException e) {}
    }

    @Test
    public void shouldAllowCreatingSpiesWithCorrectType() {
        List list = new LinkedList();
        mock(LinkedList.class, withSettings().spiedInstance(list));
    }

    @Test
    public void shouldAllowInlineMockCreation() throws Exception {
        when(mock(Set.class).isEmpty()).thenReturn(false);
    }

}