package myapp.SuggestionBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import myapp.Main;

import java.io.IOException;

public class CreateSuggestionItem {

	public static HBox createSuggestionItem(String suggestion, String meaning, TextField searchBar) {
		HBox suggestionItem = new HBox();
		suggestionItem.getStyleClass().add("suggestion-item");
		String stringBuilder = suggestion +
				": " +
				meaning;

		ContextMenu contextMenu = loadContextMenu();

		Label wordLabel = new Label(stringBuilder);
		suggestionItem.getChildren().addAll(wordLabel);
		// Add hover effect
		suggestionItem.setOnMouseEntered(event -> {
			Main.mainController.setLeftClick(suggestion);
			Main.mainController.setRightClick("More word options");
			suggestionItem.setStyle("-fx-background-color: rgba(0, 0, 0, 0.38);-fx-border-width: 3;-fx-border-color: rgb(18, 19, 20)");
		});
		suggestionItem.setOnMouseExited(event -> {
			Main.mainController.setLeftClick("");
			Main.mainController.setRightClick("");
			suggestionItem.setStyle("-fx-background-color: transparent;");
		});

		suggestionItem.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				((ContextMenuController) contextMenu.getUserData()).setSelectedWord(suggestion, meaning);
				contextMenu.show(suggestionItem, event.getScreenX(), event.getScreenY());
			} else if (event.getButton() == MouseButton.PRIMARY) {
				searchBar.setText(suggestion + ": " + meaning);
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