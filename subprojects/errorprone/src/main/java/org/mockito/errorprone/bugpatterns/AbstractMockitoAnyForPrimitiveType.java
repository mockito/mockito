/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.errorprone.bugpatterns;

import static com.google.errorprone.matchers.Description.NO_MATCH;

import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ArrayType;
import java.util.List;
import javax.lang.model.type.TypeKind;

/** Base for {@link BugChecker}s that detect issues with any() matchers and primitive types. */
public abstract class AbstractMockitoAnyForPrimitiveType extends BugChecker
    implements MethodInvocationTreeMatcher {

  protected abstract Matcher<? super MethodInvocationTree> matcher();

  protected abstract String formatMessage(
      String expectedTypeAsString, Type matcherType, String replacementName);

  @Override
  public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
    if (!matcher().matches(tree, state)) {
      return NO_MATCH;
    }

    MethodSymbol method = ASTHelpers.getSymbol(tree);
    Type matcherType = method.getReturnType();

    // It is expected that the call to anyX() is itself the argument to another call which is
    // the one being mocked, e.g. something like this:
    //   when(mock.call(..., anyInt(), ...))...
    TreePath path = state.getPath();
    Tree parentTree = path.getParentPath().getLeaf();
    if (!(parentTree instanceof MethodInvocationTree)) {
      // Ignore calls that are not arguments to another method call.
      // TODO: Report this as a problem because it makes little sense.
      // TODO: Support casting.
      return NO_MATCH;
    }

    MethodInvocationTree parentCall = (MethodInvocationTree) parentTree;
    MethodSymbol parentMethod = ASTHelpers.getSymbol(parentCall);

    // Find the index of the argument in the parent call.
    int argumentIndex = -1;
    List<? extends ExpressionTree> parentArguments = parentCall.getArguments();
    for (int i = 0; i < parentArguments.size(); i++) {
      ExpressionTree argumentTree = parentArguments.get(i);
      if (argumentTree == tree) {
        argumentIndex = i;
        break;
      }
    }
    if (argumentIndex == -1) {
      throw new IllegalStateException(
          "Cannot find argument " + state.getSourceForNode(tree) + " in argument list from " + state.getSourceForNode(parentTree));
    }

    Type parameterType = getParameterType(parentMethod, argumentIndex);

    TypeKind parameterTypeKind = parameterType.getKind();
    if (parameterTypeKind.isPrimitive() && parameterTypeKind != matcherType.getKind()) {
      String expectedTypeAsString = parameterType.toString();
      String replacementName =
          "any"
              + Character.toUpperCase(expectedTypeAsString.charAt(0))
              + expectedTypeAsString.substring(1);

      String message = formatMessage(expectedTypeAsString, matcherType, replacementName);

      SuggestedFix.Builder fixBuilder = SuggestedFix.builder();

      ExpressionTree methodSelect = tree.getMethodSelect();
      String replacement;
      if (methodSelect instanceof MemberSelectTree) {
        MemberSelectTree qualifier = (MemberSelectTree) methodSelect;
        replacement = state.getSourceForNode(qualifier.getExpression()) + "." + replacementName;
      } else {
        replacement = replacementName;
        String staticImport = method.owner + "." + replacementName;
        fixBuilder.addStaticImport(staticImport);
      }

      SuggestedFix fix = fixBuilder.replace(tree, replacement + "()").build();

      return buildDescription(tree).setMessage(message).addFix(fix).build();
    }

    return NO_MATCH;
  }

  /**
   * Get the type of the parameter for a supplied argument.
   *
   * @param method the method symbol that is being called.
   * @param argumentIndex the index of the argument, can be greater than the number of parameters
   *     for a var arg method.
   * @return the type of the associated parameter.
   */
  private Type getParameterType(MethodSymbol method, int argumentIndex) {
    List<VarSymbol> parameters = method.getParameters();
    Type parameterType;
    int parameterCount = parameters.size();
    if (argumentIndex >= parameterCount && method.isVarArgs()) {
      VarSymbol varArgParameter = parameters.get(parameterCount - 1);
      parameterType = ((ArrayType) varArgParameter.asType()).getComponentType();
    } else {
      parameterType = parameters.get(argumentIndex).asType();
    }
    return parameterType;
  }
}
