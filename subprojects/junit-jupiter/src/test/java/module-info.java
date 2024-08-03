module mockitousage.junit.jupiter.test {
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.junit.platform.engine;
    requires org.junit.platform.launcher;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.assertj.core;
    opens org.mockitousage to org.junit.platform.commons;
}
