module org.mockito.junit.jupiter {
    requires jdk.attach;
    requires java.instrument;
    requires java.management;
    requires java.base;
    requires org.junit.jupiter.api;
    requires org.mockito;

    exports org.mockito.junit.jupiter;
    exports org.mockito.junit.jupiter.resolver;

    opens org.mockito.junit.jupiter;
    opens org.mockito.junit.jupiter.resolver;
}
