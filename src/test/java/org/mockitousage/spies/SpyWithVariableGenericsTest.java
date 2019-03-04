/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.Test;
import org.mockito.Mockito;

public class SpyWithVariableGenericsTest {

    @Test
    public void can_spy_anonymous_class_created_in_method() {
        Publisher<Integer> spy = Mockito
            .spy(SpyWithVariableGenericsTest.<Integer>createPublisher());

        final AtomicBoolean called = new AtomicBoolean(false);

        spy.subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer next) {
                called.set(true);
                assertThat(next).isEqualTo(5);
            }
        });

        spy.publish(5);
        assertThat(called.get()).isTrue();
    }

    private static <T> Publisher<T> createPublisher() {
        return new Publisher<T>() {
            private Subscriber<? super T> subscriber;

            @Override
            public void subscribe(Subscriber<? super T> subscriber) {
                this.subscriber = subscriber;
            }

            @Override
            public void publish(T object) {
                this.subscriber.onNext(object);
            }
        };
    }

    interface Publisher<T> {
        void subscribe(Subscriber<? super T> subscribe);
        void publish(T object);
    }

    interface Subscriber<T> {
        void onNext(T next);
    }

}
