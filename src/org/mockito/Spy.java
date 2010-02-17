package org.mockito;

import java.lang.annotation.*;

/**
 * Allows shorthand wrapping of field instances in an spy object. <b>Warning</b> if you call
 * <code>MockitoAnnotations.init(this)</code> in a super class this will not work. Since
 *  fields in subclass are only instantiated after super class constructor has returned.
 *
 * <p>Example:
 * <pre>
 * public class Test{
 *
 *    &#64;Spy Foo spyOnFoo = new Foo();
 *
 *    &#64;Before
 *    public void init(){
 *       MockitoAnnotations.init(this);
 *    }
 *    ...
 * }
 * </pre>
 * <p>Same as doing:
 * <pre>
 *    Foo spyOnFoo = Mockito.spy(new Foo());
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Spy {
}
