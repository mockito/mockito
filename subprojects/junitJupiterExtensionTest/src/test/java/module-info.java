module mockito.junitJupiterExtensionTest.test {
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.assertj.core;
    opens org.mockitousage;
    uses org.mockito.junit.jupiter.MockitoExtension;
}
