package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates to the user that she should not provide custom implementations of given type.
 * Some types that are a part of Mockito public API are not intended to be extended.
 * It's because Mockito team needs to be able to add new methods to some types without breaking compatibility contract.
 * We would never break compatibility by changing the signature of an existing public method.
 * However, we need flexibility to add new methods to some types to evolve the API if needed.
 * Public types are all types that are *not* under "org.mockito.internal.*" package.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotExtensible {
}
