package myapp.SuggestionBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;

public class CreateSuggestionItem {
	public static HBox createSuggestionItem(String suggestion, String meaning, TextField searchBar) {
        HBox suggestionItem = new HBox();
		suggestionItem.getStyleClass().add("suggestion-item");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(suggestion);
        stringBuilder.append(": ");
        stringBuilder.append(meaning);

			ContextMenu contextMenu = loadContextMenu();

			Label wordLabel = new Label(stringBuilder.toString());
			suggestionItem.getChildren().addAll(wordLabel);

			// Add hover effect
			suggestionItem.setOnMouseEntered(event -> suggestionItem.setStyle("-fx-background-color: #fce3e3;-fx-border-width: 3;-fx-border-color: rgb(18, 19, 20)"));
			suggestionItem.setOnMouseExited(event -> suggestionItem.setStyle("-fx-background-color: transparent;"));

			suggestionItem.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.SECONDARY) {
					((ContextMenuController) contextMenu.getUserData()).setSelectedWord(suggestion,meaning);
					contextMenu.show(suggestionItem, event.getScreenX(), event.getScreenY());
				} else if (event.getButton() == MouseButton.PRIMARY) {
					searchBar.setText(suggestion+": "+meaning);
				}
			});

			suggestionItem.setMinWidth(Region.USE_PREF_SIZE);
			suggestionItem.setMaxWidth(Region.USE_PREF_SIZE);


		return suggestionItem;
	}

	private static ContextMenu loadContextMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(CreateSuggestionItem.class.getResource("/myapp/ContextMenu.fxml"));
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