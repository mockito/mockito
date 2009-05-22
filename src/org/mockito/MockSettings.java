/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.stubbing.Answer;

public interface MockSettings {
    
    MockSettings extraInterfaces(Class<?>... interfaces);

    MockSettings name(String name);

    MockSettings spiedInstance(Object object);

    @SuppressWarnings("unchecked")
    //it's ok to supress it because having raw Answer here it makes nicer for clients 
    MockSettings defaultAnswer(Answer defaultAnswer);
    
}