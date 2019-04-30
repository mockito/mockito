/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import static com.google.errorprone.matchers.Description.NO_MATCH;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.BugPattern.SeverityLevel;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.ClassTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.ClassTree;

/** Finds subclasses of @NotExtensible interfaces. */
@AutoService(BugChecker.class)
@BugPattern(
    name = "MockitoNotExtensible",
    summary = "Some types that are a part of Mockito public API are not intended to be extended.",
    explanation =
        "Some types that are a part of Mockito public API are not intended to be extended."
            + " It's because Mockito team needs to be able to add new methods to some types"
            + " without breaking compatibility contract. Any type that is not intended"
            + " to be extended is annotated with @NotExtensible.",
    severity = SeverityLevel.ERROR)
public class MockitoNotExtensible extends BugChecker implements ClassTreeMatcher {

    @Override
    public Description matchClass(ClassTree tree, VisitorState state) {
        if (tree.getImplementsClause().stream()
            .anyMatch(
                implementing ->
                    ASTHelpers.hasAnnotation(
                        ASTHelpers.getSymbol(implementing), "org.mockito.NotExtensible", state))) {
            return describeMatch(tree);
        }

        return NO_MATCH;
    }
}
