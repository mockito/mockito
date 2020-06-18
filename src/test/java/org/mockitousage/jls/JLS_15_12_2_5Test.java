/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.jls;

import static net.bytebuddy.ClassFileVersion.JAVA_V6;
import static net.bytebuddy.ClassFileVersion.JAVA_V7;
import static net.bytebuddy.ClassFileVersion.JAVA_V8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.bytebuddy.ClassFileVersion;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * Illustrate differences in the JLS depending on the Java version.
 */
@RunWith(Enclosed.class)
public class JLS_15_12_2_5Test {

    /**
     * The JLS §15.12.2.5 states that the compiler must chose the most specific overload in Java 6 or Java 7,
     * but with generics in the matcher, <strong>javac</strong> selects the upper bound, which is {@code Object},
     * as such javac selects the most generic method.
     *
     * https://docs.oracle.com/javase/specs/jls/se6/html/expressions.html#15.12.2.5
     * https://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.12.2.5
     *
     * <blockquote>
     *     <p>If more than one member method is both accessible and applicable to a method invocation, it is necessary to
     *     choose one to provide the descriptor for the run-time method dispatch. The Java programming language uses
     *     the rule that the most specific method is chosen.</p>
     *
     *     <p>The informal intuition is that one method is more specific than another if any invocation handled by
     *     the first method could be passed on to the other one without a compile-time type error.</p>
     * </blockquote>
     */
    public static class JLS_15_12_2_5_Java6_Java7_Test {
        @Before
        public void setUp() throws Exception {
            Assume.assumeTrue(
                    ClassFileVersion.of(JLS_15_12_2_5_Java6_Java7_Test.class).equals(JAVA_V6)
                            || ClassFileVersion.of(JLS_15_12_2_5_Java6_Java7_Test.class)
                                    .equals(JAVA_V7));
        }

        @Test
        public void with_single_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.oneArg(isNull())).thenReturn("ok");

            assertThat(mock.oneArg(null))
                    .describedAs(
                            "Most generic method chosen for matcher "
                                    + "(isNull generic upper bound is Object), but null applies "
                                    + "to select most specific method")
                    .isEqualTo(null);
        }

        @Test
        public void with_single_arg_and_matcher_cast() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.oneArg((String) isNull())).thenReturn("ok");

            assertThat(mock.oneArg(null))
                    .describedAs("Most specific method enforced for matcher via cast")
                    .isEqualTo("ok");
        }

        @Test
        public void with_single_arg_and_null_Object_reference() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.oneArg(isNull())).thenReturn("ok");

            Object arg = null;
            assertThat(mock.oneArg(arg))
                    .describedAs("Most generic method chosen for matcher")
                    .isEqualTo("ok");
        }

        @Test
        public void with_variable_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            assertThat(mock.varargs(null))
                    .describedAs(
                            "Most generic method chosen for matcher "
                                    + "(isNull generic upper bound is Object), but null applies "
                                    + "to select most specific method")
                    .isEqualTo(null);
        }

        @Test
        public void with_variable_arg_and_matcher_String_cast() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs((String) isNull())).thenReturn("ok");

            assertThat(mock.varargs(null))
                    .describedAs("Most specific method enforced for matcher via String cast")
                    .isEqualTo("ok");
        }

        @Test
        public void with_variable_arg_and_matcher_String_array_cast() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs((String[]) isNull())).thenReturn("ok");

            assertThat(mock.varargs(null))
                    .describedAs("Most specific method enforced for matcher via String[] cast")
                    .isEqualTo("ok");
        }

        @Test
        public void with_variable_arg_and_null_Object_array() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            Object[] args = null;
            assertThat(mock.varargs(args))
                    .describedAs("isNull matcher generic upper bound is Object")
                    .isEqualTo("ok");
        }

        @Test
        public void with_variable_arg_and_null_Object_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            Object arg = null;
            assertThat(mock.varargs(arg))
                    .describedAs("isNull matcher generic upper bound is Object")
                    .isEqualTo("ok");
        }
    }

    /**
     * The JLS §15.12.2.5 states that the compiler must chose the most specific overload in Java 8, however the
     * Java 8 compiler perform a type inference before selecting
     *
     * https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2
     * <blockquote>
     *     <p>Deciding whether a method is applicable will, in the case of generic methods (§8.4.4), require an analysis
     *     of the type arguments. Type arguments may be passed explicitly or implicitly. If they are passed implicitly,
     *     bounds of the type arguments must be inferred (§18 (Type Inference)) from the argument expressions.</p>
     *
     *     <p>If several applicable methods have been identified during one of the three phases of applicability
     *     testing, then the most specific one is chosen, as specified in section §15.12.2.5.</p>
     * </blockquote>
     *
     * https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.1
     * <blockquote>
     *     <p>The definition of potential applicability goes beyond a basic arity check to also take into account
     *     the presence and "shape" of functional interface target types. In some cases involving type argument
     *     inference, a lambda expression appearing as a method invocation argument cannot be properly typed until
     *     after overload resolution. These rules allow the form of the lambda expression to still be taken into
     *     account, discarding obviously incorrect target types that might otherwise cause ambiguity errors.</p>
     * </blockquote>
     *
     * https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2.5
     * <blockquote>
     *     <p>One applicable method m1 is more specific than another applicable method m2, for an invocation with argument
     *     expressions e1, ..., ek, if any of the following are true:
     *     <ul>
     *
     *     <li>m2 is generic, and m1 is inferred to be more specific than m2 for argument expressions e1, ..., ek
     *     by §18.5.4.</li>
     *     <li>m2 is not generic, and m1 and m2 are applicable by strict or loose invocation, and where m1 has formal
     *     parameter types S1, ..., Sn and m2 has formal parameter types T1, ..., Tn, the type Si is more specific
     *     than Ti for argument ei for all i (1 ≤ i ≤ n, n = k).</li>
     *     <li>m2 is not generic, and m1 and m2 are applicable by variable arity invocation, and where the first k
     *     variable arity parameter types of m1 are S1, ..., Sk and the first k variable arity parameter types of m2
     *     are T1, ..., Tk, the type Si is more specific than Ti for argument ei for all i (1 ≤ i ≤ k).
     *     Additionally, if m2 has k+1 parameters, then the k+1'th variable arity parameter type of m1 is a subtype
     *     of the k+1'th variable arity parameter type of m2.</li>
     *     </ul></p>
     *
     *     <p>The above conditions are the only circumstances under which one method may be more specific than another.</p>
     *
     *     <p>A type S is more specific than a type T for any expression if S <: T (§4.10).</p>
     * </blockquote>
     */
    public static class JLS_15_12_2_5_Java8_Test {
        @Before
        public void setUp() throws Exception {
            Assume.assumeTrue(
                    ClassFileVersion.of(JLS_15_12_2_5_Java8_Test.class).isAtLeast(JAVA_V8));
        }

        @Test
        public void with_single_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.oneArg(isNull())).thenReturn("ok");

            assertThat(mock.oneArg(null))
                    .describedAs("Most specific method chosen for matcher and for null")
                    .isEqualTo("ok");
        }

        @Test
        public void with_single_arg_and_null_Object_reference() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.oneArg(isNull())).thenReturn("ok");

            Object arg = null;
            assertThat(mock.oneArg(arg)).describedAs("not the stubbed method").isEqualTo(null);
        }

        @Test
        public void with_variable_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            assertThat(mock.varargs(null))
                    .describedAs("Most specific method chosen for matcher and for null")
                    .isEqualTo("ok");
        }

        @Test
        public void with_variable_arg_and_null_Object_array() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            Object[] args = null;
            assertThat(mock.varargs(args))
                    .describedAs("Most specific method chosen for matcher")
                    .isEqualTo(null);
        }

        @Test
        public void with_variable_arg_and_null_Object_arg() throws Exception {
            SingleOverload mock = mock(SingleOverload.class);

            when(mock.varargs(isNull())).thenReturn("ok");

            Object arg = null;
            assertThat(mock.varargs(arg))
                    .describedAs("Most specific method chosen for matcher")
                    .isEqualTo(null);
        }
    }

    interface SingleOverload {
        String oneArg(Object arg);

        String oneArg(String arg);

        String varargs(Object... args);

        String varargs(String... args);
    }
}
