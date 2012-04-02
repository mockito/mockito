package org.mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import org.junit.Before;
import org.junit.Test;

public class MockingDetailsTest {
    
    private static class TestClass{
    }

    @Mock TestClass mock1;
    @Spy TestClass spy1;
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
        assertTrue(MockingDetails.of(mock1).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForDirectMock(){
        assertTrue(MockingDetails.of(mock2).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForAnnotatedSpy(){
        assertTrue(MockingDetails.of(spy1).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForDirectSpy(){

        assertTrue(MockingDetails.of(spy2).isMock());
    }

    @Test
    public void shouldReturnFalse_FromIsMock_ForNonMock(){
        assertFalse(MockingDetails.of(nonMock).isMock());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForAnnotatedMock(){
        assertFalse(MockingDetails.of(mock1).isSpy());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForDirectMock(){
        assertFalse(MockingDetails.of(mock2).isSpy());
    }


    @Test
    public void shouldReturnTrue_FromIsSpy_ForAnnotatedSpy(){
        assertTrue(MockingDetails.of(spy1).isSpy());
    }

    @Test
    public void shouldReturnTrue_FromIsSpy_ForDirectSpy(){
        assertTrue(MockingDetails.of(spy2).isSpy());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForNonMock(){
        assertFalse(MockingDetails.of(nonMock).isSpy());
    }
}
