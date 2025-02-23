module org.mockito.junit.jupiter {
    requires org.junit.jupiter.api;
    requires org.mockito;
    exports org.mockito.junit.jupiter;
    opens org.mockito.junit.jupiter to org.junit.platform.commons;
}
