/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * MockitoAnnotations.initMocks(this); initializes fields annotated with Mockito annotations.
 * <p>  
 * <ul>
 * <li>Allows shorthand creation of objects required for testing.</li> 
 * <li>Minimizes repetitive mock creation code.</li> 
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because <b>field name</b> is used to identify the mock.</li>
 * </ul>
 * 
 * <pre class="code"><code class="java">
 *   public class ArticleManagerTest extends SampleBaseTestCase { 
 *     
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Mock private UserProvider userProvider;
 *     
 *       private ArticleManager manager;
 *     
 *       &#064;Before public void setup() {
 *           manager = new ArticleManager(userProvider, database, calculator);
 *       }
 *   }
 *   
 *   public class SampleBaseTestCase {
 *   
 *       &#064;Before public void initMocks() {
 *           MockitoAnnotations.initMocks(this);
 *       }
 *   }
 * </code></pre>
 * <p>
 * Read also about other annotations &#064;{@link Spy}, &#064;{@link Captor}, &#064;{@link InjectMocks}
 * <p>
 * <b><code>MockitoAnnotations.initMocks(this)</code></b> method has to called to initialize annotated fields.
 * <p>
 * In above example, <code>initMocks()</code> is called in &#064;Before (JUnit4) method of test's base class. 
 * For JUnit3 <code>initMocks()</code> can go to <code>setup()</code> method of a base class.
 * You can also put initMocks() in your JUnit runner (&#064;RunWith) or use built-in runner: {@link MockitoJUnitRunner}
 */
public class MockitoAnnotations {

    /**
     * Initializes objects annotated with Mockito annotations for given testClass:
     *  &#064;{@link org.mockito.Mock}, &#064;{@link Spy}, &#064;{@link Captor}, &#064;{@link InjectMocks} 
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     */
    public static void initMocks(Object testClass) {
        if (testClass == null) {
            throw new MockitoException("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class");
        }

        AnnotationEngine annotationEngine = new GlobalConfiguration().getAnnotationEngine();
        annotationEngine.process(testClass.getClass(), testClass);
    }
}
