module org.mockito {

    requires java.instrument;
    requires jdk.attach;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    requires static junit;
    requires static org.hamcrest;
    requires static org.opentest4j;

    exports org.mockito;
    exports org.mockito.configuration;
    exports org.mockito.creation.instance;
    exports org.mockito.exceptions.base;
    exports org.mockito.exceptions.misusing;
    exports org.mockito.exceptions.verification;
    exports org.mockito.exceptions.verification.junit;
    exports org.mockito.exceptions.verification.opentest4j;
    exports org.mockito.hamcrest;
    exports org.mockito.invocation;
    exports org.mockito.junit;
    exports org.mockito.listeners;
    exports org.mockito.mock;
    exports org.mockito.plugins;
    exports org.mockito.quality;
    exports org.mockito.session;
    exports org.mockito.stubbing;
    exports org.mockito.verification;
}
