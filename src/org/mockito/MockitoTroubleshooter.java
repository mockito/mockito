package org.mockito;

import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * First of all, I encourage you to read the Mockito FAQ: <a href="http://code.google.com/p/mockito/wiki/FAQ">http://code.google.com/p/mockito/wiki/FAQ</a>
 * <p>
 * In case of questions you may also post to mockito mailing list: <a href="http://groups.google.com/group/mockito">http://groups.google.com/group/mockito</a> 
 * <p>  
 * Next, you should know that Mockito validates if you use it correctly <b>all the time</b>. However, there's a gotcha so read on.
 * <p>
 * Examples of incorrect use:
 * <pre>
 * //Oups, someone forgot thenReturn() part:
 * when(mock.get());
 * 
 * //Oups, someone put the verified method call inside verify() where it should be outside:
 * verify(mock.execute());
 * 
 * //Oups, someone has used EasyMock for too long and forgot to specify the method to verify:
 * verify(mock);
 * </pre>
 * 
 * Mockito throws exceptions if you misuse it so that you will know if your tests are written correctly. 
 * The thing is that Mockito does the validation <b>next time</b> you use the framework (e.g. next time you verify, stub, call mock etc.). 
 * But even though the exception might b thrown in the next test, the exception message contains a navigable stack trace element with location of the defect. 
 * Hence you can click and find the place where Mockito was misused.
 * <p>
 * Sometimes though, you might want to validate the framework state explicitly. 
 * For example, one of the users wanted to put {@link MockitoTroubleshooter#validateFrameworkState()} in his &#064;After method
 * so that he knows immediately when he misused Mockito. Without it, he would have known about it not sooner than <b>next time</b> he used the framework.  
 * <p>
 * Bear in mind that <b>usually you shouldn't need MockitoTroubleshooter</b> 
 * and framework validation triggered on next-time basis is just enough.  
 */
public class MockitoTroubleshooter {

    /**
     * Explicitly validates the framework state to detect invalid use of Mockito.
     * <p>
     * See examples in javadoc for {@link MockitoTroubleshooter} class
     */
    public static void validateFrameworkState() {
        MockingProgress mockingProgress = new ThreadSafeMockingProgress();
        mockingProgress.validateState();
    }
}