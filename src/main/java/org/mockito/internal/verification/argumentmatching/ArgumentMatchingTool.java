/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.argumentmatching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;

@SuppressWarnings("unchecked")
public class ArgumentMatchingTool {

    private ArgumentMatchingTool() {}
    /**
     * Suspiciously not matching arguments are those that don't match, the toString() representation is the same but types are different.
     */
    public static Integer[] getSuspiciouslyNotMatchingArgsIndexes(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }

        List<Integer> suspicious = new LinkedList<>();
        int i = 0;
        for (ArgumentMatcher m : matchers) {
            if (m instanceof ContainsExtraTypeInfo
                    && !safelyMatches(m, arguments[i])
                    && toStringEquals(m, arguments[i])
                    && !((ContainsExtraTypeInfo) m).typeMatches(arguments[i])) {
                suspicious.add(i);
            }
            i++;
        }
        return suspicious.toArray(new Integer[0]);
    }

    private static boolean safelyMatches(ArgumentMatcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean toStringEquals(ArgumentMatcher m, Object arg) {
        return m.toString().equals(String.valueOf(arg));
    }

    /**
     * Suspiciously not matching arguments are those that don't match, and the classes have same simple name.
     */
    public static Set<String> getNotMatchingArgsWithSameName(
            List<ArgumentMatcher> matchers, Object[] arguments) {
        Map<String, Set<String>> classesHavingSameName = new HashMap<>();
        for (ArgumentMatcher m : matchers) {
            if (m instanceof ContainsExtraTypeInfo) {
                Object wanted = ((ContainsExtraTypeInfo) m).getWanted();
                if (wanted == null) {
                    continue;
                }
                Class wantedClass = wanted.getClass();
                classesHavingSameName
                        .computeIfAbsent(wantedClass.getSimpleName(), className -> new HashSet<>())
                        .add(wantedClass.getCanonicalName());
            }
        }
        for (Object argument : arguments) {
            if (argument == null) {
                continue;
            }
            Class wantedClass = argument.getClass();
            classesHavingSameName
                    .computeIfAbsent(wantedClass.getSimpleName(), className -> new HashSet<>())
                    .add(wantedClass.getCanonicalName());
        }
        return classesHavingSameName.entrySet().stream()
                .filter(classEntry -> classEntry.getValue().size() > 1)
                .map(classEntry -> classEntry.getKey())
                .collect(Collectors.toSet());
    }
}
