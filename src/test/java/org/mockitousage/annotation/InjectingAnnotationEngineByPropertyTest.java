/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

public class InjectingAnnotationEngineByPropertyTest {

    public static class MockedDao {
        void assertInjection() {
            fail("should be mocked");
        }
    }

    public static class ByPropertySpiedService {
        MockedDao mockedDao;

        void assertInjection() {
            assertNotNull(mockedDao);
            mockedDao.assertInjection();
        }
    }

    public static class ByPropertyTestedService {
        MockedDao mockedDao;
        ByPropertySpiedService byPropertySpiedService;

        void assertInjection() {
            assertNotNull(mockedDao);
            mockedDao.assertInjection();
            assertNotNull(byPropertySpiedService);
            byPropertySpiedService.assertInjection();
        }
    }

    @InjectMocks private ByPropertyTestedService byPropertyTestedService;

    @InjectMocks @Spy private ByPropertySpiedService byPropertySpiedService;

    @Mock private MockedDao mockedDao;

    @Test
    public void should_inject_mocks_and_spies_by_property() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(byPropertyTestedService);
        byPropertyTestedService.assertInjection();
    }

    @Test
    public void injected_mocks_and_spies_should_be_of_same_instance() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(byPropertyTestedService);
        byPropertyTestedService.assertInjection();
        assertEquals(byPropertySpiedService, byPropertyTestedService.byPropertySpiedService);
        assertEquals(mockedDao, byPropertyTestedService.mockedDao);
        assertEquals(mockedDao, byPropertyTestedService.byPropertySpiedService.mockedDao);
    }
}
