package org.mockito.cglibintegration;

import static org.junit.Assert.fail;
import java.util.ArrayList;
import org.junit.Test;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.NoOp;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void should_scream_when_enhanced_but_not_a_mock_passed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            mockUtil.getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }
}
