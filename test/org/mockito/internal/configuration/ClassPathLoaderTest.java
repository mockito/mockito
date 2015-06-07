package org.mockito.internal.configuration;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ClassPathLoaderTest extends TestBase {

    @Test
    public void shouldReadConfigurationClassFromClassPath() {
        ConfigurationAccess.getConfig().overrideDefaultAnswer(new Answer<Object>() {
            public Object answer(final InvocationOnMock invocation) {
                return "foo";
            }});

        final IMethods mock = mock(IMethods.class);
        assertEquals("foo", mock.simpleMethod());
    }
}
