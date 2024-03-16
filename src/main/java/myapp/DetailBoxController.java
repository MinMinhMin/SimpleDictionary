package myapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class DetailBoxController {

	private Stage stage;
	private String word;

	@FXML
	private Button closeButton;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private VBox infoVBox;
	@FXML
	private TabPane tp;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setWord(String word) {
		this.word = word;
		//updateUI();
	}

	public void updateUI() {
		List<API.Meaning> meanings = API.fetchMeanings(word);
		List<API.Phonetic> phonetics = API.fetchPhonetics(word);

		addLabel("Word: " + word);

		addLabel("Meanings:");
		for (API.Meaning meaning : meanings) {
			addLabel("  Part of Speech: " + meaning.getPartOfSpeech());
			for (API.Definition definition : meaning.getDefinitions()) {
				addLabel("    Definition: " + definition.getDefinition());
				addLabel("    Example: " + (definition.getExample() != null ? definition.getExample() : "N/A"));
			}
		}

		addLabel("Phonetics:");
		for (API.Phonetic phonetic : phonetics) {
			addLabel("  IPA: " + (phonetic.getText() != null ? phonetic.getText() : "N/A"));
			addLabel("  Audio: " + (phonetic.getAudio() != null ? phonetic.getAudio() : "N/A"));
		}
	}

	private void addLabel(String text) {
		Text label = new Text(text);

		infoVBox.getChildren().add(label);
	}

	@FXML
	public void close() {
		stage.close();
	}
}