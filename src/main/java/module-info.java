module com.front {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
	requires com.google.gson;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens myapp to com.google.gson, javafx.fxml;

	exports myapp;
}