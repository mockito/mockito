/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.Incubating;

/**
 * This listener gets notified when the user starts verification.
 * It allows to replace the mock object for verification.
 * This API is not needed for regular Mockito users who want to write beautiful and clean tests.
 * It is only needed for advanced framework integrations where there are multiple layers of proxying.
 * An example framework that leverages this API is <a href="https://projects.spring.io/spring-boot/">Spring Boot</a>.
 * For details about the use case see <a href="https://github.com/mockito/mockito/issues/1191">issue 1191</a>.
 * For sample code see {@code VerificationStartedListenerTest} class.
 * Mockito is Open Source so feel free to dive into the code!
 * <p>
 * How can you add listeners?
 * The listener is attached to the mock object during creation:
 * <pre class="code"><code class="java">
 *     List mock = Mockito.mock(List.class, withSettings().verificationStartedListeners(myListener));
 * </pre>
 * When multiple listeners are added, they are notified in order.
 * There is no reason to add multiple listeners but we wanted to keep the API simple and consistent with how we manage Mock object listeners.
 * See {@link org.mockito.MockSettings#verificationStartedListeners(VerificationStartedListener...)}.
 * <p>
 * When is the listener notified?
 * <pre class="code"><code class="java">
 *     //given verification:
 *     verify(mock).someMethod();
 *
 *     //let's slit it into 2 distinct steps so that it is easy to explain:
 *
 *     //step 1
 *     verify(mock);
 *
 *     //step 2
 *     mock.someMethod();
 *
 *     //the listener is notified during step 1
 *     //step 2 is when Mockito attempts to verify the method call
 * </pre>
 * <p>
 * What can I do when the listener is notified?
 * The main reason we added this listener to the API is to allow to replace the mock object that is about to be verified.
 * This is a pretty hardcore use case, needed by other frameworks that wrap Mockito with another layer of proxying.
 * Such framework may need to unwrap the outer proxy layer and pass genuine Mockito mock to the verification.
 * For specific use case how it is needed by Spring Boot, see <a href="https://github.com/mockito/mockito/issues/1191">issue 1191</a>.
 * <p>
 * When do I use the listener?
 * Unless you write a framework that integrates with Mockito, there is no reason for you to use this API.
 * Keep mocking and writing great unit tests!
 *
 * @since 2.11.0
 */
@Incubating
public interface VerificationStartedListener {

    /**
     * Triggered when the user calls {@code Mockito.verify()}.
     * For details see {@link VerificationStartedListener}.
     *
     * @param event object that allows to identify and replace mock for verification.
     * @since 2.11.0
     */
    @Incubating
    void onVerificationStarted(VerificationStartedEvent event);
}
