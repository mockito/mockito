/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.mock.MockCreationSettings;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockSettingsTest extends TestBase {
    @Test
    public void public_api_for_creating_settings() throws Exception {
        //when
        MockCreationSettings<List> settings = Mockito.withSettings()
            .name("dummy")
            .build(List.class);

        //then
        assertEquals(List.class, settings.getTypeToMock());
        assertEquals("dummy", settings.getMockName().toString());
    }
    @Test
    public void test_without_annotations() throws Exception {
        MockCreationSettings<List> settings = Mockito.withSettings()
            .withoutAnnotations()
            .build(List.class);

        CreationSettings copy = new CreationSettings((CreationSettings)settings);

        assertEquals(List.class, settings.getTypeToMock());
        assertEquals(List.class, copy.getTypeToMock());

        assertTrue(settings.isStripAnnotations());
        assertTrue(copy.isStripAnnotations());
    }
}
