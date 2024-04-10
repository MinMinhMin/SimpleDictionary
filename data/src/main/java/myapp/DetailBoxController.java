package myapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class DetailBoxController {

	private Stage stage;

	private static String word;

	public String getWord() {
		return word;
	}

	@FXML
	private VBox infoVBox;


	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setWord(String word) {
		this.word = word;
		if(word == null){return;}
		updateUI();
	}

	public void updateUI() {
        API api=API.fetchWordDetails(word);
		List<API.Meaning> meanings = API.fetchMeanings(api,word);
		List<API.Phonetic> phonetics = API.fetchPhonetics(api,word);

		addLabel("Word: " + word);
		addLabel("Phonetics:");
		for (API.Phonetic phonetic : phonetics) {
			addLabel(" -IPA: " + (phonetic.getText() != null ? phonetic.getText() : "N/A"));
			addHyperLink(phonetic.getAudio());
		}
		addLabel("Meanings:");
		for (API.Meaning meaning : meanings) {
			addLabel(" -Part of Speech: " + meaning.getPartOfSpeech());
			for (API.Definition definition : meaning.getDefinitions()) {
				addLabel("   +Definition: " + definition.getDefinition());
				addLabel("   +Example: " + (definition.getExample() != null ? definition.getExample() : "N/A"));
			}
		}
	}

	private void addLabel(String text) {
		Text label = new Text(text);
		label.setStyle("-fx-font-weight: 900");

		infoVBox.getChildren().add(label);
	}
	private void addHyperLink(String text) {

		if(text.length()!=0) {
			Button audio = new Button();
			audio.getStylesheets().add(DetailBoxController.class.getResource("Styling.css").toExternalForm());
			audio.getStyleClass().add("audioButton");
			final Media sound = new Media(text);
			final MediaPlayer mediaPlayer = new MediaPlayer(sound);
			audio.setOnAction(
					e -> {mediaPlayer.seek(mediaPlayer.getStartTime());mediaPlayer.play();
						System.out.println(text);}
			);
			infoVBox.getChildren().add(audio);
		}
		else{addLabel(" Audio: N/A");}
	}

	@FXML
	public void close() {
		setWord(null);
		stage.close();
	}
	@FXML
	public void resizeUP(){stage.setHeight(500);stage.setWidth(1275);}
	@FXML
	public void resizeDOWN(){stage.setHeight(500);stage.setWidth(385);}
}