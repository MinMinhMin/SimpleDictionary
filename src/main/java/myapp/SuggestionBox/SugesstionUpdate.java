package myapp.SuggestionBox;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class SugesstionUpdate {
	public static void sugesstionUpdate(String prefix, Words words, VBox suggestionBox, TextField searchBar) {
		prefix = prefix.toLowerCase();
		List<String> suggestions = words.auto_complete(prefix);
		suggestionBox.getChildren().clear();
        for (String suggestion:suggestions){
            suggestionBox.getChildren().add(CreateSuggestionItem.createSuggestionItem(suggestion, searchBar));
        }

	}
}