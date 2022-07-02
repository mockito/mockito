/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.InjectUnsafe;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.*;

@RunWith(MockitoJUnitRunner.class)
public class InjectUnsafeTest {

    public static class Antenna {
        private final String name;

        public Antenna(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class FinalReceiver {
        private final Antenna antenna = new Antenna("the-real-one");

        public String getAntennaName() {
            return antenna.getName();
        }
    }

    public static class StaticReceiver {
        @SuppressWarnings("FieldMayBeFinal")
        private static Antenna antenna = new Antenna("the-real-one");

        public String getAntennaName() {
            return antenna.getName();
        }
    }

    public static class StaticFinalReceiver {
        @SuppressWarnings("FieldMayBeFinal")
        private static final Antenna antenna = new Antenna("the-real-one");

        public String getAntennaName() {
            return antenna.getName();
        }
    }

    @Before
    public void before() {
        System.out.println("before");
    }

    @After
    public void after() {
        System.out.println("after");
    }

    @Mock private Antenna mockAntenna;

    @InjectMocks
    @InjectUnsafe(FINAL)
    private FinalReceiver finalReceiver;

    @InjectMocks
    @InjectUnsafe(STATIC)
    private StaticReceiver staticReceiver;

    @InjectMocks
    @InjectUnsafe(STATIC_FINAL)
    private StaticFinalReceiver staticFinalReceiver;

    @Test
    public void injectunsafe_injects_into_final_fields() {
        Mockito.doReturn("final mock").when(mockAntenna).getName();

        String actual = finalReceiver.getAntennaName();

        assertEquals("final mock", actual);
    }

    @Test
    public void injectunsafe_injects_into_static_fields() {
        Mockito.doReturn("static mock").when(mockAntenna).getName();

        String actual = staticReceiver.getAntennaName();

        assertEquals("static mock", actual);
    }

    @Test
    public void injectunsafe_injects_into_static_final_fields() {
        Mockito.doReturn("static final mock").when(mockAntenna).getName();

        String actual = staticFinalReceiver.getAntennaName();

        assertEquals("static final mock", actual);
    }
}
