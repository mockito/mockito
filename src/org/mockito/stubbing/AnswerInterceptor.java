/*
 * Copyright (c) 2013 Thomson Reuters GRC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Thomson Reuters Tax and Accounting
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with
 * Thomson Reuters Tax and Accounting.
 */
package org.mockito.stubbing;

import org.mockito.invocation.InvocationOnMock;

/**
 */
public interface AnswerInterceptor<T>
{
    T answer(Answer answer, InvocationOnMock invocation) throws Throwable;
}
