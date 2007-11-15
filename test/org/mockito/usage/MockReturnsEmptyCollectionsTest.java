package org.mockito.usage;

import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class MockReturnsEmptyCollectionsTest {

    @SuppressWarnings("unchecked")
    @Test 
    public void shouldReturnEmptyCollections() {
        CollectionsServer mock = Mockito.mock(CollectionsServer.class);
        
        assertTrue(mock.list().isEmpty());
        assertTrue(mock.linkedList().isEmpty());
        assertTrue(mock.map().isEmpty());
        assertTrue(mock.hashSet().isEmpty());
    }

    @SuppressWarnings("unchecked")
    private class CollectionsServer {
        List list() {
            return null;
        }

        LinkedList linkedList() {
            return null;
        }

        Map map() {
            return null;
        }

        java.util.HashSet hashSet() {
            return null;
        }
    }
}