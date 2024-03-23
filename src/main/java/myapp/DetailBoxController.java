package myapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class DetailBoxController {

    List<API.Meaning>meanings;
    List<API.Phonetic>phonetics;
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
		updateUI();
	}

	public void updateUI() {
        API api=API.fetchWordDetails(word);
		List<API.Meaning> meanings = API.fetchMeanings(api,word);
		List<API.Phonetic> phonetics = API.fetchPhonetics(api,word);

		addLabel("Word: " + word);

		addLabel("Meanings:");
		for (API.Meaning meaning : meanings) {
			addLabel(" -Part of Speech: " + meaning.getPartOfSpeech());
			for (API.Definition definition : meaning.getDefinitions()) {
				addLabel("   +Definition: " + definition.getDefinition());
				addLabel("   +Example: " + (definition.getExample() != null ? definition.getExample() : "N/A"));
			}
		}

		addLabel("Phonetics:");
		for (API.Phonetic phonetic : phonetics) {
			addLabel(" -IPA: " + (phonetic.getText() != null ? phonetic.getText() : "N/A"));
			addHyperLink(phonetic.getAudio());
		}
	}

	private void addLabel(String text) {
		Text label = new Text(text);

		infoVBox.getChildren().add(label);
	}
	private void addHyperLink(String text) {

		if(text.length()!=0) {
			Button audio = new Button();
			audio.getStylesheets().add(DetailBoxController.class.getResource("Button.css").toExternalForm());
			audio.getStyleClass().add("audioButton");
			final Media sound = new Media(text);
			final MediaPlayer mediaPlayer = new MediaPlayer(sound);
			audio.setOnAction(
					e -> {mediaPlayer.seek(mediaPlayer.getStartTime());mediaPlayer.play();}
			);

			infoVBox.getChildren().add(audio);
		}
		else{addLabel(" Audio: N/A");}
	}

	@FXML
	public void close() {
		stage.close();
	}
	@FXML
	public void resizeUP(){stage.setHeight(500);stage.setWidth(1200);}
	@FXML
	public void resizeDOWN(){stage.setHeight(500);stage.setWidth(385);}
}