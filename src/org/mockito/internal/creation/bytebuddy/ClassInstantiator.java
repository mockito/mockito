package org.mockito.internal.creation.bytebuddy;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
*
*/
interface ClassInstantiator {

    <T> T instantiate(Class<T> type);

    static class UsingObjenesis implements ClassInstantiator {

        private final Objenesis objenesis;

        public UsingObjenesis(boolean useClassCache) {
            this.objenesis = new ObjenesisStd(useClassCache);
        }

        public <T> T instantiate(Class<T> type) {
            return objenesis.newInstance(type);
        }
    }
}
