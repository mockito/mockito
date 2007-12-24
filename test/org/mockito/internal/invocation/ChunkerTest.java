/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;

public class ChunkerTest extends RequiresValidState {

    private final class EqualitySeer implements Chunker.ChunkSeer<Integer> {
        public boolean isSameChunk(Integer previous, Integer current) {
            return previous.equals(current);
        }
    }

    private Chunker chunker;

    @Before
    public void setup() throws Exception {
        chunker = new Chunker();
    }

    @Test
    public void shouldChunkObjectLists() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 2, 3), new EqualitySeer());
        assertEquals(3, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1));
        assertThat(chunked.get(1).getObjects(), collectionHasExactlyInOrder(2));
        assertThat(chunked.get(2).getObjects(), collectionHasExactlyInOrder(3));
    }
    
    @Test
    public void shouldChunkObjectListsAndFindDuplicates() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 1, 1, 2, 2, 3, 3, 3, 3), new EqualitySeer());
        assertEquals(3, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1, 1, 1));
        assertThat(chunked.get(1).getObjects(), collectionHasExactlyInOrder(2, 2));
        assertThat(chunked.get(2).getObjects(), collectionHasExactlyInOrder(3, 3, 3, 3));
    }
    
    @Test
    public void shouldChunkWhenDuplicatesOnEdges() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 1, 2, 3, 3), new EqualitySeer());
        assertEquals(3, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1, 1));
        assertThat(chunked.get(1).getObjects(), collectionHasExactlyInOrder(2));
        assertThat(chunked.get(2).getObjects(), collectionHasExactlyInOrder(3, 3));
    }
    
    @Test
    public void shouldChunkWhenDuplicatesInTheMiddle() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 2, 2, 3), new EqualitySeer());
        assertEquals(3, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1));
        assertThat(chunked.get(1).getObjects(), collectionHasExactlyInOrder(2, 2));
        assertThat(chunked.get(2).getObjects(), collectionHasExactlyInOrder(3));
    }
    
    @Test
    public void shouldNotMergeDuplicatesThatAreNotConsecutive() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 1, 2, 1, 1, 1), new EqualitySeer());
        assertEquals(3, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1, 1));
        assertThat(chunked.get(1).getObjects(), collectionHasExactlyInOrder(2));
        assertThat(chunked.get(2).getObjects(), collectionHasExactlyInOrder(1, 1, 1));
    }
    
    @Test
    public void shouldCreateSingleChunkForOneElementList() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1), new EqualitySeer());
        assertEquals(1, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1));
    }
    
    @Test
    public void shouldCreateSingleChunkOfSizeTwo() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(asList(1, 1), new EqualitySeer());
        assertEquals(1, chunked.size());
        assertThat(chunked.get(0).getObjects(), collectionHasExactlyInOrder(1, 1));
    }
    
    @Test
    public void shouldCreateEmptyChunks() throws Exception {
        List<ObjectsChunk<Integer>> chunked = chunker.chunk(Collections.<Integer>emptyList(), new EqualitySeer());
        assertEquals(0, chunked.size());
    }
}