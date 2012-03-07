package org.mockitousage.testng;

import org.testng.annotations.Test;

import static org.mockito.Mockito.verifyZeroInteractions;

public class ResetMocksInParentTestClassTooTest extends ParentTest {

    @Test
    public void interact_with_parent_mock() throws Exception {
        parentMockField.get("a");
    }

    @Test
    public void verify__zero_interaction_with_parent_mock() throws Exception {
        verifyZeroInteractions(parentMockField);
    }
}
