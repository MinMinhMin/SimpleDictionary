package myapp;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainController {
	@FXML
	private ToggleButton play;
	@FXML
	public TextField searchBar;
	public ContextMenu gameMenu = GameMenuController.loadGameMenu();
	private Stage stage;
	@FXML
	private VBox suggestionBox;

    public static Words words;

	@FXML
	private Button gamebutton,shutdown,minimize,translate,add,delete,previous,next;
	@FXML
	private Slider volumeslider;
	private List<String> songs = Arrays.asList(
			"music/Runaway-Rim_cover-Dios.mp3",
			"music/The World is still Beautiful.mp3",
			"music/Tokyo Ghoul - Glassy Sky [東京喰種 -トーキョーグール-].mp3",
			"music/Ichika Nito - Away (Official Music Video).mp3",
			"music/Forever.mp3"
	);
	private List<String> songnames = Arrays.asList(
			"Runaway",
			"TWisBeautiful",
			"Glassy Sky",
			"Away",
			"Forever"
	);
	@FXML
	private Label nameOfSong ;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void initialize() {
		audioPLayer();
		words =new Words();
		searchBar.textProperty().addListener((observable, oV, nV) -> {
			if (nV.isEmpty()) {
				suggestionBox.getChildren().clear();
			} else {
				SugesstionUpdate.sugesstionUpdate(nV, words, suggestionBox, searchBar);
			}
		});
		applyHover();


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
		applyScaleTransition(setButton);
		setButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		setButton.getStyleClass().add("normalButton");
		setButton.setMaxWidth(Double.MAX_VALUE);
		setButton.setAlignment(Pos.CENTER);
		setButton.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black");
		Button closeButton = new Button("Close");
		applyScaleTransition(closeButton);
		closeButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		closeButton.getStyleClass().add("normalButton");
		setButton.setOnAction(event -> {
			String mean = meaning.getText();
            words.add_word(english,mean);
			SugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar);
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
        words.delete_word(word);
		SugesstionUpdate.sugesstionUpdate(searchBar.getText(),words, suggestionBox, searchBar);
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
	private void applyHover(){
		applyScaleTransition(gamebutton);
		applyScaleTransition(shutdown);
		applyScaleTransition(minimize);
		applyScaleTransition(translate);
		applyScaleTransition(add);
		applyScaleTransition(delete);
		applyScaleTransitionForToggleButton(play);
		applyScaleTransition(previous);
		applyScaleTransition(next);
	}
	public static void applyScaleTransition(Button button) {
		ScaleTransition stGrow = new ScaleTransition(Duration.millis(200), button);
		stGrow.setToX(0.9);
		stGrow.setToY(0.9);

		ScaleTransition stShrink = new ScaleTransition(Duration.millis(200), button);
		stShrink.setToX(1);
		stShrink.setToY(1);

		button.setOnMouseEntered(e -> {
			stGrow.playFromStart();
		});

		button.setOnMouseExited(e -> {
			stShrink.playFromStart();
		});
	}
	public static void applyScaleTransitionForToggleButton(ToggleButton button) {
		ScaleTransition stGrow = new ScaleTransition(Duration.millis(200), button);
		stGrow.setToX(0.9);
		stGrow.setToY(0.9);

		ScaleTransition stShrink = new ScaleTransition(Duration.millis(200), button);
		stShrink.setToX(1);
		stShrink.setToY(1);

		button.setOnMouseEntered(e -> {
			stGrow.playFromStart();
		});

		button.setOnMouseExited(e -> {
			stShrink.playFromStart();
		});
	}
	//audio player
	public void audioPLayer(){
		MusicPlayer musicPlayer = new MusicPlayer(play,next,previous,volumeslider,songs,songnames,nameOfSong);
		musicPlayer.setup();
		play = musicPlayer.getPlay();
		next=musicPlayer.getNext();
		previous = musicPlayer.getPrevious();
		volumeslider =  musicPlayer.getVolumeslider();
		nameOfSong = musicPlayer.getNameOfSong();

	}


}