module com.example.proyectomp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jaudiotagger;
    requires java.desktop;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.example.proyectomp3 to javafx.fxml;
    exports com.example.proyectomp3;
}