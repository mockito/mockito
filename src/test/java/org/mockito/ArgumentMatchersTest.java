package org.mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.and;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.aryEq;
import static org.mockito.ArgumentMatchers.cmpEq;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.find;
import static org.mockito.ArgumentMatchers.geq;
import static org.mockito.ArgumentMatchers.gt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.leq;
import static org.mockito.ArgumentMatchers.lt;
import static org.mockito.ArgumentMatchers.or;
import static org.mockito.ArgumentMatchers.startsWith;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;
import org.mockito.internal.matchers.StartsWith;

@RunWith(Parameterized.class)
public class ArgumentMatchersTest {

    public boolean actual, expected;

    public ArgumentMatchersTest(boolean actual, boolean expected) {
        this.actual = actual;
        this.expected = expected;
    }

    @Test
    public void verify_scenarios() {
        assertThat(expected, is(actual));
    }

    @Parameters
    public static List<Object[]> scenarios() {
        return Arrays.asList(
            getScenario(isInstanceOf(isA(String.class), InstanceOf.class), true),
            getScenario(isInstanceOf(geq(2), GreaterOrEqual.class), true),
            getScenario(isInstanceOf(leq(2), LessOrEqual.class), true),
            getScenario(isInstanceOf(gt(3d), GreaterThan.class), true),
            getScenario(isInstanceOf(lt(1d), LessThan.class), true),
            getScenario(isInstanceOf(eq(1d), Equals.class), true),
            getScenario(isInstanceOf(cmpEq("aa"), CompareEqual.class), true),
            getScenario(isInstanceOf(find("aa"), Find.class), true),
            getScenario(isInstanceOf(aryEq(new Integer[] {1, 2, 3}), ArrayEquals.class), true),
            getScenario(isInstanceOf(eq(2.32, 0.02), EqualsWithDelta.class), true),
            getScenario(isInstanceOf(isNotNull(), NotNull.class), true),
            getScenario(isInstanceOf(contains("sub"), Contains.class), true),
            getScenario(isInstanceOf(startsWith("pre"), StartsWith.class), true),
            getScenario(isInstanceOf(endsWith("pre"), EndsWith.class), true),
            getScenario(isInstanceOf(any(), Any.class), true),
            getScenario(isInstanceOf(isNull(), Null.class), true),
            getScenario(isInstanceOf(and(), And.class), true),
            getScenario(isInstanceOf(or(), Or.class), true)
        );
    }

    private static Object[] getScenario(boolean expected, boolean actual) {
        return new Object[] { expected, actual};
    }

    private static boolean isInstanceOf(Object arg, Class clazz) {
        return clazz.isAssignableFrom(arg.getClass());
    }
}
