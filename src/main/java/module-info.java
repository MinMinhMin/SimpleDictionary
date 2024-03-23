module com.front {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
	requires com.google.gson;

	opens myapp to com.google.gson, javafx.fxml;

	exports myapp;
}