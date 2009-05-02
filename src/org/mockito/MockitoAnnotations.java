/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * <ul>
 * <li>Allows shorthand mock creation.</li> 
 * <li>Minimizes repetitive mock creation code.</li> 
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because <b>field name</b> is used to identify the mock.</li>
 * </ul>
 * 
 * <pre>
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
 * </pre>
 * 
 * <b><code>MockitoAnnotations.initMocks(this)</code></b> method has to called to initialize annotated mocks.
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
     * Initializes objects annotated with &#064;Mock for given testClass.
     * <p>
     * See examples in javadoc for {@link MockitoAnnotations} class.
     */
    public static void initMocks(Object testClass) {
        if (testClass == null) {
            throw new MockitoException("testClass cannot be null. For info how to use @Mock annotations see examples in javadoc for MockitoAnnotations class");
        }
        
        Class<?> clazz = testClass.getClass();
        while (clazz != Object.class) {
            scan(testClass, clazz);
            clazz = clazz.getSuperclass();
        }
    }

    private static void scan(Object testClass, Class<?> clazz) {
        AnnotationEngine annotationEngine = new GlobalConfiguration().getAnnotationEngine();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            for(Annotation annotation : field.getAnnotations()) {
                Object mock = annotationEngine.createMockFor(annotation, field);
                if (mock != null) {
                    boolean wasAccessible = field.isAccessible();
                    field.setAccessible(true);
                    try {
                        field.set(testClass, mock);
                    } catch (IllegalAccessException e) {
                        throw new MockitoException("Problems initiating mocks annotated with " + annotation, e);
                    } finally {
                        field.setAccessible(wasAccessible);
                    }    
                }
            }
        }
    }
}