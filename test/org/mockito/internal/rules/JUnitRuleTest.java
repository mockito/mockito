package org.mockito.internal.rules;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class JUnitRuleTest {

    private JUnitRule jUnitRule;
    private InjectTestCase injectTestCase;

    @Before
    public void setUp() throws Exception {
        injectTestCase = new InjectTestCase();
        jUnitRule = new JUnitRule(injectTestCase);
    }

    @Test
    public void testInject() throws Throwable {
        jUnitRule.apply(new DummyStatement(), Description.EMPTY).evaluate();
        assertNotNull("", injectTestCase.getInjected());
        assertNotNull("", injectTestCase.getInjectInto());
    }

    @Test
    public void testThrowAnException() throws Throwable {
        try {
            jUnitRule.apply(new ExceptionStatement(), Description.EMPTY).evaluate();
            fail("Should throw exception");
        } catch (RuntimeException e) {
            assertEquals("Correct message", "Statement exception", e.getMessage());
        }
    }

    @Test
    public void testMockitoValidation() throws Throwable {
        try {
            jUnitRule.apply(new UnfinishedStubbingStatement(), Description.EMPTY).evaluate();
            fail("Should detect unvalid Mockito usage");
        } catch (UnfinishedStubbingException e) {
        }
    }

    private static class DummyStatement extends Statement {
        @Override
        public void evaluate() throws Throwable {
        }
    }

    private static class ExceptionStatement extends Statement {
        @Override
        public void evaluate() throws Throwable {
            throw new RuntimeException("Statement exception");
        }
    }

    private static class UnfinishedStubbingStatement extends Statement {
        @Override
        public void evaluate() throws Throwable {
            InjectTestCase injectTestCase = new InjectTestCase();
            MockitoAnnotations.initMocks(injectTestCase);
            injectTestCase.unfinishedStubbingThrowsException();
        }
    }
}