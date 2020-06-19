/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.*;

/**
 * Allows shorthand {@link org.mockito.ArgumentCaptor} creation on fields.
 *
 * <p>Example:
 * <pre class="code"><code class="java">
 * public class Test{
 *
 *    &#64;Captor ArgumentCaptor&lt;AsyncCallback&lt;Foo&gt;&gt; captor;
 *
 *    private AutoCloseable closeable;
 *
 *    &#64;Before
 *    public void open() {
 *       MockitoAnnotations.openMocks(this);
 *    }
 *
 *    &#64;After
 *    public void release() throws Exception {
 *       closeable.close();
 *    }
 *
 *    &#64;Test public void shouldDoSomethingUseful() {
 *       //...
 *       verify(mock).doStuff(captor.capture());
 *       assertEquals("foo", captor.getValue());
 *    }
 * }
 * </code></pre>
 *
 * <p>
 * One of the advantages of using &#64;Captor annotation is that you can avoid warnings related capturing complex generic types.
 *
 * @see ArgumentCaptor
 * @since 1.8.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Captor {}
