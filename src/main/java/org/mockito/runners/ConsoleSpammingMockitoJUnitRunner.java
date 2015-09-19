/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.internal.debugging.WarningsCollector;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.RunnerImpl;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.internal.util.MockitoLogger;

import java.lang.reflect.InvocationTargetException;

/**
 * Uses <b>JUnit 4.5</b> runner {@link BlockJUnit4ClassRunner}.
 * <p>
 * Experimental implementation that suppose to improve tdd/testing experience. 
 * Don't hesitate to send feedback to mockito@googlegroups.com
 * <b>It is very likely it will change in the next version!</b>
 * <p>
 * This runner does exactly what {@link MockitoJUnitRunner} does but also  
 * prints warnings that might be useful. 
 * The point is that Mockito should help the tdd developer to quickly figure out if the test fails for the right reason. 
 * Then the developer can implement the functionality. 
 * Also when the test fails it should be easy to figure out why the test fails. 
 * <p>
 * Sometimes when the test fails, the underlying reason is that stubbed method was called with wrong arguments. 
 * Sometimes it fails because one forgets to stub a method or forgets to call a stubbed method. 
 * All above problems are not immediately obvious.
 * <p>
 * One way of approaching this problem is full-blown 'expect' API. 
 * However it means the 'expectations upfront' business which is not in line with core Mockito concepts.
 * After all, the essence of testing are <b>explicit assertions</b> that are described consistently at the <b>bottom of the test</b> method.
 * <p>
 * Here's the experiment: a warning is printed to the standard output if the test fails.
 * Also, you get a clickabe link to the line of code. You can immediately jump to the place in code where the potential problem is.
 * <p> 
 * Let's say your test fails on assertion. 
 * Let's say the underlying reason is a stubbed method that was called with different arguments:
 * <pre class="code"><code class="java">
 * //test:
 * Dictionary dictionary = new Dictionary(translator);
 * when(translator.translate("Mockito")).thenReturn("cool framework");
 * String translated = dictionary.search("Mockito");
 * assertEquals("cool framework", translated);
 * 
 * //code:
 * public String search(String word) {
 *     ...
 *     return translator.translate("oups");
 *
 * </code></pre>
 * On standard output you'll see something like that:
 * <pre class="code"><code class="java">
 * [Mockito] Warning - stubbed method called with different arguments.
 * Stubbed this way:
 * translator.translate("Mockito");
 * org.dictionary.SmartDictionaryTest.shouldFindTranslation(SmartDictionaryTest.java:27)
 *  
 * But called with different arguments:
 * translator.translate("oups");
 * org.dictionary.SmartDictionary.search(SmartDictionary.java:15)
 * </code></pre>
 * <p>
 * Note that it is just a warning, not an assertion. 
 * The test fails on assertion because it's the assertion's duty to document what the test stands for and what behavior it proves. 
 * Warnings just makes it quicker to figure out if the test fails for the right reason.
 * <p>
 * Note that code links printed to the console are clickable in any decent IDE (e.g. Eclipse).
 * <p>
 * So far I identified 2 cases when warnings are printed:
 * <li>unsued stub</li>
 * <li>stubbed method but called with different arguments</li> 
 * <p>
 * <br/>
 * <p>
 * Do you think it is useful or not? Drop us an email at mockito@googlegroups.com
 */
public class ConsoleSpammingMockitoJUnitRunner extends Runner implements Filterable {

    private final MockitoLogger logger;
    private final RunnerImpl runner;
    
    public ConsoleSpammingMockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        this(new ConsoleMockitoLogger(), new RunnerFactory().create(klass));
    }
    
    ConsoleSpammingMockitoJUnitRunner(MockitoLogger logger, RunnerImpl runnerImpl) {
        this.runner = runnerImpl;
        this.logger = logger;
    }
    
    @Override
    public void run(RunNotifier notifier) {
        RunListener listener = new RunListener() {
            WarningsCollector warningsCollector;
            
            @Override
            public void testStarted(Description description) throws Exception {
                warningsCollector = new WarningsCollector();
            }
            
            @Override public void testFailure(Failure failure) throws Exception {                
                logger.log(warningsCollector.getWarnings());
            }
        };

        notifier.addListener(listener);

        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }
    
    public void filter(Filter filter) throws NoTestsRemainException {
        //filter is required because without it UnrootedTests show up in Eclipse
        runner.filter(filter);
    }
}