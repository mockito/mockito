package org.mockito;

public interface MockSettings {
    
    MockSettings extraInterfaces(Class<?>... interfaces);

    MockSettings name(String name);

    MockSettings defaultBehavior(ReturnValues returnValues);
    
    //TODO: hide spiedInstance?
    MockSettings spiedInstance(Object object);
}