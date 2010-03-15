/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mockito.runners.MockitoJUnitRunner;

/**
 * <ul>
 * <li>Allows shorthand mock creation.</li>
 * <li>Minimizes repetitive mock creation code.</li>
 * <li>Makes the test class more readable.</li>
 * <li>Makes the verification error easier to read because the <b>field name</b> is used to identify the mock.</li>
 * </ul>
 *
 * <pre>
 *   public class ArticleManagerTest extends SampleBaseTestCase {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock(name = "dbMock") private ArticleDatabase database;
 *       &#064;Mock(answer = RETURNS_MOCKS) private UserProvider userProvider;
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
 * You can also put initMocks() in your JUnit runner (&#064;RunWith) or use built-in runners: {@link MockitoJUnitRunner}
 */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mock {

    Answers answer() default Answers.RETURNS_DEFAULTS;

    String name() default "";

    Class<?>[] extraInterfaces() default {};
}
