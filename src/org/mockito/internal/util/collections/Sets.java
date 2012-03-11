package org.mockito.internal.util.collections;


import java.util.Set;

public abstract class Sets {
    public static Set<Object> newMockSafeHashSet(Iterable<Object> mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static Set<Object> newMockSafeHashSet(Object... mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static IdentitySet newIdentitySet() {
        return new IdentitySet();
    }
}
