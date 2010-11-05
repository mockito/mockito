/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import static org.mockito.exceptions.Pluralizer.pluralize;
import static org.mockito.internal.util.StringJoiner.join;

import java.util.List;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.JUnitTool;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.exceptions.util.ScenarioPrinter;
import org.mockito.internal.invocation.Invocation;

/**
 * Reports verification and misusing errors.
 * <p>
 * One of the key points of mocking library is proper verification/exception
 * messages. All messages in one place makes it easier to tune and amend them.
 * <p>
 * Reporter can be injected and therefore is easily testable.
 * <p>
 * Generally, exception messages are full of line breaks to make them easy to
 * read (xunit plugins take only fraction of screen on modern IDEs).
 */
public class Reporter {

    public void checkedExceptionInvalid(Throwable t) {
        throw new MockitoException(join(
                "Checked exception is invalid for this method!",
                "Invalid: " + t
                ));
    }

    public void cannotStubWithNullThrowable() {
        throw new MockitoException(join(
                "Cannot stub with null throwable!"
                ));

    }

    public void unfinishedStubbing(Location location) {
        throw new UnfinishedStubbingException(join(
                "Unfinished stubbing detected here:",
                location,
                "",
                "E.g. thenReturn() may be missing.",
                "Examples of correct stubbing:",
                "    when(mock.isOk()).thenReturn(true);",
                "    when(mock.isOk()).thenThrow(exception);",
                "    doThrow(exception).when(mock).someVoidMethod();",
                "Hints:",
                " 1. missing thenReturn()",
                " 2. you are trying to stub a final method, you naughty developer!",
                ""
        ));
    }

    public void missingMethodInvocation() {
        throw new MissingMethodInvocationException(join(
                "when() requires an argument which has to be 'a method call on a mock'.",
                "For example:",
                "    when(mock.getArticles()).thenReturn(articles);",
                "",
                "Also, this error might show up because:",
                "1. you stub either of: final/private/equals()/hashCode() methods.",
                "   Those methods *cannot* be stubbed/verified.",
                "2. inside when() you don't call method on mock but on some other object."
        ));
    }

    public void unfinishedVerificationException(Location location) {
        UnfinishedVerificationException exception = new UnfinishedVerificationException(join(
                "Missing method call for verify(mock) here:",
                location,
                "",
                "Example of correct verification:",
                "    verify(mock).doSomething()",
                "",
                "Also, this error might show up because you verify either of: final/private/equals()/hashCode() methods.",
                "Those methods *cannot* be stubbed/verified.",
                ""
        ));

        throw exception;
    }

    public void notAMockPassedToVerify(Class type) {
        throw new NotAMockException(join(
                "Argument passed to verify() is of type " + type.getSimpleName() + " and is not a mock!",
                "Make sure you place the parenthesis correctly!",
                "See the examples of correct verifications:",
                "    verify(mock).someMethod();",
                "    verify(mock, times(10)).someMethod();",
                "    verify(mock, atLeastOnce()).someMethod();"
        ));
    }

    public void nullPassedToVerify() {
        throw new NullInsteadOfMockException(join(
                "Argument passed to verify() should be a mock but is null!",
                "Examples of correct verifications:",
                "    verify(mock).someMethod();",
                "    verify(mock, times(10)).someMethod();",
                "    verify(mock, atLeastOnce()).someMethod();",
                "Also, if you use @Mock annotation don't miss initMocks()"
        ));
    }

    public void notAMockPassedToWhenMethod() {
        throw new NotAMockException(join(
                "Argument passed to when() is not a mock!",
                "Example of correct stubbing:",
                "    doThrow(new RuntimeException()).when(mock).someMethod();"
        ));
    }

    public void nullPassedToWhenMethod() {
        throw new NullInsteadOfMockException(join(
                "Argument passed to when() is null!",
                "Example of correct stubbing:",
                "    doThrow(new RuntimeException()).when(mock).someMethod();",
                "Also, if you use @Mock annotation don't miss initMocks()"
        ));
    }

    public void mocksHaveToBePassedToVerifyNoMoreInteractions() {
        throw new MockitoException(join(
                "Method requires argument(s)!",
                "Pass mocks that should be verified, e.g:",
                "    verifyNoMoreInteractions(mockOne, mockTwo);",
                "    verifyZeroInteractions(mockOne, mockTwo);"
                ));
    }

    public void notAMockPassedToVerifyNoMoreInteractions() {
        throw new NotAMockException(join(
            "Argument(s) passed is not a mock!",
            "Examples of correct verifications:",
            "    verifyNoMoreInteractions(mockOne, mockTwo);",
            "    verifyZeroInteractions(mockOne, mockTwo);"
        ));
    }

    public void nullPassedToVerifyNoMoreInteractions() {
        throw new NullInsteadOfMockException(join(
                "Argument(s) passed is null!",
                "Examples of correct verifications:",
                "    verifyNoMoreInteractions(mockOne, mockTwo);",
                "    verifyZeroInteractions(mockOne, mockTwo);"
        ));
    }

    public void notAMockPassedWhenCreatingInOrder() {
        throw new NotAMockException(join(
                "Argument(s) passed is not a mock!",
                "Pass mocks that require verification in order.",
                "For example:",
                "    InOrder inOrder = inOrder(mockOne, mockTwo);"
                ));
    }

    public void nullPassedWhenCreatingInOrder() {
        throw new NullInsteadOfMockException(join(
                "Argument(s) passed is null!",
                "Pass mocks that require verification in order.",
                "For example:",
                "    InOrder inOrder = inOrder(mockOne, mockTwo);"
                ));
    }

    public void mocksHaveToBePassedWhenCreatingInOrder() {
        throw new MockitoException(join(
                "Method requires argument(s)!",
                "Pass mocks that require verification in order.",
                "For example:",
                "    InOrder inOrder = inOrder(mockOne, mockTwo);"
                ));
    }

    public void inOrderRequiresFamiliarMock() {
        throw new MockitoException(join(
                "InOrder can only verify mocks that were passed in during creation of InOrder.",
                "For example:",
                "    InOrder inOrder = inOrder(mockOne);",
                "    inOrder.verify(mockOne).doStuff();"
                ));
    }

    public void invalidUseOfMatchers(int expectedMatchersCount, int recordedMatchersCount) {
        throw new InvalidUseOfMatchersException(join(
                "Invalid use of argument matchers!",
                expectedMatchersCount + " matchers expected, " + recordedMatchersCount + " recorded.",
                "This exception may occur if matchers are combined with raw values:",
                "    //incorrect:",
                "    someMethod(anyObject(), \"raw String\");",
                "When using matchers, all arguments have to be provided by matchers.",
                "For example:",
                "    //correct:",
                "    someMethod(anyObject(), eq(\"String by matcher\"));",
                "",
                "For more info see javadoc for Matchers class."
        ));
    }

    public void argumentsAreDifferent(String wanted, String actual, Location actualLocation) {
        String message = join("Argument(s) are different! Wanted:",
                wanted,
                new Location(),
                "Actual invocation has different arguments:",
                actual,
                actualLocation,
                ""
                );

        if (JUnitTool.hasJUnit()) {
            throw JUnitTool.createArgumentsAreDifferentException(message, wanted, actual);
        } else {
            throw new ArgumentsAreDifferent(message);
        }
    }

    public void wantedButNotInvoked(PrintableInvocation wanted) {
        throw new WantedButNotInvoked(createWantedButNotInvokedMessage(wanted));
    }

    public void wantedButNotInvoked(PrintableInvocation wanted, List<? extends PrintableInvocation> invocations) {
        String allInvocations;
        if (invocations.isEmpty()) {
            allInvocations = "Actually, there were zero interactions with this mock.\n";
        } else {
            StringBuilder sb = new StringBuilder("\nHowever, there were other interactions with this mock:\n");
            for (PrintableInvocation i : invocations) {
                 sb.append(i.getLocation());
                 sb.append("\n");
            }
            allInvocations = sb.toString();
        }

        String message = createWantedButNotInvokedMessage(wanted);
        throw new WantedButNotInvoked(message + allInvocations);
    }

    private String createWantedButNotInvokedMessage(PrintableInvocation wanted) {
        return join(
                "Wanted but not invoked:",
                wanted.toString(),
                new Location(),
                ""
        );
    }

    public void wantedButNotInvokedInOrder(PrintableInvocation wanted, PrintableInvocation previous) {
        throw new VerificationInOrderFailure(join(
                    "Verification in order failure",
                    "Wanted but not invoked:",
                    wanted.toString(),
                    new Location(),
                    "Wanted anywhere AFTER following interaction:",
                    previous.toString(),
                    previous.getLocation(),
                    ""
        ));
    }

    public void tooManyActualInvocations(int wantedCount, int actualCount, PrintableInvocation wanted, Location firstUndesired) {
        String message = createTooManyInvocationsMessage(wantedCount, actualCount, wanted, firstUndesired);
        throw new TooManyActualInvocations(message);
    }

    private String createTooManyInvocationsMessage(int wantedCount, int actualCount, PrintableInvocation wanted,
            Location firstUndesired) {
        return join(
                wanted.toString(),
                "Wanted " + Pluralizer.pluralize(wantedCount) + ":",
                new Location(),
                "But was " + pluralize(actualCount) + ". Undesired invocation:",
                firstUndesired,
                ""
        );
    }

    public void neverWantedButInvoked(PrintableInvocation wanted, Location firstUndesired) {
        throw new NeverWantedButInvoked(join(
                wanted.toString(),
                "Never wanted here:",
                new Location(),
                "But invoked here:",
                firstUndesired,
                ""
        ));
    }

    public void tooManyActualInvocationsInOrder(int wantedCount, int actualCount, PrintableInvocation wanted, Location firstUndesired) {
        String message = createTooManyInvocationsMessage(wantedCount, actualCount, wanted, firstUndesired);
        throw new VerificationInOrderFailure(join(
                "Verification in order failure:" + message
                ));
    }

    private String createTooLittleInvocationsMessage(Discrepancy discrepancy, PrintableInvocation wanted,
            Location lastActualInvocation) {
        String ending =
            (lastActualInvocation != null)? lastActualInvocation + "\n" : "\n";

            String message = join(
                    wanted.toString(),
                    "Wanted " + discrepancy.getPluralizedWantedCount() + ":",
                    new Location(),
                    "But was " + discrepancy.getPluralizedActualCount() + ":",
                    ending
            );
            return message;
    }

    public void tooLittleActualInvocations(Discrepancy discrepancy, PrintableInvocation wanted, Location lastActualLocation) {
        String message = createTooLittleInvocationsMessage(discrepancy, wanted, lastActualLocation);

        throw new TooLittleActualInvocations(message);
    }

    public void tooLittleActualInvocationsInOrder(Discrepancy discrepancy, PrintableInvocation wanted, Location lastActualLocation) {
        String message = createTooLittleInvocationsMessage(discrepancy, wanted, lastActualLocation);

        throw new VerificationInOrderFailure(join(
                "Verification in order failure:" + message
                ));
    }

    public void noMoreInteractionsWanted(Invocation undesired, List<VerificationAwareInvocation> invocations) {
        ScenarioPrinter scenarioPrinter = new ScenarioPrinter();
        String scenario = scenarioPrinter.print(invocations);

        throw new NoInteractionsWanted(join(
                "No interactions wanted here:",
                new Location(),
                "But found this interaction:",
                undesired.getLocation(),
                scenario,
                ""
        ));
    }

    public void noMoreInteractionsWantedInOrder(Invocation undesired) {
        throw new VerificationInOrderFailure(join(
                "No interactions wanted here:",
                new Location(),
                "But found this interaction:",
                undesired.getLocation(),
                ""
                ));
    }

    public void cannotMockFinalClass(Class<?> clazz) {
        throw new MockitoException(join(
                "Cannot mock/spy " + clazz.toString(),
                "Mockito cannot mock/spy following:",
                "  - final classes",
                "  - anonymous classes",
                "  - primitive types"
        ));
    }

    public void cannotStubVoidMethodWithAReturnValue(String methodName) {
        throw new MockitoException(join(
                "'" + methodName + "' is a *void method* and it *cannot* be stubbed with a *return value*!",
                "Voids are usually stubbed with Throwables:",
                "    doThrow(exception).when(mock).someVoidMethod();",
                "If the method you are trying to stub is *overloaded* then make sure you are calling the right overloaded version.",
                "This exception might also occur when somewhere in your test you are stubbing *final methods*."
             ));
    }

    public void onlyVoidMethodsCanBeSetToDoNothing() {
        throw new MockitoException(join(
                "Only void methods can doNothing()!",
                "Example of correct use of doNothing():",
                "    doNothing().",
                "    doThrow(new RuntimeException())",
                "    .when(mock).someVoidMethod();",
                "Above means:",
                "someVoidMethod() does nothing the 1st time but throws an exception the 2nd time is called"
             ));
    }

    public void wrongTypeOfReturnValue(String expectedType, String actualType, String methodName) {
        throw new WrongTypeOfReturnValue(join(
                actualType + " cannot be returned by " + methodName + "()",
                methodName + "() should return " + expectedType,
                "***",
                "This exception *might* occur in wrongly written multi-threaded tests.",
                "Please refer to Mockito FAQ on limitations of concurrency testing.",
                ""
                ));
    }

    public void wantedAtMostX(int maxNumberOfInvocations, int foundSize) {
        throw new MockitoAssertionError(join("Wanted at most " + pluralize(maxNumberOfInvocations) + " but was " + foundSize));
    }

    public void misplacedArgumentMatcher(Location location) {
        throw new InvalidUseOfMatchersException(join(
                "Misplaced argument matcher detected here:",
                location,
                "",
                "You cannot use argument matchers outside of verification or stubbing.",
                "Examples of correct usage of argument matchers:",
                "    when(mock.get(anyInt())).thenReturn(null);",
                "    doThrow(new RuntimeException()).when(mock).someVoidMethod(anyObject());",
                "    verify(mock).someMethod(contains(\"foo\"))",
                "",
                "Also, this error might show up because you use argument matchers with methods that cannot be mocked.",
                "Following methods *cannot* be stubbed/verified: final/private/equals()/hashCode().",
                ""
                ));
    }

    public void smartNullPointerException(Object obj, Location location) {
        throw new SmartNullPointerException(join(
                "You have a NullPointerException here:",
                new Location(),
                obj,
                "Because this method was *not* stubbed correctly:",
                location,
                ""
                ));
    }

    public void noArgumentValueWasCaptured() {
        throw new MockitoException(join(
                "No argument value was captured!",
                "You might have forgotten to use argument.capture() in verify()...",
                "...or you used capture() in stubbing but stubbed method was not called.",
                "Be aware that it is recommended to use capture() only with verify()",
                "",
                "Examples of correct argument capturing:",
                "    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);",
                "    verify(mock).doSomething(argument.capture());",
                "    assertEquals(\"John\", argument.getValue().getName());",
                ""
                ));
    }

    public void extraInterfacesDoesNotAcceptNullParameters() {
        throw new MockitoException(join(
                "extraInterfaces() does not accept null parameters."
                ));
    }

    public void extraInterfacesAcceptsOnlyInterfaces(Class<?> wrongType) {
        throw new MockitoException(join(
                "extraInterfaces() accepts only interfaces.",
                "You passed following type: " + wrongType.getSimpleName() + " which is not an interface."
        ));
    }

    public void extraInterfacesCannotContainMockedType(Class<?> wrongType) {
        throw new MockitoException(join(
                "extraInterfaces() does not accept the same type as the mocked type.",
                "You mocked following type: " + wrongType.getSimpleName(),
                "and you passed the same very interface to the extraInterfaces()"
        ));
    }

    public void extraInterfacesRequiresAtLeastOneInterface() {
        throw new MockitoException(join(
                "extraInterfaces() requires at least one interface."
        ));
    }

    public void mockedTypeIsInconsistentWithSpiedInstanceType(Class<?> mockedType, Object spiedInstance) {
        throw new MockitoException(join(
                "Mocked type must be the same as the type of your spied instance.",
                "Mocked type must be: " + spiedInstance.getClass().getSimpleName() + ", but is: " + mockedType.getSimpleName(),
                "  //correct spying:",
                "  spy = mock( ->ArrayList.class<- , withSettings().spiedInstance( ->new ArrayList()<- );",
                "  //incorrect - types don't match:",
                "  spy = mock( ->List.class<- , withSettings().spiedInstance( ->new ArrayList()<- );"
        ));
    }

    public void cannotCallRealMethodOnInterface() {
        throw new MockitoException(join(
                "Cannot call real method on java interface. Interface does not have any implementation!",
                "Calling real methods is only possible when mocking concrete classes.",
                "  //correct example:",
                "  when(mockOfConcreteClass.doStuff()).thenCallRealMethod();"
        ));
    }

    public void cannotVerifyToString() {
        throw new MockitoException(join(
                "Mockito cannot verify toString()",
                "toString() is too often used behind of scenes  (i.e. during String concatenation, in IDE debugging views). " +
                        "Verifying it may give inconsistent or hard to understand results. " +
                        "Not to mention that verifying toString() most likely hints awkward design (hard to explain in a short exception message. Trust me...)",
                "However, it is possible to stub toString(). Stubbing toString() smells a bit funny but there are rare, legitimate use cases."
        ));
    }

    public void moreThanOneAnnotationNotAllowed(String fieldName) {
        throw new MockitoException("You cannot have more than one Mockito annotation on a field!\n" +
                "The field '" + fieldName + "' has multiple Mockito annotations.\n" +
                "For info how to use annotations see examples in javadoc for MockitoAnnotations class.");
    }

    public void unsupportedCombinationOfAnnotations(String undesiredAnnotationOne, String undesiredAnnotationTwo) {
        throw new MockitoException("This combination of annotations is not permitted on a single field:\n" +
                "@" + undesiredAnnotationOne + " and @" + undesiredAnnotationTwo);
    }

    public void cannotInitializeForSpyAnnotation(String fieldName, Exception details) {
        throw new MockitoException(join("Cannot instianate a @Spy for '" + fieldName + "' field.",
            "You haven't provided the instance for spying at field declaration so I tried to construct the instance.",
            "However, I failed because: " + details.getMessage(),
            "Examples of correct usage of @Spy:",
            "   @Spy List mock = new LinkedList();",
            "   @Spy Foo foo; //only if Foo has parameterless constructor",
            "   //also, don't forget about MockitoAnnotations.initMocks();",
                ""), details);
    }

    public void cannotInitializeForInjectMocksAnnotation(String fieldName, Exception details) {
        throw new MockitoException(join("Cannot instianate @InjectMocks field named '" + fieldName + "'.",
            "You haven't provided the instance for spying at field declaration so I tried to construct the instance.",
            "However, I failed because: " + details.getMessage(),
            "Examples of correct usage of @InjectMocks:",
            "   @InjectMocks Service service = new Service();",
            "   @InjectMocks Service service; //only if Service has parameterless constructor",
            "   //also, don't forget about MockitoAnnotations.initMocks();",
            "   //and... don't forget about some @Mocks for injection :)",
                ""), details);
    }
}