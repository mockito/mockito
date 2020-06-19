/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.mockito.junit.MockitoJUnitRunner;

/**
 * Allows shorthand wrapping of field instances in an spy object.
 *
 * <p>
 * Example:
 *
 * <pre class="code"><code class="java">
 * public class Test{
 *    //Instance for spying is created by calling constructor explicitly:
 *    &#64;Spy Foo spyOnFoo = new Foo("argument");
 *    //Instance for spying is created by mockito via reflection (only default constructors supported):
 *    &#64;Spy Bar spyOnBar;
 *    private AutoCloseable closeable;
 *    &#64;Before
 *    public void init() {
 *       closeable = MockitoAnnotations.openMocks(this);
 *    }
 *    &#64;After
 *    public void release() throws Exception {
 *       closeable.close();
 *    }
 *    ...
 * }
 * </code></pre>
 * <p>
 * Same as doing:
 *
 * <pre class="code"><code class="java">
 * Foo spyOnFoo = Mockito.spy(new Foo("argument"));
 * Bar spyOnBar = Mockito.spy(new Bar());
 * </code></pre>
 *
 * <p>
 * <strong>A field annotated with &#064;Spy can be initialized explicitly at declaration point.
 * Alternatively, if you don't provide the instance Mockito will try to find zero argument constructor (even private)
 * and create an instance for you.
 * <u>But Mockito cannot instantiate inner classes, local classes, abstract classes and interfaces.</u></strong>
 *
 * For example this class can be instantiated by Mockito :
 * <pre class="code"><code class="java">public class Bar {
 *    private Bar() {}
 *    public Bar(String publicConstructorWithOneArg) {}
 * }</code></pre>
 * </p>
 *
 * <h4>Important gotcha on spying real objects!</h4>
 * <ol>
 * <li>Sometimes it's impossible or impractical to use {@link Mockito#when(Object)} for stubbing spies.
 * Therefore for spies it is recommended to always use <code>doReturn</code>|<code>Answer</code>|<code>Throw()</code>|<code>CallRealMethod</code>
 * family of methods for stubbing. Example:
 *
 * <pre class="code"><code class="java">
 *   List list = new LinkedList();
 *   List spy = spy(list);
 *
 *   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
 *   when(spy.get(0)).thenReturn("foo");
 *
 *   //You have to use doReturn() for stubbing
 *   doReturn("foo").when(spy).get(0);
 * </code></pre>
 * </li>
 *
 * <li>Mockito <b>*does not*</b> delegate calls to the passed real instance, instead it actually creates a copy of it.
 * So if you keep the real instance and interact with it, don't expect the spied to be aware of those interaction
 * and their effect on real instance state.
 * The corollary is that when an <b>*unstubbed*</b> method is called <b>*on the spy*</b> but <b>*not on the real instance*</b>,
 * you won't see any effects on the real instance.</li>
 *
 * <li>Watch out for final methods.
 * Mockito doesn't mock final methods so the bottom line is: when you spy on real objects + you try to stub a final method = trouble.
 * Also you won't be able to verify those method as well.
 * </li>
 * </ol>
 *
 * <p>
 * <strong>One last warning :</strong> if you call <code>MockitoAnnotations.openMocks(this)</code> in a
 * super class <strong>constructor</strong> then this will not work. It is because fields
 * in subclass are only instantiated after super class constructor has returned.
 * It's better to use &#64;Before.
 * <strong>Instead</strong> you can also put openMocks() in your JUnit runner (&#064;RunWith) or use the built-in
 * {@link MockitoJUnitRunner}. Also, make sure to release any mocks after disposing your test class with a
 * corresponding hook.
 * </p>
 *
 * <p>Note that the spy won't have any annotations of the spied type, because CGLIB won't rewrite them.
 * It may troublesome for code that rely on the spy to have these annotations.</p>
 *
 * @see Mockito#spy(Object)
 * @see Mock
 * @see InjectMocks
 * @see MockitoAnnotations#openMocks(Object)
 * @see MockitoJUnitRunner
 * @since 1.8.3
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface Spy {}
