/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.*;

/**
 * Change the behavior of {@link InjectMocks}: allow injection of static/final fields that are normally skipped.
 *
 * <p>Typically, injection into instances is done via the constructor.</p>
 *
 * <p>In most cases you should refactor your code to support these best practices!</p>
 *
 * <p>However, sometimes you don't want to/cannot expose fields via constructor as this might change your api
 * (even if the constructor is package-private).<br/>
 * Even worse with static fields: you'd have to make setters available via method call.<br/>
 * And then there's always legacy/library code that cannot be changed easily :(</p>
 *
 * <p>InjectUnsafe to the rescue:<br/>
 * Modifies the behavior of InjectMocks in a way that allows injection into static and final fields. </p>
 *
 * <hr/>
 *
 * <strong>
 *     With great power comes great responsibility: while injecting into final instance variables is safe,
 *     injecting into <i>static</i> or <i>static final</i> fields may have unexpected consequences:
 *     if these values do not get reset to their default value (mockito does <i>not</i> do this!), subsequent tests
 *     will use the mock.
 * </strong>
 *
 * <hr/>
 *
 * <p> Please note: not all mock makers are supported equally:
 * bytebuddy (the default) will throw {@link IllegalAccessError}
 * if you try to use <code>staticFields = {@link OverrideStaticFields#STATIC_FINAL}</code>. </p>
 *
 * <p>
 * Example:
 * <pre class="code"><code class="java">
 * // the object under test:
 * public class ArticleCalculator {
 *     private static Computer computer = new Computer();
 *
 *     public long calculate(Integer foo) {
 *         return computer.retrieveOrCalculateExpensiveOperation(foo);
 *     }
 * }
 *
 * public class Computer {
 *     private final ConcurrentHashMap<Integer, Long> data = new ConcurrentHashMap<>();
 *
 *     public Long retrieveOrCalculateExpensiveOperation(int param) {
 *         return data.computeIfAbsent(param, (k) -> (long) ((Math.random() * Integer.MAX_VALUE)) * param);
 *     }
 * }
 *
 * public class CalculatorTest {
 *
 *     &#064;Mock
 *     private Computer computer;
 *
 *     &#064;InjectMocks
 *     &#064;InjectUnsafe(staticFields = OverrideStaticFields.STATIC)
 *     private ArticleCalculator calculator;
 *
 *     &#064;Before
 *     public void initMocks() {
 *         MockitoAnnotations.initMocks(this);
 *     }
 *
 *     &#064;Test
 *     public void shouldCalculate() {
 *         Mockito.when(computer.retrieveOrCalculateExpensiveOperation(3)).thenReturn(42L);
 *
 *         long result = calculator.calculate(3);
 *
 *         Assert.assertEquals(42L, result);
 *     }
 * }
 * </code></pre>
 *
 * If the &#064;InjectMocks annotation is not given in the example above,
 * the mock will (silently) not get injected and the test will fail.
 * </p>
 *
 * FIXME: add @since tag
 * FIXME: document new API in Mockto class
 */
@Incubating
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectUnsafe {
    enum OverrideStaticFields {
        /**
         * Default: no injection into static fields
         */
        NONE,
        /**
         * Allow injection into static fields - non-final only.
         */
        STATIC,
        /**
         * Allow injection into static fields - including static final.
         */
        STATIC_FINAL
    }

    enum OverrideInstanceFields {
        /**
         * Default: no injection into final instance fields
         */
        NONE,
        /**
         * Allow mock injection into final instance fields.
         */
        FINAL
    }

    /**
     * Allow mock-injection into instance fields that get skipped during the normal injection cycle.
     */
    OverrideInstanceFields instanceFields() default OverrideInstanceFields.NONE;

    /**
     * Only use this if you <strong>absolute know what you're doing!</strong>.
     *
     * This will change the value of static fields value for <strong>all subsequent usages of the static field</strong>
     * as long as the current class loader lives.
     * This might lead to very unexpected and hard-to-debug behavior of other tests!
     */
    OverrideStaticFields staticFields() default OverrideStaticFields.NONE;
}
