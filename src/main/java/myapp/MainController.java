package myapp;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.Game.GameMenuController;
import myapp.Music.MusicPlayer;
import myapp.SuggestionBox.ContextMenuController;
import myapp.SuggestionBox.SugesstionUpdate;
import myapp.SuggestionBox.Words;
import myapp.Translate.TranslateBoxController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainController {
	private LogController log;

	public void setLog() throws IOException {
		log= new LogController();
	}

	@FXML
	private VBox MusicBox;
	@FXML
	private ToggleButton play,hide,logButton;
	@FXML
	public TextField searchBar;
	public ContextMenu gameMenu = GameMenuController.loadGameMenu();
	public static Stage MainStage;
	public  static  Stage addWordStage;
	@FXML
	private VBox suggestionBox;

    public static Words words;

	@FXML
	private Button gamebutton,shutdown,minimize,translate,add,delete,previous,next;
	@FXML
	private Slider volumeslider;
	private final List<String> songs = Arrays.asList(
			"music/Runaway-Rim_cover-Dios.mp3",
			"music/The World is still Beautiful.mp3",
			"music/Tokyo Ghoul - Glassy Sky [東京喰種 -トーキョーグール-].mp3",
			"music/Ichika Nito - Away (Official Music Video).mp3",
			"music/Forever.mp3"
	);
	private final List<String> songnames = Arrays.asList(
			"Runaway",
			"TWisBeautiful",
			"Glassy Sky",
			"Away",
			"Forever"
	);
	@FXML
	private Label nameOfSong ;
	@FXML
	private void initialize(){
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
		logButton.setOnAction(event -> {
			if(logButton.isSelected()){
				if(!log.LogStage.isShowing()){
					try {
						log.showLogBox(-930,190);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				logButton.setText("▲");
				log.animateStage("OPEN");
			}
			else{logButton.setText("▼");;log.animateStage("CLOSE");}
		});


	}

	@FXML
	private void AddClicked() {
		addWordStage = new Stage();
		String english = searchBar.getText();
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
		setButton.setOnAction(event -> {
			String mean = meaning.getText();

			ProgressBar progressBar = new ProgressBar();
			progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
			progressBar.getStyleClass().add("progress-bar");
			Text loadingText = new Text("Loading.....");
			loadingText.setStyle("-fx-font-weight:900");
			StackPane loadingPane = new StackPane();
			loadingPane.getChildren().add(progressBar);
			loadingPane.getChildren().add(loadingText);
			loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 10;-fx-background-radius: 30; -fx-border-radius:  20");
			Scene loadingscene = new Scene(loadingPane,200,180);
			addWordStage.setScene(loadingscene);
			Task<Scene> rederTask = new Task<>() {
				@Override
				protected Scene call() throws Exception {
					Words.add_word(english,mean);
					Thread.sleep(1000);

					return null;
				}
			};
			SugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar);
			rederTask.setOnSucceeded(event2 -> addWordStage.close());
			new Thread(rederTask).start();

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
        Words.delete_word(word);
		SugesstionUpdate.sugesstionUpdate(searchBar.getText(),words, suggestionBox, searchBar);
		searchBar.setText("");
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

		MainStage.close();

	}
	@FXML
	private void MinimizeStage(ActionEvent event){
      MainStage.setIconified(true);
	}

	@FXML
	private void TranslateClicked(ActionEvent event) throws IOException {
		if(TranslateBoxController.TranslateStage != null){TranslateBoxController.TranslateStage.close();TranslateBoxController.TranslateStage = null;}
		else{
			FXMLLoader loader = new FXMLLoader(TranslateBoxController.class.getResource("/myapp/Translate.fxml"));
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
		applyScaleTransitionForToggleButton(hide);
		applyScaleTransition(previous);
		applyScaleTransition(next);
		applyScaleTransitionForToggleButton(logButton);
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
		hide.setOnAction(event -> {
			if(hide.isSelected()){
				play.setVisible(false);
				next.setVisible(false);
				previous.setVisible(false);
				volumeslider.setVisible(false);
				nameOfSong.setVisible(false);
				hide.setText("◀");
				toggleMusicBoxVisibility();
			}else {
				hide.setText("▶");
				toggleMusicBoxVisibility();
				play.setVisible(true);
				next.setVisible(true);
				previous.setVisible(true);
				volumeslider.setVisible(true);
				nameOfSong.setVisible(true);

			}
		});

	}
	private boolean isHidden = false; // Initial state

	private void toggleMusicBoxVisibility() {
		TranslateTransition slideTransition = new TranslateTransition(Duration.millis(250), MusicBox);

		if (isHidden) {
			slideTransition.setToX(0);
		} else {
			slideTransition.setToX(MusicBox.getWidth()-30);
		}

		slideTransition.play();
		isHidden = !isHidden;
	}



}