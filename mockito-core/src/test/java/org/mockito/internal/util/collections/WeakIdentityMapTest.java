/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeakIdentityMapTest {

    WeakIdentityMap<String> map = new WeakIdentityMap<>();

    @Test
    public void putAndGet() {
        Object key = new Object();
        map.put(key, "value");

        assertEquals("value", map.get(key));
        assertTrue(map.containsKey(key));
    }

    @Test
    public void getReturnsNullForAbsentKey() {
        assertNull(map.get(new Object()));
    }

    @Test
    public void remove() {
        Object key = new Object();
        map.put(key, "value");

        assertEquals("value", map.remove(key));
        assertNull(map.get(key));
        assertFalse(map.containsKey(key));
    }

    @Test
    public void removeReturnsNullForAbsentKey() {
        assertNull(map.remove(new Object()));
    }

    @Test
    public void putOverwritesPreviousValue() {
        Object key = new Object();
        map.put(key, "first");
        map.put(key, "second");

        assertEquals("second", map.get(key));
        assertEquals(1, map.size());
    }

    @Test
    public void usesIdentityNotEquals() {
        // Two objects that are equals() but not ==
        String key1 = new String("same");
        String key2 = new String("same");
        assertEquals(key1, key2);
        assertNotSame(key1, key2);

        map.put(key1, "value1");
        map.put(key2, "value2");

        assertEquals("value1", map.get(key1));
        assertEquals("value2", map.get(key2));
        assertEquals(2, map.size());
    }

    @Test
    public void doesNotCallHashCodeOnKeys() {
        Object key =
                new Object() {
                    @Override
                    public int hashCode() {
                        throw new RuntimeException("hashCode should not be called");
                    }

                    @Override
                    public boolean equals(Object obj) {
                        throw new RuntimeException("equals should not be called");
                    }
                };

        map.put(key, "value");
        assertEquals("value", map.get(key));
        assertTrue(map.containsKey(key));
        assertEquals("value", map.remove(key));
    }

    @Test
    public void entrySetReflectsContents() {
        Object key1 = new Object();
        Object key2 = new Object();
        map.put(key1, "a");
        map.put(key2, "b");

        assertEquals(2, map.entrySet().size());
    }

    @Test
    public void sizeReflectsContents() {
        assertEquals(0, map.size());

        Object key = new Object();
        map.put(key, "value");
        assertEquals(1, map.size());

        map.remove(key);
        assertEquals(0, map.size());
    }

    @Test
    public void weakReferenceBehavior() {
        Object key = new Object();
        map.put(key, "value");
        assertEquals(1, map.size());

        // Clear the strong reference and trigger GC
        key = null;
        forceGc();

        // After GC, the entry should be expunged on next access
        assertEquals(0, map.size());
    }

    private static void forceGc() {
        for (int i = 0; i < 5; i++) {
            System.gc();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
