package org.mockito.runners;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Uses new default JUnit's runner {@link BlockJUnit4ClassRunner}.
 * <p>
 * JUnit 4.5 runner initializes mocks annotated with {@link Mock},
 * so that explicit usage of {@link MockitoAnnotations#initMocks(Object)} is not necessary. 
 * Mocks are initialized before each test method. 
 * Runner is completely optional - there are other ways you can get &#064;Mock working, for example by writing a base class.
 * Read more in javadoc for {@link MockitoAnnotations#initMocks(Object)}
 * <p>
 * Example:
 * <pre>
 * &#064;RunWith(MockitoJUnit44Runner.class)
 * public class ExampleTest {
 * 
 *     &#064;Mock
 *     private List list;
 * 
 *     &#064;Test
 *     public void shouldDoSomething() {
 *         list.add(100);
 *     }
 * }
 * <p>
 * 
 * </pre>
 */
public class MockitoJUnitRunner extends BlockJUnit4ClassRunner {

    public MockitoJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        MockitoAnnotations.initMocks(target);
        return super.withBefores(method, target, statement);
    }
}
