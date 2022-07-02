/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.*;

/**
 * Change the behavior of {@link InjectMocks}: allow injection of static/final fields that are normally skipped.
 *
 * <p>Typically, injection into instances is done via the constructor
 * <strong>and in most cases you should refactor your code to support these best practices!</strong></p>
 *
 * <p>However, sometimes you don't want to/cannot expose fields via constructor as this might change your api
 * (even if the constructor is package-private).<br/>
 * Even worse with static fields: you'd have to make expose the fields via setters.<br/>
 * And then there's always legacy/library code that cannot be changed easily :(</p>
 *
 * <p>
 * InjectUnsafe to the rescue:<br/>
 * Modifies the behavior of InjectMocks in a way that allows injection into static and final fields. </p>
 * <p>
 * <hr/>
 *
 * <strong>
 * <p>
 * With great power comes great responsibility: while injecting into final instance variables is safe,
 * injecting into <i>static</i> or <i>static final</i> fields may have unexpected consequences:
 * </p>
 * <p>
 * if these values do not get reset to their default value (mockito does <i>not</i> do this!),
 * followup tests in any test class anywhere (!) will use the mock until the ClassLoader running the tests
 * shuts down (usually at JVM termination).
 * </p>
 * <p>
 * If you forget this, there <i>will</i> be hard-to-debug bugs!
 * </p>
 * </strong>
 * <p>
 * <hr/>
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
 *     &#064;InjectUnsafe(OverrideStaticFields.STATIC)
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
 *         // the mocked value is returned
 *         Assert.assertEquals(42L, result);
 *     }
 * }
 * </code></pre>
 * <p>
 * If the &#064;InjectMocks annotation is not given in the example above,
 * the mock will (silently) not get injected and the test will fail.
 * </p>
 *
 * @since 2.23.9
 */
@Incubating
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectUnsafe {
    enum UnsafeFieldModifier {
        /**
         * no injection into static fields. Mainly useful for tooling.
         */
        NONE(false, false),
        /**
         * Allow mock injection into final instance fields.
         */
        FINAL(false, false),
        /**
         * Allow injection into static fields - non-final only.
         */
        STATIC(true, true),
        /**
         * Only use this if you <strong>absolutely know what you're doing!</strong>, see {@link InjectUnsafe} for
         * details.
         * <p>
         * Allow injection into static fields - including static final.
         */
        STATIC_FINAL(true, true);

        public final boolean restoreOldValue;
        public final boolean makeAccessible;

        UnsafeFieldModifier(boolean restoreOldValue, boolean makeAccessible) {
            this.restoreOldValue = restoreOldValue;
            this.makeAccessible = makeAccessible;
        }
    }

    /**
     * Allow mock-injection into fields that get skipped during the normal injection cycle.
     */
    UnsafeFieldModifier value() default UnsafeFieldModifier.FINAL;

    UnsafeFieldModifier FALLBACK_VALUE = UnsafeFieldModifier.NONE;
}
