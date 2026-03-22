/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that suppresses static initializer blocks ({@code <clinit>}) for the specified classes.
 *
 * <p>This is useful when a class has heavy static initialization (database connections, file I/O,
 * system-dependent calls) that fails or is undesirable in test environments.
 *
 * <p><b>Important:</b> Static initializer suppression must happen <em>before</em> the class is first
 * loaded by the JVM. Once a class is loaded, its static initializer has already run and cannot be
 * undone. This means the feature requires Mockito to be attached as a {@code -javaagent} (premain).
 *
 * <p>Class names are specified as strings (not {@code Class<?>}) because referencing a class via
 * {@code .class} would trigger its loading and {@code <clinit>} execution, defeating the purpose.
 *
 * <p>After suppression, all static fields of the specified classes will have their JVM zero-value
 * defaults ({@code null}, {@code 0}, {@code false}).
 *
 * <h3>Test isolation</h3>
 *
 * <p>Suppression is <b>permanent</b> for the lifetime of the JVM: once a class is loaded with its
 * {@code <clinit>} suppressed, it cannot be re-initialized. This has two consequences:
 * <ul>
 *     <li>If test class A suppresses {@code com.example.Foo} and loads it, a later test class B
 *     (without this annotation) will see the same suppressed version of {@code Foo}.</li>
 *     <li>If test class B runs <em>first</em> and loads {@code Foo} normally, test class A will
 *     fail because the class is already loaded with its original initializer.</li>
 * </ul>
 *
 * <p>For these reasons, tests using this annotation should typically be run in a <b>dedicated JVM
 * fork</b> so that each test class gets a fresh classloader. Use Gradle's {@code forkEvery = 1}
 * or Maven Surefire's {@code reuseForks=false}, depending on your build tool. Additionally,
 * <b>parallel test execution should be avoided</b> for test classes that use this feature.
 *
 * <p>Example usage with JUnit Jupiter:
 * <pre class="code"><code class="java">
 * &#064;ExtendWith(MockitoExtension.class)
 * &#064;SuppressStaticInitializationFor({"com.example.HeavyClass", "com.example.AnotherClass"})
 * public class MyTest {
 *     // HeavyClass and AnotherClass will have their static initializers suppressed
 * }
 * </code></pre>
 *
 * @since 5.24.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Incubating
public @interface SuppressStaticInitializationFor {
    /**
     * Fully-qualified class names whose static initializers should be suppressed.
     *
     * @return array of fully-qualified class names
     */
    String[] value();
}
