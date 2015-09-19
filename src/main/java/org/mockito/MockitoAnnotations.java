/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.ElementType.FIELD;

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
     * Use top-level {@link org.mockito.Mock} annotation instead
     * <p>
     * When &#064;Mock annotation was implemented as an inner class then users experienced problems with autocomplete features in IDEs. 
     * Hence &#064;Mock was made a top-level class.  
     * <p>
     * How to fix deprecation warnings? 
     * Typically, you can just <b>search:</b> import org.mockito.MockitoAnnotations.Mock; <b>and replace with:</b> import org.mockito.Mock;
     * <p>
     * If you're an existing user then sorry for making your code littered with deprecation warnings. 
     * This change was required to make Mockito better.
     */
    @Target( { FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @Deprecated
    public @interface Mock {}    
    
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
        Class<?> clazz = testClass.getClass();

        //below can be removed later, when we get read rid of deprecated stuff
        if (annotationEngine.getClass() != new DefaultMockitoConfiguration().getAnnotationEngine().getClass()) {
            //this means user has his own annotation engine and we have to respect that.
            //we will do annotation processing the old way so that we are backwards compatible
            while (clazz != Object.class) {
                scanDeprecatedWay(annotationEngine, testClass, clazz);
                clazz = clazz.getSuperclass();
            }
        }

        //anyway act 'the new' way
        annotationEngine.process(testClass.getClass(), testClass);
    }

    static void scanDeprecatedWay(AnnotationEngine annotationEngine, Object testClass, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            processAnnotationDeprecatedWay(annotationEngine, testClass, field);
        }
    }

    @SuppressWarnings("deprecation")
    static void processAnnotationDeprecatedWay(AnnotationEngine annotationEngine, Object testClass, Field field) {
        boolean alreadyAssigned = false;
        for(Annotation annotation : field.getAnnotations()) {
            Object mock = annotationEngine.createMockFor(annotation, field);
            if (mock != null) {
                throwIfAlreadyAssigned(field, alreadyAssigned);
                alreadyAssigned = true;                
                try {
                    new FieldSetter(testClass, field).set(mock);
                } catch (Exception e) {
                    throw new MockitoException("Problems setting field " + field.getName() + " annotated with "
                            + annotation, e);
                }
            }
        }
    }

    static void throwIfAlreadyAssigned(Field field, boolean alreadyAssigned) {
        if (alreadyAssigned) {
            new Reporter().moreThanOneAnnotationNotAllowed(field.getName());
        }
    }
}
