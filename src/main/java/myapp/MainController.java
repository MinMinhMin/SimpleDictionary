package myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {
	@FXML
	public TextField searchBar;
	ContextMenu gameMenu = GameMenuController.loadGameMenu();
	private Stage stage;
	@FXML
	private VBox suggestionBox;
	private AutoComplete.Trie trie;
	@FXML
	private Button gamebutton;
	public final String ALOT_WORDS ="data/List.txt" ;


	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void initialize() {
		trie = AutoComplete.buildTrieFromFile(ALOT_WORDS);
		searchBar.textProperty().addListener((observable, oV, nV) -> {
			if (nV.isEmpty()) {
				suggestionBox.getChildren().clear();
			} else {
				SugesstionUpdate.sugesstionUpdate(nV, trie, suggestionBox, searchBar);
			}
		});
	}

	@FXML
	private void AddClicked() {
		String english = searchBar.getText();
		Stage addWordStage = new Stage();
		Label label = new Label('"' + english + '"' + "means:");
		label.setStyle("-fx-font-weight: 900;");
		label.setMaxWidth(Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		TextField meaning = new TextField();
		meaning.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black");
		Button setButton = new Button("Set");
		setButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		setButton.getStyleClass().add("normalButton");
		setButton.setMaxWidth(Double.MAX_VALUE);
		setButton.setAlignment(Pos.CENTER);
		setButton.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black");
		Button closeButton = new Button("Close");
		closeButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		closeButton.getStyleClass().add("normalButton");
		setButton.setOnAction(event -> {
			String mean = meaning.getText();
			String line = english + "\t" + mean;
			FileUtil.AddtoFIle(ALOT_WORDS, line);
			trie.insert(english, mean);
			SugesstionUpdate.sugesstionUpdate(searchBar.getText(), trie, suggestionBox, searchBar);
			addWordStage.close();
		});
		closeButton.setStyle("-fx-font-weight: 900;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black;-fx-background-color: white");
		closeButton.setOnAction(event -> {
			addWordStage.close();
		});
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(closeButton, label, meaning, setButton);
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 6;-fx-border-color: black");
		Scene scene = new Scene(vbox, 200, 180);
		scene.setFill(Color.TRANSPARENT);
		addWordStage.initStyle(StageStyle.TRANSPARENT);
		addWordStage.setX(1080);
		addWordStage.setY(250);
		addWordStage.setResizable(false);
		addWordStage.setScene(scene);
		addWordStage.initModality(Modality.APPLICATION_MODAL);
		addWordStage.show();
	}

	@FXML
	private void DeleteClicked(ActionEvent event) {
		String word = searchBar.getText();
		System.out.println(word);
		if (word == "") {
			word = "b";
		}
		DeleteWord.deleteWord(word);
		trie.remove(word);
		SugesstionUpdate.sugesstionUpdate(searchBar.getText(), trie, suggestionBox, searchBar);
	}

	@FXML
	private void GameClicked() {
		gamebutton.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (gameMenu != null) {
					gameMenu.hide();
				}
				gameMenu = GameMenuController.loadGameMenu();
				gameMenu.show(gamebutton, event.getScreenX(), event.getScreenY());
			}
		});
	}

	@FXML
	private void ShutDownStage(ActionEvent event) {

		this.stage.close();

	}
	@FXML
	private void MinimizeStage(ActionEvent event){
      this.stage.setIconified(true);
	}

	@FXML
	private void TranslateClicked(ActionEvent event) throws IOException {
		if(TranslateBoxController.TranslateStage != null){TranslateBoxController.TranslateStage.close();TranslateBoxController.TranslateStage = null;}
		else{
			FXMLLoader loader = new FXMLLoader(TranslateBoxController.class.getResource("Translate.fxml"));
			Parent layout = loader.load();
			TranslateBoxController translateBoxController = loader.getController();
			translateBoxController.showTranslateStage(layout);
		}

	}

}