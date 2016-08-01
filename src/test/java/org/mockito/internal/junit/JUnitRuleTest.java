package org.mockito.internal.junit;

import org.junit.After;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
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
import static org.mockito.Mockito.mock;

public class JUnitRuleTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule jUnitRule = new JUnitRule(logger, false);
    private InjectTestCase injectTestCase = new InjectTestCase();
    private FrameworkMethod dummy = mock(FrameworkMethod.class);

    @After public void after() {
        //so that the validate framework usage exceptions do not collide with the tests here
        resetState();
    }

    @Test
    public void injects_into_test_case() throws Throwable {
        jUnitRule.apply(new DummyStatement(), dummy, injectTestCase).evaluate();
        assertNotNull("@Mock mock object created", injectTestCase.getInjected());
        assertNotNull("@InjectMocks object created", injectTestCase.getInjectInto());
        assertNotNull("Mock injected into the object", injectTestCase.getInjectInto().getInjected());
    }

    @Test
    public void rethrows_exception() throws Throwable {
        try {
            jUnitRule.apply(new ExceptionStatement(), dummy, injectTestCase).evaluate();
            fail("Should throw exception");
        } catch (RuntimeException e) {
            assertEquals("Correct message", "Statement exception", e.getMessage());
        }
    }

    @Test
    public void detects_invalid_mockito_usage_on_success() throws Throwable {
        try {
            jUnitRule.apply(new UnfinishedStubbingStatement(), dummy, injectTestCase).evaluate();
            fail("Should detect invalid Mockito usage");
        } catch (UnfinishedStubbingException e) {
        }
    }

    @Test
    public void does_not_check_invalid_mockito_usage_on_failure() throws Throwable {
        //I am not sure that this is the intended behavior
        //However, it was like that since the beginning of JUnit rule support
        //Users never questioned this behavior. Hence, let's stick to it unless we have more data
        try {
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    Mockito.when(mock.simpleMethod()); // <--- unfinished stubbing
                    throw new RuntimeException("Boo!"); // <--- some failure
                }
            }, dummy, injectTestCase).evaluate();
            fail();
        } catch (RuntimeException e) {
            assertEquals("Boo!", e.getMessage());
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