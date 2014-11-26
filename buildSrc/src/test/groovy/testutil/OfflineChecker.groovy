package testutil

class OfflineChecker {

    static isOffline() {
        isOffline("http://google.com")
    }

    @groovy.transform.PackageScope static isOffline(String testUrl) {
        try {
            new URL(testUrl).withInputStream {}
            return false
        } catch (Exception ignored) {
            return true
        }
    }
}
