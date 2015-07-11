package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

/**
 * Sets the annotation engine for the TestClass being run
 *
 * <ul>
 * <li>Allows a customized annotation engine to be used instead of the default engine i.e. {@link org.mockito.internal.configuration.InjectingAnnotationEngine}</li>
 * <li>Provides the possibilities to use different injection strategies for each test class instead of having a single strategy</li>
 * <li>Provides an alternative to the approach of providing a {@link org.mockito.configuration.MockitoConfiguration} implementation</li>
 * <li>Provides the possibility to use alternative/additional annotations for the fields being injected, such as marking a field to be injected with real instances rather than mocks</li>
 * </ul>
 *
 * <pre class="code"><code class="java">
 *   &#064;MockitoPlugin(annotationEngine = MyAnnotationEngine.class)
 *   public class ArticleManagerTest extends SampleBaseTestCase {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private  articleMonitor;
 *
 *       private ArticleManager manager;
 *   }
 *
 *   public class SampleBaseTestCase {
 *
 *       &#064;Before public void initMocks() {
 *           MockitoAnnotations.initMocks(this);
 *       }
 *   }
 *   
 *   public class MyAnnotationEngine implements AnnotationEngine {
 *       void process(Class<?> clazz, Object testInstance) {
 *           ... processing of logic to do the injection ...
 *       }
 *
 *   }
 * </code></pre>
 *
 * <p>
 * The annotation engine needs to extend the interface {@link org.mockito.configuration.AnnotationEngine} and implement the 
 * <strong><code>void process(Class<?> clazz, Object testInstance)</code></strong> method and a provide default constructor.
 * </p>
 *
 * <p>
 * <strong><code>MockitoAnnotations.initMocks(this)</code></strong> method has to be called to initialize annotated objects.
 * In above example, <code>initMocks()</code> is called in &#064;Before (JUnit4) method of test's base class.
 * For JUnit3 <code>initMocks()</code> can go to <code>setup()</code> method of a base class.
 * <strong>Instead</strong> you can also put initMocks() in your JUnit runner (&#064;RunWith) or use the built-in
 * {@link org.mockito.runners.MockitoJUnitRunner}.
 * </p>
 * 
 * <p>
 * Please note that the annotation engine will not work well in combination with using MockitoConfiguration, 
 * as the GlobalConfiguration will be replaced for each &#064;MockitoPlugin annotation found. 
 *
 * @see MockitoAnnotations#initMocks(Object)
 * @see org.mockito.runners.MockitoJUnitRunner
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MockitoPlugin {

    /**
     * 
     * @return the class, which should be used as the annotation engine for the test
     */
    public Class<? extends AnnotationEngine> annotationEngine() default InjectingAnnotationEngine.class;
}
