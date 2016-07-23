package org.mockito.internal.junit;

import org.junit.Test;
import org.junit.runners.model.Statement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;

public class JUnitRuleTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule jUnitRule = new JUnitRule(logger);
    private InjectTestCase injectTestCase = new InjectTestCase();

    @Test
    public void shouldInjectIntoTestCase() throws Throwable {
        jUnitRule.apply(new DummyStatement(),null, injectTestCase).evaluate();
        assertNotNull("@Mock mock object created", injectTestCase.getInjected());
        assertNotNull("@InjectMocks object created", injectTestCase.getInjectInto());
        assertNotNull("Mock injected into the object", injectTestCase.getInjectInto().getInjected());
    }

    @Test
    public void shouldRethrowException() throws Throwable {
        try {
            jUnitRule.apply(new ExceptionStatement(), null,injectTestCase).evaluate();
            fail("Should throw exception");
        } catch (RuntimeException e) {
            assertEquals("Correct message", "Statement exception", e.getMessage());
        }
    }

    @Test
    public void shouldDetectUnfinishedStubbing() throws Throwable {
        try {
            jUnitRule.apply(new UnfinishedStubbingStatement(),null, injectTestCase).evaluate();
            fail("Should detect invalid Mockito usage");
        } catch (UnfinishedStubbingException e) {
        }
    }

    @Test
    public void shouldWarnAboutUnusedStubsWhenFailed() throws Throwable {
        try {
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = Mockito.mock(IMethods.class);
                    declareUnusedStub(mock);
                    throw new AssertionError("x");
                }
            },null, injectTestCase).evaluate();
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(filterLineNo(logger.getLoggedInfo()),
                "[Mockito] Additional stubbing information (see javadoc for StubbingInfo class):\n" +
                "[Mockito]\n" +
                "[Mockito] Unused stubbing (perhaps can be removed from the test?):\n" +
                "[Mockito]\n" +
                "[Mockito] 1. -> at org.mockito.internal.junit.JUnitRuleTest.declareUnusedStub(JUnitRuleTest.java:0)"
            );
        }
    }

    @Test
    public void can_remove_line_numbers() throws Throwable {
        assertEquals(
                "[Mockito] 1. -> at org.mockito.internal.junit.JUnitRuleTest.declareUnusedStub(JUnitRuleTest.java:0)",
                filterLineNo("[Mockito] 1. -> at org.mockito.internal.junit.JUnitRuleTest.declareUnusedStub(JUnitRuleTest.java:82)"));
    }

    @Test
    public void shouldNotWarnAboutUnusedStubsWhenPassed() throws Throwable {
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = Mockito.mock(IMethods.class);
                declareUnusedStub(mock);
            }
        },null, injectTestCase).evaluate();

        assertEquals("", logger.getLoggedInfo());
    }

    private static void declareUnusedStub(IMethods mock) {
        Mockito.when(mock.simpleMethod("foo")).thenReturn("bar");
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

    public static class InjectTestCase {

        @Mock
        private Injected injected;

        @InjectMocks
        private InjectInto injectInto;

        @Test
        public void dummy() throws Exception {
        }

        public void unfinishedStubbingThrowsException() throws Exception {
            Mockito.when(injected.stringMethod());
        }

        public Injected getInjected() {
            return injected;
        }

        public InjectInto getInjectInto() {
            return injectInto;
        }

        public static class Injected {
            public String stringMethod() {
                return "string";
            }
        }

        public static class InjectInto {
            private Injected injected;

            public Injected getInjected() {
                return injected;
            }
        }

    }
}