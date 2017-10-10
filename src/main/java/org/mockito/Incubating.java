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
 * to solve your use case.</li>
 * <li>The API might change.
 * The chance for that is incredibly small because we care great deal for the initial design.
 * The incubating API might change based on the feedback from the community in order to make the API most useful for the users.
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
