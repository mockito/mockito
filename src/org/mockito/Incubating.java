/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The annotation conveys following information:
 * <ul>
 * <li>The API is fairly new and we would appreciate your feedback. For example, what are you missing from the API
 * to solve your use case (yes, please, real use cases).</li>
 * <li>For types or methods that are already released this annotation means that that the API might change.
 * The chance for that is small and we will always try to make any changes in a backwards compatible way.
 * The only reason we would want to change it is to provide better support for using and extending Mockito.
 * </li>
 * <li>
 * For types or methods that are not yet released it means the API is <strong>work in progress</strong>
 * and can change before release.
 * </li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Incubating {
}
