package org.mockito.rules;

import java.lang.annotation.Annotation;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.rules.annotations.ComponentUnderTest;
import org.mockito.rules.statement.ComponentUnderTestStatement;

/**
 * Allows easy stubbing of dependencies of tested components.
 * <p/>
 * Each tested component should declare its dependencies with package scope.
 * <p/>
 * Include this rule in you JUnit, annotate the tested component a
 * {@link ComponentUnderTest} and all dependencies annotated with with the
 * annotations specified in this rule constructor will be mocked. Example for a
 * Spring component which uses autowired dependencies:
 * 
 * <pre>
 * public class MyComponentTest {
 *     &#064;Rule
 *     public ComponentUnderTestRule rule = new ComponentUnderTestRule(Autowired.class);
 * 
 *     &#064;ComponentUnderTest
 *     private MyComponent component; // instantiation is not necessary
 * 
 *     &#064;Test
 *     public void shouldDoSomething() {
 *       // given
 *       given(component.dependency.foo()).willReturn(&quot;A&quot;);
 *    
 *       // when
 *       Result result = component.bar();
 *    
 *       // then
 *       assertThat(result).isEqualTo(...);
 *     }
 * }
 * 
 * </pre>
 * <p/>
 */
public class ComponentUnderTestRule implements MethodRule { // Please note that
                                                            // MethodRule is no
                                                            // longer deprecated
                                                            // in JUnit 4.11

    private final MockEngine mockEngine;
    private final ObjectCreationEngine objectCreationEngine;

    private final Class<? extends Annotation>[] annotationsToMock;

    public ComponentUnderTestRule(final Class<? extends Annotation>... annotationsToMock) {
        this(MockitoMockEngine.getInstance(), CGLibObjectCreationEngineImpl.getInstance(), annotationsToMock);
    }

    public ComponentUnderTestRule(final MockEngine mockEngine, final ObjectCreationEngine objectCreationEngine,
            final Class<? extends Annotation>... annotationsToMock) {
        this.mockEngine = mockEngine;
        this.annotationsToMock = annotationsToMock;
        this.objectCreationEngine = objectCreationEngine;
    }

    @Override
    public Statement apply(final Statement statement, final FrameworkMethod method, final Object testClass) {
        return new ComponentUnderTestStatement(statement, testClass, annotationsToMock, mockEngine,
                objectCreationEngine);
    }

}
