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
 *    //Instance for spying is created by calling constructor explicitly:
 *    &#64;Spy Foo spyOnFoo = new Foo("argument");
 *    //Instance for spying is created by mockito via reflection (only default constructors supported): 
 *    &#64;Spy Bar spyOnBar;
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
 * Foo spyOnFoo = Mockito.spy(new Foo("argument"));
 * Bar spyOnFoo = Mockito.spy(new Bar());
 * </pre>
 *
 * <b>The field annotated with &#064;Spy can be initialized by Mockito if a zero argument constructor
 * can be found in the type (even private). <u>But Mockito cannot instantiate inner classes, local classes,
 * abstract classes and interfaces.</u></b>
 *
 * <b>The field annotated with &#064;Spy can be initiatialized explicitly at declaration point.
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
 * <p>
 * <b>Warning</b> if you call <code>MockitoAnnotations.initMocks(this)</code> in a
 * super class <b>constructor</b> then this will not work. It is because fields
 * in subclass are only instantiated after super class constructor has returned.
 * It's better to use &#64;Before.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Spy {
}
