/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * <ul>
 * <li>Allows shorthand mock and spy injection.</li>
 * <li>Minimizes repetitive mock and spy injection.</li>
 * </ul>
 * <p>
 * Mockito will try to inject mocks only either by constructor injection,
 * setter injection, or property injection in order and as described below.
 * If any of the following strategy fail, then Mockito <strong>won't report failure</strong>;
 * i.e. you will have to provide dependencies yourself.
 * <ol>
 *     <li><strong>Constructor injection</strong>; the biggest constructor is chosen,
 *     then arguments are resolved with mocks declared in the test only.
 *     <p><u>Note:</u> If arguments can not be found, then null is passed.
 *     If non-mockable types are wanted, then constructor injection won't happen.
 *     In these cases, you will have to satisfy dependencies yourself.</p></li>
 *
 *     <li><strong>Property setter injection</strong>; mocks will first be resolved by type,
 *     then (using name if there is several property of the same type).
 *     <p><u>Note:</u> If &#064;InjectMocks instance wasn't initialized before and have a no-arg constructor,
 *     then it will be initialized with this constructor.</p></li>
 *
 *     <li><strong>Field injection</strong>; mocks will first be resolved by type,
 *     then (using name if there is several property of the same type).
 *     <p><u>Note:</u> If &#064;InjectMocks instance wasn't initialized before and have a no-arg constructor,
 *     then it will be initialized with this constructor.</p></li>
 * </ol>
 * </p>
 *
 * <p>
 * Example:
 * <pre>
 *   public class ArticleManagerTest extends SampleBaseTestCase {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Spy private UserProvider userProvider = new ConsumerUserProvider();
 *
 *       &#064;InjectMocks private ArticleManager manager;
 *
 *       &#064;Test public void shouldDoSomething() {
 *           manager.initiateArticle();
 *           verify(database).addListener(any(ArticleListener.class));
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
 * </p>
 *
 * <p>
 * In the above example the field ArticleManager annotated with &#064;InjectMocks can have
 * a parameterized constructor only or a no-arg constructor only, or both.
 * All these constructors can be package protected, protected or private, however
 * <u>Mockito cannot instantiate inner classes, local classes, abstract classes and of course interfaces.</u>
 *
 * <p>The same stands for setters or fields, they can be declared with private
 * visibility, Mockito will see them through reflection.
 * However fields that are static or final will be ignored.</p>
 *
 * <p>So on the field that needs injection, for example constructor injection will happen here :</p>
 * <pre>
 *   public class ArticleManager {
 *       ArticleManager(ArticleCalculator calculator, ArticleDatabase database) {
 *           // parameterized constructor
 *       }
 *   }
 * </pre>
 *
 * <p>Property setter injection will happen here :</p>
 * <pre>
 *   public class ArticleManager {
 *       ArticleManager() {
 *           // no-arg constructor
 *       }
 *
 *       void setDatabase(ArticleDatabase database) {
 *           // setter
 *       }
 *   }
 * </pre>
 *
 * <p>Field injection will be used here :</p>
 * <pre>
 *   public class ArticleManager {
 *       private ArticleDatabase database;
 *       private ArticleCalculator calculator;
 *   }
 * </pre>
 * </p>
 *
 * <p>And finally, no injection will happen on the type in this case:</p>
 * <pre>
 *   public class ArticleManager {
 *       private ArticleDatabase database;
 *       private ArticleCalculator calculator;
 *
 *       ArticleManager(ArticleObserver observer, boolean flag) {
 *           // observer is not declared in the test above.
 *           // flag is not mockable anyway
 *       }
 *   }
 * </pre>
 * </p>
 *
 *
 * <p>
 * Again, note that &#064;InjectMocks will only inject mocks/spies created using the &#64;Spy or &#64;Mock annotation.
 * </p>
 *
 * <p>
 * <b><code>MockitoAnnotations.initMocks(this)</code></b> method has to called to initialize annotated objects.
 * A <code>MockitoJUnitRunner</code> can also be used to initialize mocks instead of the &#64;Before approach.
 * <p>
 *
 * <p>
 * In above example, <code>initMocks()</code> is called in &#064;Before (JUnit4) method of test's base class.
 * For JUnit3 <code>initMocks()</code> can go to <code>setup()</code> method of a base class.
 * You can also put initMocks() in your JUnit runner (&#064;RunWith) or use built-in runners: {@link org.mockito.runners.MockitoJUnitRunner}
 * </p>
 */
@Documented
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMocks {}
