/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// issue 262
@RunWith(MockitoJUnitRunner.class)
public class ShouldNotTryToInjectInFinalOrStaticFieldsTest {

    public static class ExampleService {
        public static final List<String> CONSTANTS = Arrays.asList("c1", "c1");
        public final Set<String> aSet = new HashSet<String>();
    }

    @Spy private List<String> unrelatedList = new ArrayList<String>();
    @Mock private Set<String> unrelatedSet;

    @InjectMocks private ExampleService exampleService = new ExampleService();

    @Test
    public void dont_fail_with_CONSTANTS() throws Exception {
    }

    @Test
    public void dont_inject_in_final() {
        assertNotSame(unrelatedSet, exampleService.aSet);
    }

}