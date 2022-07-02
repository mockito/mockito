/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import javax.annotation.Generated;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * To set a value on a static final field, setting the field accessible
 * by using {@link Field#setAccessible(boolean)} is not enough.
 * Only if removing the field modifier {@link Modifier#STATIC}, a value can be set via reflection.
 */
public abstract class StaticFinalOverrider {
    public static StaticFinalOverrider forField(Field field) {
        if (needsOverriding(field)) {
            return new NopStaticFinalOverrider();
        } else {
            return new StaticFinalOverriderImpl(field);
        }
    }

    private static boolean needsOverriding(Field field) {
        boolean canWrite = (field.getModifiers() & (Modifier.FINAL)) == 0;
        return canWrite;
    }

    public abstract void enableWrite();

    public abstract void restore();

    private static class StaticFinalOverriderImpl extends StaticFinalOverrider {
        private final Field injecteeField;
        private final Field modifiersField;

        private Integer originalModifiers = null;

        private StaticFinalOverriderImpl(Field injecteeField) {
            this.injecteeField = injecteeField;

            modifiersField = tryFindModifiersField();
        }

        @Generated(
                "exclude from coverage report because we cannot test the exception in a meaningful way")
        private static Field tryFindModifiersField() {
            try {
                Field modifiersField = findModifiersField();

                return modifiersField;
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Field.modifiers not found", e);
            }
        }

        private static Field findModifiersField() throws NoSuchFieldException {
            Field modifiersField = Field.class.getDeclaredField("modifiers");

            return modifiersField;
        }

        @Override
        public void enableWrite() {
            backupFieldSettings();
            tryRemoveFinalModifier();
        }

        @Override
        public void restore() {
            ensureFieldSettingsBackupValid();
            tryRestoreFieldSettings();
        }

        @Generated(
                "exclude from coverage report because we cannot test the exception in a meaningful way")
        private void tryRemoveFinalModifier() {
            try {
                removeFinalModifier();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "Cannot change modifier on field: " + injecteeField, e);
            }
        }

        private void removeFinalModifier() throws IllegalAccessException {
            modifiersField.setAccessible(true);
            modifiersField.setInt(injecteeField, injecteeField.getModifiers() & ~Modifier.FINAL);
        }

        private void backupFieldSettings() {
            originalModifiers = injecteeField.getModifiers();
        }

        private void ensureFieldSettingsBackupValid() {
            if (originalModifiers == null) {
                throw new IllegalStateException(
                        "The field " + injecteeField + " was never modified");
            }
        }

        @Generated(
                "exclude from coverage report because we cannot test the exception in a meaningful way")
        private void tryRestoreFieldSettings() {
            try {
                restoreFieldSettings();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot reset field: " + injecteeField, e);
            }
        }

        private void restoreFieldSettings() throws IllegalAccessException {
            modifiersField.setInt(injecteeField, originalModifiers);
            modifiersField.setAccessible(false);
        }
    }

    private static class NopStaticFinalOverrider extends StaticFinalOverrider {
        @Override
        public void enableWrite() {
            // nop
        }

        @Override
        public void restore() {
            // nop
        }
    }
}
