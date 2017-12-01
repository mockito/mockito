/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.mock.MockCreationSettings;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
}
