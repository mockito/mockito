package org.easymock.tests2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;

public class NiceMockReturnsEmptyCollectionsTest {

    @SuppressWarnings("unchecked")
    @Test public void testBeNiceConvertsDefaultMock() {
        CollectionServer mock = EasyMock.createNiceMock(CollectionServer.class);
        
        EasyMock.replay(mock);
        
        assertTrue(mock.list().isEmpty());
        assertTrue(mock.linkedList().isEmpty());
        assertTrue(mock.map().isEmpty());
        assertTrue(mock.hashSet().isEmpty());

        EasyMock.verify(mock);
    }

    @SuppressWarnings("unchecked")
    private interface CollectionServer {
        List list();
        LinkedList linkedList();
        Map map();
        java.util.HashSet hashSet();
    }
}