/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.stubbing.Answer;

/**
 * Behavior Driven Development style of stubbing that integrates nicely with //given //when //then comments.
 * Start learning about BDD here: <link href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">http://en.wikipedia.org/wiki/Behavior_Driven_Development</link>
 * <p>
 * The entire test can look like:  
 * <pre>
 * public void shouldBuyBread() throws Exception {
 *   //given
 *   given(seller.askForBread()).willReturn(new Bread());
 *   
 *   //when
 *   Goods goods = shopping.shopForBread();
 *   
 *   //then
 *   assertThat(goods, containBread());
 * }  
 * </pre>
 * 
 * Stubbing voids with throwables:
 * <pre>
 *   //given
 *   willThrow(new RuntimeException("boo")).given(mock).foo();
 *   
 *   //when
 *   Result result = systemUnderTest.perform();
 *   
 *   //then
 *   assertEquals(failure, result);
 * </pre>
 * <p>
 * BDDMockito also shows how you can adjust the mocking syntax if you feel like 
 */
@SuppressWarnings("unchecked")
public class BDDMockito extends Mockito {
    
    /**
     * See original {@link NewOngoingStubbing}
     */
    public static interface BDDMyOngoingStubbing<T> {
        
        /**
         * See original {@link NewOngoingStubbing#thenAnswer(Answer)}
         */
        BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer);
        
        /**
         * See original {@link NewOngoingStubbing#thenReturn(Object)}
         */
        BDDMyOngoingStubbing<T> willReturn(T value);
        
        /**
         * See original {@link NewOngoingStubbing#thenReturn(Object, Object...)}
         */
        BDDMyOngoingStubbing<T> willReturn(T value, T... values);
        
        /**
         * See original {@link NewOngoingStubbing#thenThrow(Throwable...)}
         */
        BDDMyOngoingStubbing<T> willThrow(Throwable... throwables);

        /**
         * See original {@link NewOngoingStubbing#thenCallRealMethod()}
         */
        BDDMyOngoingStubbing<T> willCallRealMethod();
    }
    
    public static class BDDOngoingStubbingImpl<T> implements BDDMyOngoingStubbing<T> {

        private final NewOngoingStubbing<T> mockitoOngoingStubbing;

        public BDDOngoingStubbingImpl(NewOngoingStubbing<T> ongoingStubbing) {
            this.mockitoOngoingStubbing = ongoingStubbing;
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDMyOngoingStubbing#willAnswer(org.mockito.stubbing.Answer)
         */
        public BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenAnswer(answer));
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDMyOngoingStubbing#willReturn(java.lang.Object)
         */
        public BDDMyOngoingStubbing<T> willReturn(T value) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value));
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDMyOngoingStubbing#willReturn(java.lang.Object, T[])
         */
        public BDDMyOngoingStubbing<T> willReturn(T value, T... values) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenReturn(value, values));
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDMyOngoingStubbing#willThrow(java.lang.Throwable[])
         */
        public BDDMyOngoingStubbing<T> willThrow(Throwable... throwables) {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenThrow(throwables));
        }

        public BDDMyOngoingStubbing<T> willCallRealMethod() {
            return new BDDOngoingStubbingImpl<T>(mockitoOngoingStubbing.thenCallRealMethod());
        }
    }
    
    /**
     * see original {@link Mockito#when(Object)}
     */
    public static <T> BDDMyOngoingStubbing<T> given(T methodCall) {
        return new BDDOngoingStubbingImpl<T>(Mockito.when(methodCall));
    }
    
    /**
     * See original {@link Stubber}
     */
    public static interface BDDStubber {
        /**
         * See original {@link Stubber#doAnswer(Answer)}
         */
        BDDStubber willAnswer(Answer answer);
        
        /**
         * See original {@link Stubber#doNothing()}
         */
        BDDStubber willNothing();
        
        /**
         * See original {@link Stubber#doReturn(Object)}
         */
        BDDStubber willReturn(Object toBeReturned);
        
        /**
         * See original {@link Stubber#doThrow(Throwable)}
         */
        BDDStubber willThrow(Throwable toBeThrown);
        
        /**
         * See original {@link Stubber#when(Object)}
         */
        <T> T given(T mock);
    }
    
    public static class BDDStubberImpl implements BDDStubber {

        private final Stubber mockitoStubber;

        public BDDStubberImpl(Stubber mockitoStubber) {
            this.mockitoStubber = mockitoStubber;
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDStubber#given(java.lang.Object)
         */
        public <T> T given(T mock) {
            return mockitoStubber.when(mock);
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDStubber#willAnswer(org.mockito.stubbing.Answer)
         */
        public BDDStubber willAnswer(Answer answer) {
            return new BDDStubberImpl(mockitoStubber.doAnswer(answer));
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDStubber#willNothing()
         */
        public BDDStubber willNothing() {
            return new BDDStubberImpl(mockitoStubber.doNothing());
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDStubber#willReturn(java.lang.Object)
         */
        public BDDStubber willReturn(Object toBeReturned) {
            return new BDDStubberImpl(mockitoStubber.doReturn(toBeReturned));
        }

        /* (non-Javadoc)
         * @see org.mockitousage.customization.BDDMockito.BDDStubber#willThrow(java.lang.Throwable)
         */
        public BDDStubber willThrow(Throwable toBeThrown) {
            return new BDDStubberImpl(mockitoStubber.doThrow(toBeThrown));
        }
    }
    
    /**
     * see original {@link Mockito#doThrow(Throwable)}
     */
    public static BDDStubber willThrow(Throwable toBeThrown) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown));
    }
    
    /**
     * see original {@link Mockito#doAnswer(Answer)}
     */
    public static BDDStubber willAnswer(Answer answer) {
        return new BDDStubberImpl(Mockito.doAnswer(answer));
    }  
    
    /**
     * see original {@link Mockito#doNothing()}
     */
    public static BDDStubber willDoNothing() {
        return new BDDStubberImpl(Mockito.doNothing());
    }    
    
    /**
     * see original {@link Mockito#doReturn(Object)}
     */
    public static BDDStubber willReturn(Object toBeReturned) {
        return new BDDStubberImpl(Mockito.doReturn(toBeReturned));
    }

    /**
     * see original {@link Mockito#doCallRealMethod()}
     */
    public static BDDStubber willCallRealMethod() {
        return new BDDStubberImpl(Mockito.doCallRealMethod());
    }
}