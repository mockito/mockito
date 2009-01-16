/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.customization;

import org.mockito.Mockito;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.stubbing.Answer;

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
}