/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.collections.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public class ArgumentsProcessor {
    // expands array varArgs that are given by runtime (1, [a, b]) into true
    // varArgs (1, a, b);
    public static Object[] expandVarArgs(final boolean isVarArgs, final Object[] args) {
        if (!isVarArgs || new ArrayUtils().isEmpty(args) || args[args.length - 1] != null && !args[args.length - 1].getClass().isArray()) {
            return args == null ? new Object[0] : args;
        }

        final int nonVarArgsCount = args.length - 1;
        Object[] varArgs;
        if (args[nonVarArgsCount] == null) {
            // in case someone deliberately passed null varArg array
            varArgs = new Object[] { null };
        } else {
            varArgs = ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        }
        final int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }

    public static List<ArgumentMatcher> argumentsToMatchers(Object[] arguments) {
        List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }
}
