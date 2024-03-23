package myapp;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SugesstionUpdate {
	public static void sugesstionUpdate(String prefix, AutoComplete.Trie trie, VBox suggestionBox, TextField searchBar) {
		prefix = prefix.toLowerCase();
		List<String> suggestions = trie.autocompleteWithMeanings(prefix);

		suggestionBox.getChildren().clear();
		Map<String, List<String>> groundedSg = suggestions.stream().collect(Collectors.groupingBy(suggestion -> {
			String[] line = suggestion.split("\t");
			return line.length >= 1 ? line[0].trim() : "";
		}));
		for (List<String> wordSuggestions : groundedSg.values()) {
			if (!wordSuggestions.isEmpty()) {
				suggestionBox.getChildren().add(CreateSuggestionItem.createSuggestionItem(wordSuggestions.get(0), searchBar));
			}
		}
	}
}