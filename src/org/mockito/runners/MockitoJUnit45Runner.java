package org.mockito.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;

/**
 * Mockito integration with JUnit 4.5. Supports new default JUnit's runner
 * {@link BlockJUnit4ClassRunner}.
 * <p>
 * See {@link MockitoJUnit4Runner} for details.
 * <p>
 * Example of use in test class:
 * 
 * <pre>
 * &#064;RunWith(MockitoJUnit45Runner.class)
 * public class ExampleTest {
 * 
 *     &#064;Mock
 *     private List list;
 * 
 *     &#064;Test
 *     public void shouldInitMocksUsingRunner() {
 *         list.add(&quot;test&quot;);
 *     }
 * }
 * </pre>
 */
public class MockitoJUnit45Runner extends BlockJUnit4ClassRunner {

    public MockitoJUnit45Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        MockitoAnnotations.initMocks(target);
        return super.withBefores(method, target, statement);
    }
}
