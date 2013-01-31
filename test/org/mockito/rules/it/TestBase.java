package org.mockito.rules.it;

import org.junit.Rule;
import org.mockito.rules.ComponentUnderTestRule;

public abstract class TestBase {

    @Rule
    public final ComponentUnderTestRule rule = new TestAutowiredInjectingRule();

}
