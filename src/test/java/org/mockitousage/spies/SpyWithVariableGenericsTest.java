package org.mockitousage.spies;

import org.junit.Test;
import org.mockito.Mockito;

public class SpyWithVariableGenericsTest {

    @Test
    public void can_spy_anonymous_class_created_in_method() {
        Mockito.spy(SpyWithVariableGenericsTest.<Integer>createPublisher());
    }

    private static <T> Publisher<T> createPublisher() {
        return new Publisher<T>() {
            @Override
            public void subscribe(Subscriber<? super T> subscribe) {

            }
        };
    }

    interface Publisher<T> {
        void subscribe(Subscriber<? super T> subscribe);
    }

    interface Subscriber<T> {
        void onNext(T next);
    }

}
