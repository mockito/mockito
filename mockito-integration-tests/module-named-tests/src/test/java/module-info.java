module org.mockito.test {
    requires junit;
    requires org.mockito;
    requires org.assertj.core;

    exports org.mockito.modulenamedtest to
            junit;
}
