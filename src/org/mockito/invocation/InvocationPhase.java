/*
 * Copyright (c) 2013 Thomson Reuters GRC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Thomson Reuters Tax and Accounting
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * Thomson Reuters Tax and Accounting.
 */
package org.mockito.invocation;

public enum InvocationPhase
{
    /**
     * Invocation occurred defining an invocation (i.e., during a when() call).
     */
    DEFINE,

    /**
     * Invocation occurred executing a method on a mock/spy.
     */
    EXECUTE,

    /**
     * Invocation occurred during a verify() call.
     */
    VERIFY
}
