package myapp;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.Game.GameMenuController;
import myapp.Game.WordMeaning.WordMeaning;
import myapp.Music.MusicPlayer;
import myapp.SuggestionBox.ContextMenuController;
import myapp.SuggestionBox.SugesstionUpdate;
import myapp.SuggestionBox.Words;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;
import myapp.Transition.ScaleTransitionForToggleButton;
import myapp.Translate.TranslateBoxController;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

	private   Stage MainStage;

	public Stage getMainStage() {
		return MainStage;
	}

	public void setMainStage(Stage mainStage) {
		MainStage = mainStage;
	}
	private String modified = "normal";

	public Stage getAddWordStage() {
		return addWordStage;
	}
	private   Stage addWordStage;
	private   Words words;

	public Words getWords() {
		return words;
	}

	private   LogController log;

	public LogController getLog() {
		return log;
	}

	private final List<String> songs = Arrays.asList(
			"music/Ichika Nito - Away (Official Music Video).mp3",
			"music/The World is still Beautiful.mp3",
			"music/Forever.mp3"
	);
	private final List<String> songnames = Arrays.asList(
			"Away",
			"TWisBeautiful",
			"Forever"
	);
	public SugesstionUpdate sugesstionUpdate;
	@FXML
	public TextField searchBar;
	public GameMenuController gameMenuController = new GameMenuController();
	@FXML
	private Text LeftClick,RightClick;

	public void setLeftClick(String leftClicktext) {
		LeftClick.setText(leftClicktext);
	}

	public void setRightClick(String rightClicktext) {
		RightClick.setText(rightClicktext);
	}

	public Stage LogStage;
	@FXML
	private VBox MusicBox;
	@FXML
	private ToggleButton play, hide, logButton;
	@FXML
	private VBox suggestionBox;
	@FXML
	private HBox PopUpBox;
	@FXML
	private Label Date, Time;
	@FXML
	public Button mouse;
	@FXML
	private Button gamebutton, shutdown, minimize, translate, add, delete, previous, next,Internet,settingButton;
	@FXML
	private Slider volumeslider;
	@FXML
	private Label nameOfSong;
	private boolean isHidden = false;
	public  boolean isInternetConnected;

	public Label getDate() {
		return Date;
	}

	public Label getTime() {
		return Time;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}
	public  String getModified() {
		return this.modified;
	}

	@FXML
	private void initialize() {
		Platform.runLater(()->{
            WordMeaning wordMeaning1 =new WordMeaning("no_audio");
            System.out.println(wordMeaning1.getQuestion());
            String[] choices1=wordMeaning1.getChoices();
            Boolean[] answers=wordMeaning1.getAnswers();
            for (String s:choices1){

                System.out.println(s);

            }
            for(Boolean b:answers){

                System.out.println(b);

            }

            System.out.println("//////////////////////////////////");

            WordMeaning wordMeaning2 =new WordMeaning("audio");
            System.out.println(wordMeaning2.getQuestion());
            String[] choices2=wordMeaning2.getChoices();
            Boolean[] answers2=wordMeaning2.getAnswers();

            System.out.println(wordMeaning2.getAudio_link_answer());
            for (String s:choices2){

                System.out.println(s);

            }

            for (Boolean b:answers2){

                System.out.println(b);

            }


			mouse.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
			MainStage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
				if (event.getButton() == MouseButton.PRIMARY){
					mouse.getStyleClass().remove("mouse-left-click");
					mouse.getStyleClass().add("mouse");
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					mouse.getStyleClass().remove("mouse-right-click");
					mouse.getStyleClass().add("mouse");
				}

			});
			MainStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if (event.getButton() == MouseButton.PRIMARY){
					mouse.getStyleClass().remove("mouse");
					mouse.getStyleClass().add("mouse-left-click");
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					mouse.getStyleClass().remove("mouse");
					mouse.getStyleClass().add("mouse-right-click");
				}

			});
		});
		sugesstionUpdate = new SugesstionUpdate(suggestionBox);
		DateandTime();
		audioPLayer();
		words = new Words();
		searchBar.textProperty().addListener((observable, oV, nV) -> {
			if (nV.isEmpty()) {
				suggestionBox.getChildren().clear();
			} else {
				sugesstionUpdate.sugesstionUpdate(nV, words, suggestionBox, searchBar,modified);
				suggestionBox = sugesstionUpdate.getSuggestionBox();
			}
		});
		applyHover();
		Internetcheck();
		volumeslider.setOnMouseEntered(e->{
			setLeftClick("Change music volume");
		});
		volumeslider.setOnMouseExited(e->{
			setLeftClick("");
		});


	}
	public void Internetcheck(){
		Internet.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		if (InternetConnectionService.isInternetConnected()) {
			Internet.getStyleClass().remove("NoInternet");
			Internet.getStyleClass().add("YesInternet");
			isInternetConnected = true;
		} else {
			Internet.getStyleClass().remove("YesInternet");
			Internet.getStyleClass().add("NoInternet");
			isInternetConnected = false;
		}
	}

	@FXML
	private void AddClicked() throws IOException {
		addWordStage = new Stage();
		addWordStage.initOwner(MainStage);
		String english = searchBar.getText();
		Label label = new Label('"' + english + '"' + "means:");
		label.setStyle("-fx-font-weight: 900;");
		label.setMaxWidth(Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		TextField meaning = new TextField();
		meaning.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black");
		Button setButton = new Button("Set");
		setButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		setButton.getStyleClass().add("normalButton");
		setButton.setMaxWidth(Double.MAX_VALUE);
		setButton.setAlignment(Pos.CENTER);
		setButton.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black");
		Button closeButton = new Button("Close");
		ScaleTransitionForButton scaleTransitionForButton = new ScaleTransitionForButton(
				new Button[]{setButton, closeButton},
				new String[]{"Save word","Close"},
				new String[]{"",""}

		);
		scaleTransitionForButton.applyScaleTransition();

		setButton.setOnAction(event -> {
			String mean = meaning.getText();
			log.Update(Date.getText() + " " + Time.getText() + " ( " + "Add " + english + ": " + mean + ")");
			ProgressBar progressBar = new ProgressBar();
			progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
			progressBar.getStyleClass().add("progress-bar");
			Text loadingText = new Text("Loading.....");
			loadingText.setStyle("-fx-font-weight:900");
			StackPane loadingPane = new StackPane();
			loadingPane.getChildren().add(progressBar);
			loadingPane.getChildren().add(loadingText);
			loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 4;-fx-background-radius: 30; -fx-border-radius:  20");
			Scene loadingscene = new Scene(loadingPane, 200, 180);
			addWordStage.setScene(loadingscene);
			Task<Scene> rederTask = new Task<>() {
				@Override
				protected Scene call() throws Exception {
					Words.add_word(english, mean,true);

					return null;
				}
			};
			sugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar,modified);
			rederTask.setOnSucceeded(event2 -> {
				addWordStage.close();
				POPUP("Add word success!",true);
			});
			new Thread(rederTask).start();

		});
		closeButton.setStyle("-fx-font-weight: 900;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black;-fx-background-color: white");
		closeButton.setOnAction(event -> {
			addWordStage.close();
		});
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(closeButton, label, meaning, setButton);
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black");
		Scene scene = new Scene(vbox, 200, 180);
		scene.setFill(Color.TRANSPARENT);
		addWordStage.initStyle(StageStyle.TRANSPARENT);
		addWordStage.setX(1080);
		addWordStage.setY(280);
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
			return;
		}

		Words.delete_word(word,true);

		sugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar,modified);
		searchBar.setText("");
	}
	private  ContextMenu gameMenu = gameMenuController.loadGameMenu();

	@FXML
	private void GameClicked() {
		gamebutton.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (gameMenu != null) {
					gameMenu.hide();
				}
				gameMenu = gameMenuController.loadGameMenu();
				gameMenu.show(gamebutton, event.getScreenX(), event.getScreenY());
			}
		});

	}
	private SettingMenuController settingMenuController= new SettingMenuController();
	public ContextMenu settingMenu = settingMenuController.loadSettingMenu();
	@FXML
	private void SettingClicked(){
		settingButton.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (settingMenu != null) {
					settingMenu.hide();
				}
				settingMenu = settingMenuController.loadSettingMenu();
				settingMenu.show(settingButton, event.getScreenX(), event.getScreenY()-100);
			}
		});
	}

	@FXML
	private void ShutDownStage(ActionEvent event) {


		MainStage.close();

	}

	@FXML
	private void MinimizeStage(ActionEvent event) {
		MainStage.setIconified(true);
	}

	@FXML
	private void TranslateClicked(ActionEvent event) throws IOException {
		if (TranslateBoxController.TranslateStage != null) {
			TranslateBoxController.TranslateStage.close();
			TranslateBoxController.TranslateStage = null;
		} else {
			FXMLLoader loader = new FXMLLoader(TranslateBoxController.class.getResource("/myapp/Translate.fxml"));
			Parent layout = loader.load();
			TranslateBoxController translateBoxController = loader.getController();
			translateBoxController.showTranslateStage(layout);
		}

	}

	private void applyHover() {
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{gamebutton, settingButton, shutdown, minimize, translate, add, delete, previous, next},
				new String[]{"Choose game","Setting menu","Close app","Minimize app","Translation box","Add chosen word","Delete chosen word","Previous song","Next song"},
				new String[]{"","","","","","","","",""}
		);
		scaleTransition.applyScaleTransition();
		scaleTransition = new ScaleTransitionForToggleButton(
				new ToggleButton[]{play,hide,logButton},
				new String[]{"Play song","Hide/Unhide music","Hide/Unhide LogBox"},
				new String[]{"","",""}
		);
		scaleTransition.applyScaleTransition();
	}

	public void DateandTime() {
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				String[] DateandTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).split(" ");
				Date.setText(DateandTime[0]);
				Time.setText(DateandTime[1]);
			}
		};
		timer.start();
	}

	//audio player
	public void audioPLayer() {
		MusicPlayer musicPlayer = new MusicPlayer(play, next, previous, volumeslider, songs, songnames, nameOfSong);
		musicPlayer.setup();
		play = musicPlayer.getPlay();
		next = musicPlayer.getNext();
		previous = musicPlayer.getPrevious();
		volumeslider = musicPlayer.getVolumeslider();
		nameOfSong = musicPlayer.getNameOfSong();
		hide.setOnAction(event -> {
			if (hide.isSelected()) {
				play.setVisible(false);
				next.setVisible(false);
				previous.setVisible(false);
				volumeslider.setVisible(false);
				nameOfSong.setVisible(false);
				hide.setText("◀");
				toggleMusicBoxVisibility();
			} else {
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

	private void toggleMusicBoxVisibility() {
		TranslateTransition slideTransition = new TranslateTransition(Duration.millis(250), MusicBox);
		slideTransition.setInterpolator(Interpolator.EASE_OUT);

		if (isHidden) {
			slideTransition.setToX(0);
		} else {
			slideTransition.setToX(MusicBox.getWidth() - 30);
		}

		slideTransition.play();
		isHidden = !isHidden;
	}
	public void POPUP(String popUpString,Boolean isSuccess){
		PopUpBox.setVisible(false);
		PopUpBox.getChildren().clear();
		String popUpColor;
		if(isSuccess){popUpColor = "#7aea7a";}
		else{popUpColor = "rgb(190,36,36)";}
		Text label = new Text(popUpString);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		PopUpBox.getChildren().add(label);
		PopUpBox.setStyle("-fx-border-width: 3; -fx-border-color:rgb(7, 17, 17);-fx-border-radius: 10;-fx-background-radius: 10; -fx-background-color:"+popUpColor + ";");
		PopUpBox.setVisible(true);
		FadeTransition ft = new FadeTransition(Duration.seconds(3.0), PopUpBox);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);

		ft.setOnFinished(e -> {
			PopUpBox.setVisible(false);
			PopUpBox.setStyle("-fx-border-width: 3; -fx-border-color:rgb(7, 17, 17);-fx-border-radius: 10; -fx-background-color: transparent ");
			PopUpBox.getChildren().clear();

		});
		ft.play();
	}

	public void logShow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogBox.fxml"));
		Parent layout = loader.load();
		LogStage = new Stage();
		LogStage.initOwner(MainStage);
		LogStage.initModality(Modality.NONE);
		Scene scene = new Scene(layout, 440, 505);
		scene.setFill(Color.TRANSPARENT);
		LogStage.setScene(scene);
		LogStage.setResizable(false);
		LogStage.initStyle(StageStyle.TRANSPARENT);
		MainStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustLogBoxPosition(-905, 225);
		});

		MainStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustLogBoxPosition(-905, 225);
		});

		adjustLogBoxPosition(-905, 225);
		LogController logController = loader.getController();
		log = logController;
		logController.setLogStage(LogStage);
		logController.setScene(scene);
		logButton.setOnAction(event -> {
			if (logButton.isSelected()) {
				if (!LogStage.isShowing()) {
					LogStage.show();
				}
				logController.animateStage("OPEN");
				logButton.setText("▲");
			} else {
				logButton.setText("▼");
				logController.animateStage("CLOSE");
			}
		});

	}

	private void adjustLogBoxPosition(double offsetX, double offsetY) {
		if (LogStage != null && MainStage != null) {
			LogStage.setX(MainStage.getX() + MainStage.getWidth() + offsetX);
			LogStage.setY(MainStage.getY() + offsetY);
		}
	}

}
