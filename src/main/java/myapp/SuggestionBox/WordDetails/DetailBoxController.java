package myapp.SuggestionBox.WordDetails;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import myapp.Main;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.sql.SQLException;

public class DetailBoxController {

	private Stage stage;

	private String word;

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

		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{close},
				new String[]{"Close"},
				new String[]{""}
		);
		scaleTransition.applyScaleTransition();
		this.word = word;
		if(word == null){return;}
		updateUI();
	}

	public void updateUI() throws SQLException {

        WordDetails wordDetails=new WordDetails(word);
        for(WordDetails.Homonyms homonyms:wordDetails.allWord_details){
            addLabel("Nghĩa "+homonyms.getHomonyms()+"(Homonyms "+homonyms.getHomonyms()+"): ",false);
            addLabel("  .Phonetic",false);
            for(WordDetails.Phonetic phonetic:homonyms.getAllPhonetic()){

                addLabel("  -Kiểu:  "+(phonetic.getType() != null ? phonetic.getType() : "N/A"),false);
                addLabel("  -IPA:  "+(phonetic.getIPA() != null ? phonetic.getIPA() : "N/A"),false);
                addHyperLink((phonetic.getLink() != null ? phonetic.getLink() : "N/A"));

            }
            addLabel("",false);
            addLabel("  .Grammar",false);
            for (WordDetails.PoS poS:homonyms.getAllPoS()){

                addLabel("-    Loại từ: "+poS.getpOs(),false);
                for (WordDetails.Pair pair:poS.getPairs()){

                    if(pair.getDefiniton()==null){
                        break;
                    }
					boolean isCopyable =true;
                    addLabel("    +   Định nghĩa: "+pair.getDefiniton(),true);
					if(pair.getExample() == null){isCopyable = false;}
                    addLabel("    +   Ví dụ: "+(pair.getExample() != null ? pair.getExample() : "N/A"),isCopyable);

                }

            }
            addLabel("",false);
            addLabel("",false);


        }
        if(!wordDetails.check){
            addLabel("Look like this words didn't add to the database :3",false);
        }

	}

	private void addLabel(String text,boolean isCopyable) {
		Text label = new Text(text);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		label.setWrappingWidth(330);
		if(isCopyable){
			Button copyButton = new Button("Copy below");
			copyButton.setStyle("-fx-border-width: 2;-fx-background-color: white;-fx-border-color: black;-fx-font-weight: 900;-fx-font-size: 8;-fx-background-radius :20;-fx-border-radius: 20 ");
			ScaleTransition scaleTransition= new ScaleTransitionForButton(
					new Button[]{copyButton},
					new String[]{"Copy"},
					new String[]{""}
			);
			scaleTransition.applyScaleTransition();
			copyButton.setOnAction(e->{
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(text);
				clipboard.setContent(content);
				Main.mainController.POPUP("Copied to your clipboard!",true);
			});
			infoVBox.getChildren().add(copyButton);
		}
		infoVBox.getChildren().add(label);
	}
	private void addHyperLink(String text) {

		if(text.length()!=0) {
			Button audio = new Button();
			ScaleTransition scaleTransition = new ScaleTransitionForButton(
					new Button[]{audio},
					new String[]{"Play audio"},
					new String[]{""}
			);
			scaleTransition.applyScaleTransition();
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
		else{addLabel(" Audio: N/A",false);}
	}

	@FXML
	public void close() throws SQLException {
		setWord(null);
		stage.close();
	}

}