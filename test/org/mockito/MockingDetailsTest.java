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
    
    @Before
    public void setUp(){
        initMocks( this );
    }
    
    @Test
    public void shouldReturnTrue_FromIsMock_ForAnnotatedMock(){
        assertTrue(MockingDetails.of(mock1).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsMock_ForDirectMock(){
        TestClass mock2 = mock( TestClass.class );
        assertTrue(MockingDetails.of(mock2).isMock());
    }

    @Test
    public void shouldReturnFalse_FromIsMock_ForNonMock(){
        TestClass nonMock = new TestClass();
        assertFalse(MockingDetails.of(nonMock).isMock());
    }

    @Test
    public void shouldReturnTrue_FromIsSpy_ForAnnotatedSpy(){
        assertTrue(MockingDetails.of(spy1).isSpy());
    }

    @Test
    public void shouldReturnTrue_FromIsSpy_ForDirectSpy(){
        TestClass spy2 = spy( new TestClass());
        assertTrue(MockingDetails.of(spy2).isSpy());
    }

    @Test
    public void shouldReturnFalse_FromIsSpy_ForNonSpy(){
        TestClass nonSpy = new TestClass();
        assertFalse(MockingDetails.of(nonSpy).isSpy());
    }
}
