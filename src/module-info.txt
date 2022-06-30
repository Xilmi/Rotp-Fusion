module rotp {
    requires transitive java.desktop;
    requires transitive java.management;
    requires transitive jdk.management; // for com.sun.management
    requires commons.math3;

    // experimental stuff
    // json
    requires transitive com.fasterxml.jackson.databind;
    // all the graalvm stuff. GraalVM fails to work with sound last time I tried it.
//    requires transitive svm;
//    requires transitive org.graalvm.sdk;

    exports rotp;
}
