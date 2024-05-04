package myapp.SuggestionBox;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SugesstionUpdate {
	protected VBox suggestionBox;

	public VBox getSuggestionBox() {
		return suggestionBox;
	}
	public SugesstionUpdate(VBox suggestionBox){
		this.suggestionBox = suggestionBox;

	}

	public  void sugesstionUpdate(String prefix, Words words, VBox suggestionBox, TextField searchBar) {
		prefix = prefix.toLowerCase();
		List<String> suggestions = words.auto_complete(prefix);
        Set<String>set=new LinkedHashSet<>(suggestions);
        List<String>new_suggestions=new ArrayList<>(set);
		suggestionBox.getChildren().clear();
        for (String suggestion:new_suggestions){
            for (String meaning:Words.meaning.get(suggestion)){

                suggestionBox.getChildren().add(CreateSuggestionItem.createSuggestionItem(suggestion, meaning,searchBar));

            }
        }

	}
}