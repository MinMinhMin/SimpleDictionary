package myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;

public class CreateSuggestionItem {
	public static HBox createSuggestionItem(String suggestion, TextField searchBar) {
		HBox suggestionItem = new HBox();
		suggestionItem.getStyleClass().add("suggestion-item");

		String[] Line = suggestion.split(" - ");
		if (Line.length == 2) {
			String word = Line[0].trim();
			String meanings = Line[1].trim();

			ContextMenu contextMenu = loadContextMenu(word, searchBar);

			Label wordLabel = new Label(word);
			wordLabel.getStyleClass().add("suggestion-label");

			Label lb = new Label(": " + meanings);
			suggestionItem.getChildren().addAll(wordLabel, lb);

			// Add hover effect
			suggestionItem.setOnMouseEntered(event -> suggestionItem.setStyle("-fx-background-color: #fce3e3;-fx-border-width: 3;-fx-border-color: rgb(18, 19, 20)"));
			suggestionItem.setOnMouseExited(event -> suggestionItem.setStyle("-fx-background-color: transparent;"));

			suggestionItem.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.SECONDARY) {
					((ContextMenuController) contextMenu.getUserData()).setSelectedWord(word);
					contextMenu.show(suggestionItem, event.getScreenX(), event.getScreenY());
				} else if (event.getButton() == MouseButton.PRIMARY) {
					searchBar.setText(word);
				}
			});

			suggestionItem.setMinWidth(Region.USE_PREF_SIZE);
			suggestionItem.setMaxWidth(Region.USE_PREF_SIZE);
		}

		return suggestionItem;
	}

	private static ContextMenu loadContextMenu(String word, TextField searchBar) {
		try {
			FXMLLoader loader = new FXMLLoader(CreateSuggestionItem.class.getResource("ContextMenu.fxml"));
			ContextMenu contextMenu = loader.load();
			ContextMenuController controller = loader.getController();
			contextMenu.setUserData(controller);
			return contextMenu;
		} catch (IOException e) {
			e.printStackTrace();
			return new ContextMenu();
		}
	}
}