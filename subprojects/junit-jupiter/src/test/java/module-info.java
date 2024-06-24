module mockito.junit.jupiter.test {
    requires java.base;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.assertj.core;
    requires org.junit.platform.engine;
    requires org.junit.platform.launcher;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    opens org.mockitousage;
    opens org.mockitousage.annotation;

    exports org.mockitousage;
    exports org.mockitousage.annotation;
}
