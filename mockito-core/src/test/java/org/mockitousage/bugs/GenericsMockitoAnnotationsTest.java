/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

/**
 * This was an issue reported in #1174.
 */
public class GenericsMockitoAnnotationsTest {

    @Mock private TestCollectionSourceProvider testCollectionSourceProvider;

    @Ignore
    @Test
    public void should_not_throw_class_cast_exception() {
        given(testCollectionSourceProvider.getCollection(new ArrayList<Integer>()))
                .willReturn(new ArrayList<Integer>());
    }

    static class TestCollectionSourceProvider {
        <T extends Collection<E>, E> T getCollection(T collection) {
            return collection;
        }
    }

    @Before
    public void setUp() throws Exception {
        openMocks(this);
    }
}
