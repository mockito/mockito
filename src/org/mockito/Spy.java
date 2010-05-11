/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.*;

/**
 * Allows shorthand wrapping of field instances in an spy object.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * public class Test{
 *    &#64;Spy Foo spyOnFoo = new Foo();
 *    &#64;Before
 *    public void init(){
 *       MockitoAnnotations.initMocks(this);
 *    }
 *    ...
 * }
 * </pre>
 * <p>
 * Same as doing:
 * 
 * <pre>
 * Foo spyOnFoo = Mockito.spy(new Foo());
 * </pre>
 * 
 * <b>Warning</b> if you call <code>MockitoAnnotations.initMocks(this)</code> in a
 * super class <b>constructor</b> then this will not work. It is because fields
 * in subclass are only instantiated after super class constructor has returned.
 * It's better to use &#64;Before.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Spy {
}
