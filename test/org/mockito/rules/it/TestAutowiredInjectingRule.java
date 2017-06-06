package org.mockito.rules.it;

import org.mockito.rules.ComponentUnderTestRule;

public class TestAutowiredInjectingRule extends ComponentUnderTestRule {

    @SuppressWarnings("unchecked")
    public TestAutowiredInjectingRule() {
        super(TestAutowired.class);
    }
}
