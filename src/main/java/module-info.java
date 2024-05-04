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
	exports myapp.Game;
	opens myapp.Game to com.google.gson, javafx.fxml;
	exports myapp.Game.CrossBoard;
	opens myapp.Game.CrossBoard to com.google.gson, javafx.fxml;
	exports myapp.Game.WordMeaning;
	opens myapp.Game.WordMeaning to javafx.fxml;
	exports myapp.SuggestionBox;
	opens myapp.SuggestionBox to com.google.gson, javafx.fxml;
	exports myapp.SuggestionBox.WordDetails;
	opens myapp.SuggestionBox.WordDetails to com.google.gson, javafx.fxml;
	exports myapp.Translate;
	opens myapp.Translate to com.google.gson, javafx.fxml;
	exports myapp.Music;
	opens myapp.Music to com.google.gson, javafx.fxml;
}