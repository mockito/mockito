package org.mockitousage.testng.failuretests;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Should fail.
 *
 * @see TestNGShouldFailWhenMockitoListenerFailsTest
 */
@Listeners(MockitoTestNGListener.class)
@Test(description = "Always failing, shouldn't be listed in 'mockito-testng.xml'")
public class FailingOnPurposeBecauseIncorrectAnnotationUsage {
    @Mock @Spy Map cant_mock_and_spy_at_the_same_time;
    @Test public void dummy_test_method() throws Exception { }
}
