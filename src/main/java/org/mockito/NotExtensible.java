/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates to the user that she should not provide custom implementations of given type.
 * Helps framework integrators and our users understand how to use Mockito API safely,
 * without the risk of getting exposed to incompatible changes.
 * Some types that are a part of Mockito public API are not intended to be extended.
 * It's because Mockito team needs to be able to add new methods to some types without breaking compatibility contract.
 * We would never break compatibility by changing the signature of an existing public method.
 * However, we need flexibility to add new methods to some types to evolve the API if needed.
 * Public types are all types that are *not* under "org.mockito.internal.*" package.
 * <p>
 * Absence of {@code NotExtensible} annotation on a type *does not* mean it is intended to be extended.
 * The annotation has been introduced late and therefore it is not used frequently in the codebase.
 * Many public types from Mockito API are not intended for extension, even though they do not have this annotation applied.
 *
 * @since 2.10.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotExtensible {
}
