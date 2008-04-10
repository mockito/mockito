/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.mockito.exceptions.base.MockitoException;

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
 * You can also put it in your JUnit4 runner (&#064;RunWith).
 * For JUnit3 <code>initMocks()</code> can go to <code>setup()</code> method of a base class.
 * <p>
 * How to implement mockito junit runner? See examples from mockito/test/org/mockitousage/examples/junitrunner subpackage. 
 * You may want to check out the project from svn repository to easily browse Mockito's test code.
 */
public class MockitoAnnotations {

    /**
     * Allows shorthand mock creation, see examples in javadoc for {@link MockitoAnnotations} class.
     */
    @Target( { FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Mock {}
    
    /**
     * Initializes objects annotated with &#064;Mock for given testClass.
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
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(Mock.class)) {
                boolean wasAccessible = f.isAccessible();
                f.setAccessible(true);
                try {
                    f.set(testClass, Mockito.mock(f.getType(), f.getName()));
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating mocks annotated with @Mock", e);
                } finally {
                    f.setAccessible(wasAccessible);
                }
            }
        }
    }
}