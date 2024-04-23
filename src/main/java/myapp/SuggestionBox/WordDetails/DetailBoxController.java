package myapp.SuggestionBox.WordDetails;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import myapp.MainController;

import java.sql.SQLException;

public class DetailBoxController {

	private Stage stage;

	private static String word;

	public String getWord() {
		return word;
	}

	@FXML
	private VBox infoVBox;
	@FXML
	public Button close;


	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setWord(String word) throws SQLException {
		MainController.applyScaleTransition(close);
		this.word = word;
		if(word == null){return;}
		updateUI();
	}

	public void updateUI() throws SQLException {


        WordDetails wordDetails=new WordDetails(word);
        for(WordDetails.Homonyms homonyms:wordDetails.allWord_details){
            addLabel("Nghĩa "+homonyms.getHomonyms()+"(Homonyms "+homonyms.getHomonyms()+"): ");
            addLabel("  .Phonetic");
            for(WordDetails.Phonetic phonetic:homonyms.getAllPhonetic()){

                addLabel("  -Kiểu:  "+(phonetic.getType() != null ? phonetic.getType() : "N/A"));
                addLabel("  -IPA:  "+(phonetic.getIPA() != null ? phonetic.getIPA() : "N/A"));
                addHyperLink((phonetic.getLink() != null ? phonetic.getLink() : "N/A"));

            }
            addLabel("");
            addLabel("  .Grammar");
            for (WordDetails.PoS poS:homonyms.getAllPoS()){

                addLabel("-    Loại từ: "+poS.getpOs());
                for (WordDetails.Pair pair:poS.getPairs()){

                    if(pair.getDefiniton()==null){
                        break;
                    }
                    addLabel("    +   Định nghĩa: "+pair.getDefiniton());
                    addLabel("    +   Ví dụ: "+(pair.getExample() != null ? pair.getExample() : "N/A"));

                }

            }
            addLabel("");
            addLabel("");


        }
        if(!wordDetails.check){
            addLabel("Look like this words didn't add to the database :3");
        }

	}

	private void addLabel(String text) {
		Text label = new Text(text);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		label.setWrappingWidth(330);

		infoVBox.getChildren().add(label);
	}
	private void addHyperLink(String text) {

		if(text.length()!=0) {
			Button audio = new Button();
			MainController.applyScaleTransition(audio);
			audio.getStylesheets().add(DetailBoxController.class.getResource("/myapp/Styling.css").toExternalForm());
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
	public void close() throws SQLException {
		setWord(null);
		stage.close();
	}

}