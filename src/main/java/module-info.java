module com.example.mokkivaraus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;
    requires mysql.connector.j;

    opens com.example.mokkivaraus to javafx.fxml;
    exports com.example.mokkivaraus;
}