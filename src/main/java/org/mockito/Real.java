package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark a field as injectable real object, which will be used for {@link InjectMocks}.
 *
 * In the example below a use-case where the subject under testing requires both a real and mock as
 * constructor argument. Without &#064;Real, eventName in EventWatcher will be null and causing a
 * {@link NullPointerException} when addMessage(String, String) is executed.
 *
 * <pre class="code"><code class="java">
 *   public class EventWatcher {
 *
 *       private String eventName;
 *
 *       private Notifier notifier;
 *
 *       public EventWatcher(String eventName, Notifier notifier) {
 *           this.eventName = eventName;
 *           this.notifier = notifier;
 *       }
 *
 *       public void addMessage(String event, String message) {
 *           if (this.eventName.equals(event)) {
 *               notifier.notify(message);
 *           }
 *       }
 *   }
 *
 *   public class MessageCacheTest extends SampleBaseTestCase {
 *
 *       &#064;Real String eventName = "PLAYER_ADDED_EVENT";
 *
 *       &#064;Mock Notifier notifier;
 *
 *       &#064;InjectMocks EventWatcher watcher;
 *
 *       &#064;Test
 *       public void should_notify_when_message_is_received_with_correct_event_name() {
 *           watcher.addMessage("PLAYER_ADDED_EVENT", "My message");
 *           verify(notifier).notify("My message");
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
 *
 * <p>
 * <strong>Note:</strong> Whenever possible, use &#064;Mock. Only use this annotation if you are unable to instantiate
 * mocks of a class (for example the class is final).
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
 * @see InjectMocks
 * @see MockitoAnnotations#initMocks(Object)
 * @see org.mockito.runners.MockitoJUnitRunner
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Real {}
