module com.example.mokkivaraus {
    requires javafx.controls;
    requires javafx.fxml;


    requires java.desktop; // Добавьте эту строку, чтобы ваш модуль прочитал пакеты Swing и AWT
    requires kernel;
    requires layout;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;
    requires mysql.connector.j;
    requires itextpdf;
    requires org.apache.pdfbox;
    requires org.slf4j;

    opens com.example.mokkivaraus to javafx.fxml;
    exports com.example.mokkivaraus;
}