/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockitoutil.TestBase;

public class MockingDetailsTest extends TestBase {
    
    static class TestClass {}

    @Mock TestClass annotatedMock;
    @Spy TestClass annotatedSpy;

    @Test
    public void should_know_spy(){
        assertTrue(mockingDetails(annotatedSpy).isMock());
        assertTrue(mockingDetails(spy( new TestClass())).isMock());
        assertTrue(mockingDetails(spy(TestClass.class)).isMock());
        assertTrue(mockingDetails(mock(TestClass.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS))).isMock());

        assertTrue(mockingDetails(annotatedSpy).isSpy());
        assertTrue(mockingDetails(spy( new TestClass())).isSpy());
        assertTrue(mockingDetails(spy(TestClass.class)).isSpy());
        assertTrue(mockingDetails(mock(TestClass.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS))).isSpy());
    }

    @Test
    public void should_know_mock(){
        assertTrue(mockingDetails(annotatedMock).isMock());
        assertTrue(mockingDetails(mock(TestClass.class)).isMock());

        assertFalse(mockingDetails(annotatedMock).isSpy());
        assertFalse(mockingDetails(mock(TestClass.class)).isSpy());
    }

    @Test
    public void should_handle_non_mocks() {
        assertFalse(mockingDetails("non mock").isSpy());
        assertFalse(mockingDetails("non mock").isMock());

        assertFalse(mockingDetails(null).isSpy());
        assertFalse(mockingDetails(null).isMock());
    }
}
