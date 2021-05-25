/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.Test;

public class PrematureGarbageCollectionTest {

    @Test
    public void provoke_premature_garbage_collection() {
        for (int i = 0; i < 500; i++) {
            populateNodeList();
        }
    }

    private static void populateNodeList() {
        Node node = nodes();
        while (node != null) {
            Node next = node.next;
            node.object.run();
            node = next;
        }
    }

    private static Node nodes() {
        Node node = null;
        for (int i = 0; i < 1_000; ++i) {
            Node next = new Node();
            next.next = node;
            node = next;
        }
        return node;
    }

    private static class Node {

        private Node next;

        private final Runnable object = Mockito.mock(Runnable.class);
    }
}
