package org.mockitousage.testng;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;

import java.util.Map;

@Listeners(MockitoTestNGListener.class)
public abstract class ParentTest {

    @Mock Map parentMockField;


}
