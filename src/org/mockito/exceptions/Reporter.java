/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.exceptions;

import static org.mockito.internal.reporting.Pluralizer.pluralize;
import static org.mockito.internal.util.StringJoiner.join;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.*;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.exceptions.util.ScenarioPrinter;
import org.mockito.internal.junit.JUnitTool;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.listeners.InvocationListener;

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
                " 3: you are stubbing the behaviour of another mock inside before 'thenReturn' instruction if completed",
                ""
        ));
    }

    public void incorrectUseOfApi() {
        throw new MockitoException(join(
                "Incorrect use of API detected here:",
                new LocationImpl(),
                "",
                "You probably stored a reference to OngoingStubbing returned by when() and called stubbing methods like thenReturn() on this reference more than once.",
                "Examples of correct usage:",
                "    when(mock.isOk()).thenReturn(true).thenReturn(false).thenThrow(exception);",
                "    when(mock.isOk()).thenReturn(true, false).thenThrow(exception);",
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
                "2. inside when() you don't call method on mock but on some other object.",
                "3. the parent of the mocked class is not public.",
                "   It is a limitation of the mock engine.",
                ""
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
                "    not: verify(mock.someMethod());",
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
                "    verifyZeroInteractions(mockOne, mockTwo);",
                ""
        ));
    }

    public void notAMockPassedToVerifyNoMoreInteractions() {
        throw new NotAMockException(join(
                "Argument(s) passed is not a mock!",
                "Examples of correct verifications:",
                "    verifyNoMoreInteractions(mockOne, mockTwo);",
                "    verifyZeroInteractions(mockOne, mockTwo);",
                ""
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

    public void invalidUseOfMatchers(int expectedMatchersCount, List<LocalizedMatcher> recordedMatchers) {
        throw new InvalidUseOfMatchersException(join(
                "Invalid use of argument matchers!",
                expectedMatchersCount + " matchers expected, " + recordedMatchers.size()+ " recorded:" +
                        locationsOf(recordedMatchers),
                "",
                "This exception may occur if matchers are combined with raw values:",
                "    //incorrect:",
                "    someMethod(anyObject(), \"raw String\");",
                "When using matchers, all arguments have to be provided by matchers.",
                "For example:",
                "    //correct:",
                "    someMethod(anyObject(), eq(\"String by matcher\"));",
                "",
                "For more info see javadoc for Matchers class.",
                ""
        ));
    }

    public void incorrectUseOfAdditionalMatchers(String additionalMatcherName, int expectedSubMatchersCount, Collection<LocalizedMatcher> matcherStack) {
        throw new InvalidUseOfMatchersException(join(
                "Invalid use of argument matchers inside additional matcher " + additionalMatcherName + " !",
                new LocationImpl(),
                "",
                expectedSubMatchersCount + " sub matchers expected, " + matcherStack.size() + " recorded:",
                locationsOf(matcherStack),
                "",
                "This exception may occur if matchers are combined with raw values:",
                "    //incorrect:",
                "    someMethod(AdditionalMatchers.and(isNotNull(), \"raw String\");",
                "When using matchers, all arguments have to be provided by matchers.",
                "For example:",
                "    //correct:",
                "    someMethod(AdditionalMatchers.and(isNotNull(), eq(\"raw String\"));",
                "",
                "For more info see javadoc for Matchers and AdditionalMatchers classes.",
                ""
        ));
    }

    public void stubPassedToVerify() {
        throw new CannotVerifyStubOnlyMock(join(
                "Argument passed to verify() is a stubOnly() mock, not a full blown mock!",
                "If you intend to verify invocations on a mock, don't use stubOnly() in its MockSettings."
        ));
    }

    public void reportNoSubMatchersFound(String additionalMatcherName) {
        throw new InvalidUseOfMatchersException(join(
                "No matchers found for additional matcher " + additionalMatcherName,
                new LocationImpl(),
                ""
        ));
    }


    private Object locationsOf(Collection<LocalizedMatcher> matchers) {
        List<String> description = new ArrayList<String>();
        for (LocalizedMatcher matcher : matchers)
            description.add(matcher.getLocation().toString());
        return join(description.toArray());
    }

    public void argumentsAreDifferent(String wanted, String actual, Location actualLocation) {
        String message = join("Argument(s) are different! Wanted:",
                wanted,
                new LocationImpl(),
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

    public void wantedButNotInvoked(DescribedInvocation wanted) {
        throw new WantedButNotInvoked(createWantedButNotInvokedMessage(wanted));
    }

    public void wantedButNotInvoked(DescribedInvocation wanted, List<? extends DescribedInvocation> invocations) {
        String allInvocations;
        if (invocations.isEmpty()) {
            allInvocations = "Actually, there were zero interactions with this mock.\n";
        } else {
            StringBuilder sb = new StringBuilder("\nHowever, there were other interactions with this mock:\n");
            for (DescribedInvocation i : invocations) {
                sb.append(i.toString())
                        .append("\n")
                        .append(i.getLocation())
                        .append("\n\n");
            }
            allInvocations = sb.toString();
        }

        String message = createWantedButNotInvokedMessage(wanted);
        throw new WantedButNotInvoked(message + allInvocations);
    }

    private String createWantedButNotInvokedMessage(DescribedInvocation wanted) {
        return join(
                "Wanted but not invoked:",
                wanted.toString(),
                new LocationImpl(),
                ""
        );
    }

    public void wantedButNotInvokedInOrder(DescribedInvocation wanted, DescribedInvocation previous) {
        throw new VerificationInOrderFailure(join(
                "Verification in order failure",
                "Wanted but not invoked:",
                wanted.toString(),
                new LocationImpl(),
                "Wanted anywhere AFTER following interaction:",
                previous.toString(),
                previous.getLocation(),
                ""
        ));
    }

    public void tooManyActualInvocations(int wantedCount, int actualCount, DescribedInvocation wanted, Location firstUndesired) {
        String message = createTooManyInvocationsMessage(wantedCount, actualCount, wanted, firstUndesired);
        throw new TooManyActualInvocations(message);
    }

    private String createTooManyInvocationsMessage(int wantedCount, int actualCount, DescribedInvocation wanted,
                                                   Location firstUndesired) {
        return join(
                wanted.toString(),
                "Wanted " + pluralize(wantedCount) + ":",
                new LocationImpl(),
                "But was " + pluralize(actualCount) + ". Undesired invocation:",
                firstUndesired,
                ""
        );
    }

    public void neverWantedButInvoked(DescribedInvocation wanted, Location firstUndesired) {
        throw new NeverWantedButInvoked(join(
                wanted.toString(),
                "Never wanted here:",
                new LocationImpl(),
                "But invoked here:",
                firstUndesired,
                ""
        ));
    }

    public void tooManyActualInvocationsInOrder(int wantedCount, int actualCount, DescribedInvocation wanted, Location firstUndesired) {
        String message = createTooManyInvocationsMessage(wantedCount, actualCount, wanted, firstUndesired);
        throw new VerificationInOrderFailure(join(
                "Verification in order failure:" + message
        ));
    }

    private String createTooLittleInvocationsMessage(org.mockito.internal.reporting.Discrepancy discrepancy, DescribedInvocation wanted,
                                                     Location lastActualInvocation) {
        String ending =
                (lastActualInvocation != null)? lastActualInvocation + "\n" : "\n";

        String message = join(
                wanted.toString(),
                "Wanted " + discrepancy.getPluralizedWantedCount() + ":",
                new LocationImpl(),
                "But was " + discrepancy.getPluralizedActualCount() + ":",
                ending
        );
        return message;
    }

    public void tooLittleActualInvocations(org.mockito.internal.reporting.Discrepancy discrepancy, DescribedInvocation wanted, Location lastActualLocation) {
        String message = createTooLittleInvocationsMessage(discrepancy, wanted, lastActualLocation);

        throw new TooLittleActualInvocations(message);
    }

    public void tooLittleActualInvocationsInOrder(org.mockito.internal.reporting.Discrepancy discrepancy, DescribedInvocation wanted, Location lastActualLocation) {
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
                new LocationImpl(),
                "But found this interaction:",
                undesired.getLocation(),
                scenario
        ));
    }

    public void noMoreInteractionsWantedInOrder(Invocation undesired) {
        throw new VerificationInOrderFailure(join(
                "No interactions wanted here:",
                new LocationImpl(),
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
                "***",
                "If you're unsure why you're getting above error read on.",
                "Due to the nature of the syntax above problem might occur because:",
                "1. The method you are trying to stub is *overloaded*. Make sure you are calling the right overloaded version.",
                "2. Somewhere in your test you are stubbing *final methods*. Sorry, Mockito does not verify/stub final methods.",
                "3. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies - ",
                "   - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.",
                ""
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
                "If you're unsure why you're getting above error read on.",
                "Due to the nature of the syntax above problem might occur because:",
                "1. This exception *might* occur in wrongly written multi-threaded tests.",
                "   Please refer to Mockito FAQ on limitations of concurrency testing.",
                "2. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies - ",
                "   - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.",
                ""
        ));
    }

    public void wantedAtMostX(int maxNumberOfInvocations, int foundSize) {
        throw new MockitoAssertionError(join("Wanted at most " + pluralize(maxNumberOfInvocations) + " but was " + foundSize));
    }

    public void misplacedArgumentMatcher(List<LocalizedMatcher> lastMatchers) {
        throw new InvalidUseOfMatchersException(join(
                "Misplaced argument matcher detected here:",
                locationsOf(lastMatchers),
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

    public void smartNullPointerException(String invocation, Location location) {
        throw new SmartNullPointerException(join(
                "You have a NullPointerException here:",
                new LocationImpl(),
                "because this method call was *not* stubbed correctly:",
                location,
                invocation,
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
        throw new MockitoException(join("Cannot instantiate a @Spy for '" + fieldName + "' field.",
                "You haven't provided the instance for spying at field declaration so I tried to construct the instance.",
                "However, I failed because: " + details.getMessage(),
                "Examples of correct usage of @Spy:",
                "   @Spy List mock = new LinkedList();",
                "   @Spy Foo foo; //only if Foo has parameterless constructor",
                "   //also, don't forget about MockitoAnnotations.initMocks();",
                ""), details);
    }

    public void cannotInitializeForInjectMocksAnnotation(String fieldName, Exception details) {
        throw new MockitoException(join("Cannot instantiate @InjectMocks field named '" + fieldName + "'.",
                "You haven't provided the instance at field declaration so I tried to construct the instance.",
                "However, I failed because: " + details.getMessage(),
                "Examples of correct usage of @InjectMocks:",
                "   @InjectMocks Service service = new Service();",
                "   @InjectMocks Service service;",
                "   //also, don't forget about MockitoAnnotations.initMocks();",
                "   //and... don't forget about some @Mocks for injection :)",
                ""), details);
    }

    public void atMostAndNeverShouldNotBeUsedWithTimeout() {
        throw new FriendlyReminderException(join("",
                "Don't panic! I'm just a friendly reminder!",
                "timeout() should not be used with atMost() or never() because...",
                "...it does not make much sense - the test would have passed immediately in concurency",
                "We kept this method only to avoid compilation errors when upgrading Mockito.",
                "In future release we will remove timeout(x).atMost(y) from the API.",
                "If you want to find out more please refer to issue 235",
                ""));
    }

    public void fieldInitialisationThrewException(Field field, Throwable details) {
        throw new MockitoException(join(
                "Cannot instantiate @InjectMocks field named '" + field.getName() + "' of type '" + field.getType() +  "'.",
                "You haven't provided the instance at field declaration so I tried to construct the instance.",
                "However the constructor or the initialization block threw an exception : " + details.getMessage(),
                ""), details);

    }

    public void invocationListenerDoesNotAcceptNullParameters() {
        throw new MockitoException("invocationListeners() does not accept null parameters");
    }

    public void invocationListenersRequiresAtLeastOneListener() {
        throw new MockitoException("invocationListeners() requires at least one listener");
    }

    public void invocationListenerThrewException(InvocationListener listener, Throwable listenerThrowable) {
        throw new MockitoException(StringJoiner.join(
                "The invocation listener with type " + listener.getClass().getName(),
                "threw an exception : " + listenerThrowable.getClass().getName() + listenerThrowable.getMessage()), listenerThrowable);
    }

    public void cannotInjectDependency(Field field, Object matchingMock, Exception details) {
        throw new MockitoException(join(
                "Mockito couldn't inject mock dependency '" + new MockUtil().getMockName(matchingMock) + "' on field ",
                "'" + field + "'",
                "whose type '" + field.getDeclaringClass().getCanonicalName() + "' was annotated by @InjectMocks in your test.",
                "Also I failed because: " + details.getCause().getMessage(),
                ""
        ), details);
    }

    public void mockedTypeIsInconsistentWithDelegatedInstanceType(Class mockedType, Object delegatedInstance) {
        throw new MockitoException(join(
                "Mocked type must be the same as the type of your delegated instance.",
                "Mocked type must be: " + delegatedInstance.getClass().getSimpleName() + ", but is: " + mockedType.getSimpleName(),
                "  //correct delegate:",
                "  spy = mock( ->List.class<- , withSettings().delegatedInstance( ->new ArrayList()<- );",
                "  //incorrect - types don't match:",
                "  spy = mock( ->List.class<- , withSettings().delegatedInstance( ->new HashSet()<- );"
        ));
    }

    public void spyAndDelegateAreMutuallyExclusive() {
        throw new MockitoException(join(
                "Settings should not define a spy instance and a delegated instance at the same time."
        )) ;
    }

    public void invalidArgumentRangeAtIdentityAnswerCreationTime() {
        throw new MockitoException(join("Invalid argument index.",
                "The index need to be a positive number that indicates the position of the argument to return.",
                "However it is possible to use the -1 value to indicates that the last argument should be",
                "returned."));
    }

    public int invalidArgumentPositionRangeAtInvocationTime(InvocationOnMock invocation, boolean willReturnLastParameter, int argumentIndex) {
        throw new MockitoException(
                join("Invalid argument index for the current invocation of method : ",
                        " -> " + new MockUtil().getMockName(invocation.getMock()) + "." + invocation.getMethod().getName() + "()",
                        "",
                        (willReturnLastParameter ?
                                "Last parameter wanted" :
                                "Wanted parameter at position " + argumentIndex) + " but " + possibleArgumentTypesOf(invocation),
                        "The index need to be a positive number that indicates a valid position of the argument in the invocation.",
                        "However it is possible to use the -1 value to indicates that the last argument should be returned.",
                        ""));
    }

    private StringBuilder possibleArgumentTypesOf(InvocationOnMock invocation) {
        Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
        if (parameterTypes.length == 0) {
            return new StringBuilder("the method has no arguments.\n");
        }

        StringBuilder stringBuilder = new StringBuilder("the possible argument indexes for this method are :\n");
        for (int i = 0, parameterTypesLength = parameterTypes.length; i < parameterTypesLength; i++) {
            stringBuilder.append("    [").append(i);

            if (invocation.getMethod().isVarArgs() && i == parameterTypesLength - 1) {
                stringBuilder.append("+] ").append(parameterTypes[i].getComponentType().getSimpleName()).append("  <- Vararg").append("\n");
            } else {
                stringBuilder.append("] ").append(parameterTypes[i].getSimpleName()).append("\n");
            }
        }
        return stringBuilder;
    }

    public void wrongTypeOfArgumentToReturn(InvocationOnMock invocation, String expectedType, Class actualType, int argumentIndex) {
        throw new WrongTypeOfReturnValue(join(
                "The argument of type '" + actualType.getSimpleName() + "' cannot be returned because the following ",
                "method should return the type '" + expectedType + "'",
                " -> " + new MockUtil().getMockName(invocation.getMock()) + "." + invocation.getMethod().getName() + "()",
                "",
                "The reason for this error can be :",
                "1. The wanted argument position is incorrect.",
                "2. The answer is used on the wrong interaction.",
                "",
                "Position of the wanted argument is " + argumentIndex + " and " + possibleArgumentTypesOf(invocation),
                "***",
                "However if you're still unsure why you're getting above error read on.",
                "Due to the nature of the syntax above problem might occur because:",
                "1. This exception *might* occur in wrongly written multi-threaded tests.",
                "   Please refer to Mockito FAQ on limitations of concurrency testing.",
                "2. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies - ",
                "   - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.",
                ""
        ));
    }

    public void defaultAnswerDoesNotAcceptNullParameter() {
        throw new MockitoException("defaultAnswer() does not accept null parameter");
    }

    public void serializableWontWorkForObjectsThatDontImplementSerializable(Class classToMock) {
        throw new MockitoException(join(
                "You are using the setting 'withSettings().serializable()' however the type you are trying to mock '" + classToMock.getSimpleName() + "'",
                "do not implement Serializable AND do not have a no-arg constructor.",
                "This combination is requested, otherwise you will get an 'java.io.InvalidClassException' when the mock will be serialized",
                "",
                "Also note that as requested by the Java serialization specification, the whole hierarchy need to implements Serializable,",
                "i.e. the top-most superclass has to implements Serializable.",
                ""
        ));
    }
}
