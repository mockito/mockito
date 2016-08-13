package org.mockito.internal.framework;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class DefaultMockitoFrameworkTest extends TestBase {

    DefaultMockitoFramework framework = new DefaultMockitoFramework();

    @Test(expected = IllegalArgumentException.class)
    public void prevents_adding_null_listener() {
        framework.addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void prevents_removing_null_listener() {
        framework.addListener(null);
    }
}