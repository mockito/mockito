/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.configuration.InjectingAnnotationEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class InjectingAnnotationEngineByConstructorTest {

    public static class MockedDao {
        void assertInjection() {
            fail("should be mocked");
        }
    }

    public static class ByConstructorSpiedService {
        final MockedDao mockedDao;
        ByConstructorSpiedService(MockedDao mockedDao) {
            this.mockedDao = mockedDao;
        }
        void assertInjection() {
            assertNotNull(mockedDao);
            mockedDao.assertInjection();
        }
    }

    public static class ByConstructorTestedService {
        final MockedDao mockedDao;
        final ByConstructorSpiedService byConstructorSpiedService;
        ByConstructorTestedService(MockedDao mockedDao, ByConstructorSpiedService byConstructorSpiedService) {
            this.mockedDao = mockedDao;
            this.byConstructorSpiedService = byConstructorSpiedService;
        }
        void assertInjection() {
            assertNotNull(mockedDao);
            mockedDao.assertInjection();
            assertNotNull(byConstructorSpiedService);
            byConstructorSpiedService.assertInjection();
        }
    }

    @InjectMocks
    private ByConstructorTestedService byConstructorTestedService;

    @InjectMocks
    @Spy
    private ByConstructorSpiedService byConstructorSpiedService;

    @Mock
    private MockedDao mockedDao;

    @Test
    public void should_inject_mocks_and_spies_by_constructor() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(byConstructorTestedService);
        byConstructorTestedService.assertInjection();
    }

    @Test
    public void injected_mocks_and_spies_should_be_of_same_instance() {
        new InjectingAnnotationEngine().process(getClass(), this);
        assertNotNull(byConstructorTestedService);
        byConstructorTestedService.assertInjection();
        assertEquals(byConstructorSpiedService, byConstructorTestedService.byConstructorSpiedService);
        assertEquals(mockedDao, byConstructorTestedService.mockedDao);
        assertEquals(mockedDao, byConstructorTestedService.byConstructorSpiedService.mockedDao);
    }

}
