/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

/**
 * Behavior Driven Development style of writing tests uses <b>//given //when //then</b> comments as fundamental parts of your test methods.
 * This is exactly how we write our tests and we warmly encourage you to do so!
 * <p>
 * Start learning about BDD here: <a href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">http://en.wikipedia.org/wiki/Behavior_Driven_Development</a>
 * <p>
 * The problem is that current stubbing api with canonical role of <b>when</b> word does not integrate nicely with <b>//given //when //then</b> comments.
 * It's because stubbing belongs to <b>given</b> component of the test and not to the <b>when</b> component of the test.
 * Hence {@link BDDMockito} class introduces an alias so that you stub method calls with {@link BDDMockito#given(Object)} method.
 * Now it really nicely integrates with the <b>given</b> component of a BDD style test!
 * <p>
 * Here is how the test might look like:
 * <pre class="code"><code class="java">
 * import static org.mockito.BDDMockito.*;
 *
 * Seller seller = mock(Seller.class);
 * Shop shop = new Shop(seller);
 *
 * public void shouldBuyBread() throws Exception {
 *   //given
 *   given(seller.askForBread()).willReturn(new Bread());
 *
 *   //when
 *   Goods goods = shop.buyBread();
 *
 *   //then
 *   assertThat(goods, containBread());
 * }
 * </code></pre>
 *
 * Stubbing voids with throwables:
 * <pre class="code"><code class="java">
 *   //given
 *   willThrow(new RuntimeException("boo")).given(mock).foo();
 *
 *   //when
 *   Result result = systemUnderTest.perform();
 *
 *   //then
 *   assertEquals(failure, result);
 * </code></pre>
 * <p>
 * For BDD style mock verification take a look at {@link Then} in action:
 * <pre class="code"><code class="java">
 *   person.ride(bike);
 *   person.ride(bike);
 *
 *   then(person).should(times(2)).ride(bike);
 * </code></pre>
 *
 * One of the purposes of BDDMockito is also to show how to tailor the mocking syntax to a different programming style.
 *
 * @since 1.8.0
 */
@SuppressWarnings("unchecked")
public class BDDMockito extends Mockito {

    /**
     * See original {@link OngoingStubbing}
     * @since 1.8.0
     */
    public interface BDDMyOngoingStubbing<T> {

        /**
         * See original {@link OngoingStubbing#thenAnswer(Answer)}
         * @since 1.8.0
         */
        BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer);

        /**
         * See original {@link OngoingStubbing#then(Answer)}
         * @since 1.9.0
         */
        BDDMyOngoingStubbing<T> will(Answer<?> answer);

        /**
         * See original {@link OngoingStubbing#thenReturn(Object)}
         * @since 1.8.0
         */
        BDDMyOngoingStubbing<T> willReturn(T value);

        /**
         * See original {@link OngoingStubbing#thenReturn(Object, Object[])}
         * @since 1.8.0
         */
        BDDMyOngoingStubbing<T> willReturn(T value, T... values);

        /**
         * See original {@link OngoingStubbing#thenThrow(Throwable...)}
         * @since 1.8.0
         */
        BDDMyOngoingStubbing<T> willThrow(Throwable... throwables);

        /**
         * See original {@link OngoingStubbing#thenThrow(Class[])}
         * @since 1.9.0
         */
        BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable>... throwableClasses);

        /**
         * See original {@link OngoingStubbing#thenCallRealMethod()}
         * @since 1.9.0
         */
        BDDMyOngoingStubbing<T> willCallRealMethod();

        /**
         * See original {@link OngoingStubbing#getMock()}
         * @since 1.9.0
         */
        <M> M getMock();
    }

    /**
     * @deprecated not part of the public API, use {@link BDDMyOngoingStubbing} instead.
     */
    @Deprecated
    public static class BDDOngoingStubbingImpl<T> implements BDDMyOngoingStubbing<T> {

        private final OngoingStubbing<T> mockitoOngoingStubbing;

        public BDDOngoingStubbingImpl(OngoingStubbing<T> ongoingStubbing) {
            this.mockitoOngoingStubbing = ongoingStubbing;
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#willAnswer(Answer)
         */
        public BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenAnswer(answer));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#will(Answer)
         */
        public BDDMyOngoingStubbing<T> will(Answer<?> answer) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.then(answer));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#willReturn(java.lang.Object)
         */
        public BDDMyOngoingStubbing<T> willReturn(T value) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#willReturn(java.lang.Object, T[])
         */
        public BDDMyOngoingStubbing<T> willReturn(T value, T... values) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value, values));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#willThrow(java.lang.Throwable[])
         */
        public BDDMyOngoingStubbing<T> willThrow(Throwable... throwables) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenThrow(throwables));
        }
        /* (non-Javadoc)
         * @see BDDMockito.BDDMyOngoingStubbing#willThrow(java.lang.Class[])
         */
        public BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable>... throwableClasses) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenThrow(throwableClasses));
        }

        public BDDMyOngoingStubbing<T> willCallRealMethod() {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenCallRealMethod());
        }

        public <M> M getMock() {
            return (M) mockitoOngoingStubbing.getMock();
        }
    }

    /**
     * see original {@link Mockito#when(Object)}
     * @since 1.8.0
     */
    public static <T> BDDMyOngoingStubbing<T> given(T methodCall) {
        return new BDDOngoingStubbingImpl<T>(Mockito.when(methodCall));
    }

    /**
     * Bdd style verification of mock behavior.
     *
     * <pre class="code"><code class="java">
     *   person.ride(bike);
     *   person.ride(bike);
     *
     *   then(person).should(times(2)).ride(bike);
     * </code></pre>
     *
     * @see #verify(Object)
     * @see #verify(Object, VerificationMode)
     * @since 1.10.0
     */
    public static <T> Then<T> then(T mock) {
        return new ThenImpl<T>(mock);
    }

    /**
     * Provides fluent way of mock verification.
     *
     * @param <T> type of the mock
     *
     * @since 1.10.5
     */
    public static interface Then<T> {

        /**
         * @see #verify(Object)
         * @since 1.10.5
         */
        T should();

        /**
         * @see #verify(Object, VerificationMode)
         * @since 1.10.5
         */
        T should(VerificationMode mode);
    }

    static class ThenImpl<T> implements Then<T> {

        private final T mock;

        ThenImpl(T mock) {
            this.mock = mock;
        }

        /**
         * @see #verify(Object)
         * @since 1.10.5
         */
        public T should() {
            return verify(mock);
        }

        /**
         * @see #verify(Object, VerificationMode)
         * @since 1.10.5
         */
        public T should(VerificationMode mode) {
            return verify(mock, mode);
        }
    }

    /**
     * See original {@link Stubber}
     * @since 1.8.0
     */
    public interface BDDStubber {
        /**
         * See original {@link Stubber#doAnswer(Answer)}
         * @since 1.8.0
         */
        BDDStubber willAnswer(Answer answer);

        /**
         * See original {@link Stubber#doNothing()}
         * @since 1.8.0
         */
        BDDStubber willNothing();

        /**
         * See original {@link Stubber#doReturn(Object)}
         * @since 1.8.0
         */
        BDDStubber willReturn(Object toBeReturned);

        /**
         * See original {@link Stubber#doThrow(Throwable)}
         * @since 1.8.0
         */
        BDDStubber willThrow(Throwable toBeThrown);

        /**
         * See original {@link Stubber#doThrow(Class)}
         * @since 1.9.0
         */
        BDDStubber willThrow(Class<? extends Throwable> toBeThrown);

        /**
         * See original {@link Stubber#doCallRealMethod()}
         * @since 1.9.0
         */
        BDDStubber willCallRealMethod();

        /**
         * See original {@link Stubber#when(Object)}
         * @since 1.8.0
         */
        <T> T given(T mock);
    }

    /**
     * @deprecated not part of the public API, use {@link BDDStubber} instead.
     */
    @Deprecated
    public static class BDDStubberImpl implements BDDStubber {

        private final Stubber mockitoStubber;

        public BDDStubberImpl(Stubber mockitoStubber) {
            this.mockitoStubber = mockitoStubber;
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#given(java.lang.Object)
         */
        public <T> T given(T mock) {
            return mockitoStubber.when(mock);
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willAnswer(Answer)
         */
        public BDDStubber willAnswer(Answer answer) {
            return new BDDStubberImpl(mockitoStubber.doAnswer(answer));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willNothing()
         */
        public BDDStubber willNothing() {
            return new BDDStubberImpl(mockitoStubber.doNothing());
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willReturn(java.lang.Object)
         */
        public BDDStubber willReturn(Object toBeReturned) {
            return new BDDStubberImpl(mockitoStubber.doReturn(toBeReturned));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willThrow(java.lang.Throwable)
         */
        public BDDStubber willThrow(Throwable toBeThrown) {
            return new BDDStubberImpl(mockitoStubber.doThrow(toBeThrown));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willThrow(Class)
         */
        public BDDStubber willThrow(Class<? extends Throwable> toBeThrown) {
            return new BDDStubberImpl(mockitoStubber.doThrow(toBeThrown));
        }

        /* (non-Javadoc)
         * @see BDDMockito.BDDStubber#willCallRealMethod()
         */
        public BDDStubber willCallRealMethod() {
            return new BDDStubberImpl(mockitoStubber.doCallRealMethod());
        }
    }

    /**
     * see original {@link Mockito#doThrow(Throwable)}
     * @since 1.8.0
     */
    public static BDDStubber willThrow(Throwable toBeThrown) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown));
    }

    /**
     * see original {@link Mockito#doThrow(Throwable)}
     * @since 1.9.0
     */
    public static BDDStubber willThrow(Class<? extends Throwable> toBeThrown) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown));
    }

    /**
     * see original {@link Mockito#doAnswer(Answer)}
     * @since 1.8.0
     */
    public static BDDStubber willAnswer(Answer answer) {
        return new BDDStubberImpl(Mockito.doAnswer(answer));
    }

    /**
     * see original {@link Mockito#doNothing()}
     * @since 1.8.0
     */
    public static BDDStubber willDoNothing() {
        return new BDDStubberImpl(Mockito.doNothing());
    }

    /**
     * see original {@link Mockito#doReturn(Object)}
     * @since 1.8.0
     */
    public static BDDStubber willReturn(Object toBeReturned) {
        return new BDDStubberImpl(Mockito.doReturn(toBeReturned));
    }

    /**
     * see original {@link Mockito#doCallRealMethod()}
     * @since 1.8.0
     */
    public static BDDStubber willCallRealMethod() {
        return new BDDStubberImpl(Mockito.doCallRealMethod());
    }
}
