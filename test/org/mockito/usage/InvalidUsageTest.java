package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.mockito.exceptions.*;
import org.mockito.internal.StateResetter;

/**
 * invalid state happens if:
 *    -unfinished stubbing
 *    -unfinished stubVoid
 *    -stubbing without actual method call
 *    -verify without actual method call
 *    
 * we should aim to detect invalid state in following scenarios:
 *    -on method call on mock
 *    -on verify
 *    -on verifyZeroInteractions
 *    -on verifyNoMoreInteractions
 *    -on stub
 *    -on stubVoid
 *    
 * obviously we should consider if it is really important to cover all those naughty usage
 */
@SuppressWarnings("unchecked")
public class InvalidUsageTest {
    
    private List mock;

    @Before
    @After
    public void resetState() {
        StateResetter.reset();
        mock = mock(List.class);
    }
    
    @Test
    public void shouldDetectStubbingWithoutMethodCallOnMock() {
        try {
            stub("blah".contains("blah"));
            fail();
        } catch (MissingMethodInvocationException e) {
            //cool
        }
    }
    
    @Ignore
    @Test
    public void unfinishedStubbingDetectedOnVerify() {
        stub(mock.add("test"));
        
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
    
    @Ignore
    @Test
    public void unfinishedStubbingDetectedWhenAnotherStubbingIsStarted() {
        stub(mock.add("test"));
        
        try {
            stub(mock.add("test")).andThrows(new Exception("ssdf"));
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
    
    @Ignore
    @Test
    public void unfinishedStubbingDetectedMockCalled() {
        stub(mock.add("test"));
        
        try {
            mock.clear();
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
    
    @Ignore
    @Test
    public void unfinishedStubbingVoid() {
        stubVoid(mock);
        
        try {
            mock.clear();
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
}
