package testutil

class OfflineChecker {

    static isOffline() {
        isOffline("http://google.com")
    }

    static isOffline(String testUrl) {
        try {
            new URL(testUrl).withInputStream {}
            return false
        } catch (Exception ignored) {
            return true
        }
    }
}
