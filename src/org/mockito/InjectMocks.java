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
 * Currently it injects fields via reflection. If you prefer constructor/setter injection - please contribute a patch.
 * </p>
 *
 * <p>
 * Mockito tries to inject by type (using name in case types are the same). 
 * Mockito does not throw anything when injection fails - you will have to satisfy the dependencies manually.
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
 *       &#064;InjectMocks private ArticleManager manager = new ArticleManager();
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
 *
 * <b>The field annotated with &#064;InjectMocks can be initiatialized explicitly (just like in the example).
 * Alternatively, if you don't provide the instance Mockito will try to find zero argument constructor (even private) and create an instance for you.
 * <u>But Mockito cannot instantiate inner classes, local classes, abstract classes and interfaces.</u></b>
 *
 * For example this class can be instantiated by Mockito :
 * <pre>public class Bar {
 *    private Bar() {}
 *    public Bar(String publicConstructorWithOneArg) {}
 * }</pre>
 * </p>
 *
 * </p>
 *
 * <p>
 * Note that &#064;InjectMocks will only inject mocks/spies created using the &#64;Spy or &#64;Mock annotation.
 * </p>
 *
 * <p>
 * <b><code>MockitoAnnotations.injectMocks(this)</code></b> method has to called to initialize annotated objects.
 * <p>
 *
 * <p>
 * In above example, <code>injectMocks()</code> is called in &#064;Before (JUnit4) method of test's base class.
 * For JUnit3 <code>injectMocks()</code> can go to <code>setup()</code> method of a base class.
 * You can also put injectMocks() in your JUnit runner (&#064;RunWith) or use built-in runners: {@link org.mockito.runners.MockitoJUnitRunner}
 * </p>
 */
@Documented
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMocks {}
