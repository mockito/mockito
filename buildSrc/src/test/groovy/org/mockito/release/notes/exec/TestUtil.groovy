package org.mockito.release.notes.exec

class TestUtil {
    static boolean commandAvailable(String command) {
        try {
            return command.execute().waitFor() == 0
        } catch (Exception e) {
            return false
        }
    }
}
