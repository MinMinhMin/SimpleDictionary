module com.front {
    requires javafx.controls;
    requires javafx.fxml;
<<<<<<< HEAD
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
	requires com.google.gson;

	opens myapp to com.google.gson, javafx.fxml;

	exports myapp;
=======

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens myapp to javafx.fxml;
    exports myapp;
>>>>>>> origin/main
}