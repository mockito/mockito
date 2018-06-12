/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.*;

/**
 * Change the behavior of {@link InjectMocks}: allow injection of static/final fields that are normally skipped.
 *
 * <p>Typically, injection into instances is done via the constructor.</p>
 *
 * <p>In most cases you should refactor your code to support these best practices!</p>
 *
 * <p>However, sometimes you don't want to/cannot expose fields via constructor as this might change your api
 * (even if the constructor is package-private).<br/>
 * Even worse with static fields: you'd have to make setters available via method call.<br/>
 * And then there's always legacy/library code that cannot be changed easily :(</p>
 *
 * <p>InjectUnsafe to the rescue:<br/>
 * Modifies the behavior of InjectMocks in a way that allows injection into static and final fields. </p>
 *
 * <p> Please note: not all mock builders are supported equally:
 * bytebuddy (the default) will throw {@link IllegalAccessError}
 * if you try to use <code>staticFields = {@link OverrideStaticFields#STATIC_FINAL}</code>. </p>
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectUnsafe {
    enum OverrideStaticFields {
        /**
         * Default: no injection into static fields
         */
        NONE,
        /**
         * Allow injection into static fields - non-final only.
         */
        STATIC,
        /**
         * Allow injection into static fields - including static final.
         */
        STATIC_FINAL
    }

    enum OverrideInstanceFields {
        /**
         * Default: no injection into final instance fields
         */
        NONE,
        /**
         * Allow mock injection into final instance fields.
         */
        FINAL
    }

    /**
     * Allow mock-injection into instance fields that get skipped during the normal injection cycle.
     */
    OverrideInstanceFields instanceFields() default OverrideInstanceFields.NONE;

    /**
     * Only use this if you <strong>absolute know what you're doing!</strong>.
     *
     * This will change the value of static fields value for <strong>all subsequent usages of the static field</strong>
     * as long as the current class loader lives.
     * This might lead to very unexpected and hard-to-debug behavior of other tests!
     */
    OverrideStaticFields staticFields() default OverrideStaticFields.NONE;
}
