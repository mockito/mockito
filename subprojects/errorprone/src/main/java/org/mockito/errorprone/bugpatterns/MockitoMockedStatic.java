/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.BugPattern.SeverityLevel;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.VariableTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.VariableTree;

@AutoService(BugChecker.class)
@BugPattern(
        name = "MockitoMockedStatic",
        summary = "Fields or parameters annotated with @Mock in MockedStatic can lead to compilation issues.",
        explanation = "Fields or parameters annotated with @Mock in MockedStatic can lead to compilation issues.",
        severity = SeverityLevel.ERROR)
public class MockitoMockedStatic extends BugChecker implements VariableTreeMatcher {

  @Override
  public Description matchVariable(VariableTree tree, VisitorState state) {
    if (tree.getModifiers().getAnnotations().stream()
            .anyMatch(annotation ->
                    state.getSourceForNode(annotation).equals("@org.mockito.Mock")
                            && state.getSourceForNode(tree.getType()).equals("org.mockito.MockedStatic"))) {
      return describeMatch(tree);
    }

    return Description.NO_MATCH;
  }
}
