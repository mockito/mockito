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
 * <li>Makes the test class more readable.</li>
 * </ul>
 *
 * <pre>
 *   public class ArticleManagerTest extends SampleBaseTestCase {
 *
 *       &#064;Mock private ArticleCalculator calculator;
 *       &#064;Mock private ArticleDatabase database;
 *       &#064;Spy private UserProvider userProvider = new ConsumerUserProvider();
 *
 *       &#064;InjectMock private ArticleManager manager = new ArticleManager();
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
 *           MockitoAnnotations.injectMocks(this);
 *       }
 *   }
 * </pre>
 *
 * <b>The field annotated with &#064;InjectMock must be initialized.</b>
 * Mocks and spies must also be initialized with <code>MockitoAnnotations.initMocks(this)</code>.
 *
 * <b><code>MockitoAnnotations.injectMocks(this)</code></b> method has to called to initialize annotated objects.
 * <p>
 * In above example, <code>injectMocks()</code> is called in &#064;Before (JUnit4) method of test's base class.
 * For JUnit3 <code>injectMocks()</code> can go to <code>setup()</code> method of a base class.
 * You can also put injectMocks() in your JUnit runner (&#064;RunWith) or use built-in runners: {@link org.mockito.runners.MockitoJUnitRunner}
 */
@Documented
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectMock {}
