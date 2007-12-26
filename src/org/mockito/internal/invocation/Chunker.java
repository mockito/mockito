/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

public class Chunker {
    
    /**
     * creates chunks of objects based on equality and consecutive factor, e.g:
     * <p>
     * if objects are [1,1,2,1,1] then there are 3 chunks: [1,1] [2] [1,1]
     */
    public <T> List<ObjectsChunk<T>> chunk(List<T> objects, ChunksDistributor<T> distributor) {
        LinkedList<ObjectsChunk<T>> chunks = new LinkedList<ObjectsChunk<T>>();
        T previous = null;
        for (T current : objects) {
            if (previous == null) {
                chunks.add(new ObjectsChunk<T>(current));
                previous = current;
                continue;
            } 
            
            if (distributor.isSameChunk(previous, current)) {
                chunks.getLast().addObject(current);
            } else {
                chunks.add(new ObjectsChunk<T>(current));
            }

            previous = current;
        }

        return chunks;
    }
    
    static interface ChunksDistributor<T> {
        boolean isSameChunk(T previous, T current);
    }
}