package org.mockitousage.testng.failuretests;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Matchers.anyString;

/**
 * Should fail.
 *
 * @see TestNGShouldFailWhenMockitoListenerFailsTest
 */
@Listeners(MockitoTestNGListener.class)
@Test(description = "Always failing, shouldn't be listed in 'mockito-testng.xml'")
public class FailingOnPurposeBecauseWrongStubbingSyntaxInConfigurationMethod {

    @Mock List list;

    // should fail
    @BeforeMethod public void some_wrong_stubs() {
        anyString();
    }


    @Test
    public void here_to_execute_the_config_method() throws Exception {
    }

}
