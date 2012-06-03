/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MockingDetailsTest {
    
    private static class TestClass{
    }

    @Mock
    TestClass mock1;
    @Spy
    TestClass spy1;
    TestClass mock2;
    TestClass spy2;
    TestClass nonMock;
    
    @Before
    public void setUp(){
        initMocks( this );
        mock2 = mock( TestClass.class );
        spy2 = spy( new TestClass());
        nonMock = new TestClass();
    }
    
    @Test
    public void shouldReturnTrue_FromIsMock_ForAnnotatedMock(){
        assertTrue(mockingDetails(mock1).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForDirectMock(){
        assertTrue(mockingDetails(mock2).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForAnnotatedSpy(){
        assertTrue(mockingDetails(spy1).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForDirectSpy(){

        assertTrue(mockingDetails(spy2).isMock());
    }

    @Test
    public void shouldReturnFalse_FromIsMock_ForNonMock(){
        assertFalse(mockingDetails(nonMock).isMock());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForAnnotatedMock(){
        assertFalse(mockingDetails(mock1).isSpy());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForDirectMock(){
        assertFalse(mockingDetails(mock2).isSpy());
    }


    @Test
    public void shouldReturnTrue_FromIsSpy_ForAnnotatedSpy(){
        assertTrue(mockingDetails(spy1).isSpy());
    }

    @Test
    public void shouldReturnTrue_FromIsSpy_ForDirectSpy(){
        assertTrue(mockingDetails(spy2).isSpy());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForNonMock(){
        assertFalse(mockingDetails(nonMock).isSpy());
    }
}
