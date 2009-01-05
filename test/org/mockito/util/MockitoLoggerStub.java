/**
 * 
 */
package org.mockito.util;

import org.mockito.internal.util.MockitoLoggerImpl;

public class MockitoLoggerStub extends MockitoLoggerImpl {
        
        StringBuilder loggedInfo = new StringBuilder();
        
        public void log(Object what) {
//            can be uncommented when debugging this test
//            super.log(what);
            loggedInfo.append(what);
        }

        public String getLoggedInfo() {
            return loggedInfo.toString();
        }
    }