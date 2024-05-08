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
import javafx.scene.image.Image;
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
import myapp.Music.MusicPlayer;
import myapp.SuggestionBox.ContextMenuController;
import myapp.SuggestionBox.SugesstionUpdate;
import myapp.SuggestionBox.Words;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;
import myapp.Transition.ScaleTransitionForToggleButton;
import myapp.Translate.TranslateBoxController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {

	public SugesstionUpdate sugesstionUpdate;
	@FXML
	public TextField searchBar;
	@FXML
	public Button mouse;
	public boolean isInternetConnected;
	public int currentTutorialPage = 0;
	Main main = new Main();
	protected Stage MainStage;
	protected List<Stage> ownedStages = new ArrayList<>();
	private String SuggestionModified = "normal";
	private Stage addWordStage;
	private Words words;

	//Log box
	public Stage LogStage;
	@FXML
	private ToggleButton  logButton;
	private LogController log;
	//

	@FXML
	private Text LeftClick, RightClick;
	@FXML
	private VBox suggestionBox;
	@FXML
	private HBox PopUpBox;
	@FXML
	private Label Date, Time;

	@FXML
	private Button Github, gamebutton, shutdown, minimize, translate, add, delete, Internet, settingButton, TutorialButton;

	//Nhạc
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
	@FXML
	private VBox MusicBox;
	@FXML
	private ToggleButton  hide;
	@FXML
	private ToggleButton play;
	@FXML
	private Button previous, next;
	@FXML
	private Slider volumeslider;
	@FXML
	private Label nameOfSong;
	private boolean isMusicBoxHidden = false;
	//

	public GameMenuController gameMenuController = new GameMenuController();
	private ContextMenu gameMenu = gameMenuController.loadGameMenu();
	private final SettingMenuController settingMenuController = new SettingMenuController();
	public ContextMenu settingMenu = settingMenuController.loadSettingMenu();

	private final List<Image> TutorialImages = new ArrayList<>();
	private PicturePlayer TutorialPicturePLayer;

	public Stage getMainStage() {
		return MainStage;
	}
	public List<Stage> getOwnedStages() {
		return ownedStages;
	}
	public void addOwnedStage(Stage stage) {
		ownedStages.add(stage);
	}
	public void setMainStage(Stage mainStage) {
		MainStage = mainStage;
	}

	public Stage getAddWordStage() {
		return addWordStage;
	}

	public Words getWords() {
		return words;
	}

	public LogController getLog() {
		return log;
	}

	public void setLeftClick(String leftClicktext) {
		LeftClick.setText(leftClicktext);
	}

	public void setRightClick(String rightClicktext) {
		RightClick.setText(rightClicktext);
	}

	public Label getDate() {
		return Date;
	}

	public Label getTime() {
		return Time;
	}

	public String getSuggestionModified() {
		return this.SuggestionModified;
	}

	public void setSuggestionModified(String suggestionModified) {
		this.SuggestionModified = suggestionModified;
	}

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			//Load ảnh tutorial
			for (int index = 0; index <= 15; index++) {
				URL picurl = getClass().getResource("Tutorial/Tutorial" + (index) + ".png");
				Image test = new Image(picurl.toString());
				TutorialImages.add(test);
			}
			TutorialPicturePLayer = new PicturePlayer(TutorialImages);
			TutorialPicturePLayer.setMainStage(MainStage);
			TutorialPicturePLayer.createStage();
			//

			//Thao tác chuột
			mouse.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
			MainStage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					mouse.getStyleClass().remove("mouse-left-click");
					mouse.getStyleClass().add("mouse");
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					mouse.getStyleClass().remove("mouse-right-click");
					mouse.getStyleClass().add("mouse");
				}

			});
			MainStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					mouse.getStyleClass().remove("mouse");
					mouse.getStyleClass().add("mouse-left-click");
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					mouse.getStyleClass().remove("mouse");
					mouse.getStyleClass().add("mouse-right-click");
				}

			});
		});
		//

		sugesstionUpdate = new SugesstionUpdate(suggestionBox);
		DateandTime();
		audioPLayer();
		words = new Words();
		//update suggestionBox theo dữ liệu nhập từ bàn phím
		searchBar.textProperty().addListener((observable, oV, nV) -> {
			if (nV.isEmpty()) {
				suggestionBox.getChildren().clear();
			} else {
				sugesstionUpdate.sugesstionUpdate(nV, words, suggestionBox, searchBar, SuggestionModified);
				suggestionBox = sugesstionUpdate.getSuggestionBox();
			}
		});
		//

		applyHover();//áp dụng hiệu ứng hover cho các nút của stage chính
		Internetcheck();




	}

	//Kiểm tra mạng...
	public void Internetcheck() {
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
	//

    //Khởi động Stage của nút thêm từ khi ấn
	@FXML
	private void AddClicked() {
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
				new String[]{"Save word", "Close"},
				new String[]{"", ""}

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
					Words.add_word(english, mean, true);

					return null;
				}
			};
			sugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar, SuggestionModified);
			rederTask.setOnSucceeded(event2 -> {
				addWordStage.close();
				POPUP("Add word success!", true);
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

	//Xóa từ trong data (từ + nghĩa)
	@FXML
	private void DeleteClicked(ActionEvent event) {
		String word = searchBar.getText();
		System.out.println(word);
		if (word == "") {
			return;
		}

		Words.delete_word(word, true);

		sugesstionUpdate.sugesstionUpdate(searchBar.getText(), words, suggestionBox, searchBar, SuggestionModified);
		searchBar.setText("");
	}

	//Ấn game
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

	//Ấn setting
	@FXML
	private void SettingClicked() {
		settingButton.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (settingMenu != null) {
					settingMenu.hide();
				}
				settingMenu = settingMenuController.loadSettingMenu();
				settingMenu.show(settingButton, event.getScreenX(), event.getScreenY() - 100);
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

	//Ấn dịch
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

	//Áp dụng hiệu ứng hover cho từng nút của stage chính
	private void applyHover() {
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{gamebutton, settingButton, shutdown, minimize, translate, add, delete, previous, next, TutorialButton, Github},
				new String[]{"Choose game", "Setting menu", "Close app", "Minimize app", "Translation box", "Add chosen word", "Delete chosen word", "Previous song", "Next song", "Instruction", "Github links "},
				new String[]{"", "", "", "", "", "", "", "", "", "", ""}
		);
		scaleTransition.applyScaleTransition();
		scaleTransition = new ScaleTransitionForToggleButton(
				new ToggleButton[]{play, hide, logButton},
				new String[]{"Play song", "Hide/Unhide music", "Hide/Unhide LogBox"},
				new String[]{"", "", ""}
		);
		scaleTransition.applyScaleTransition();
	}

    //Cập nhật ngày tháng
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

	//Audio player
	public void audioPLayer() {
		volumeslider.setOnMouseEntered(e -> {
			setLeftClick("Change music volume");
		});
		volumeslider.setOnMouseExited(e -> {
			setLeftClick("");
		});
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

	//Ẩn/hiện nhạc
	private void toggleMusicBoxVisibility() {
		TranslateTransition slideTransition = new TranslateTransition(Duration.millis(250), MusicBox);
		slideTransition.setInterpolator(Interpolator.EASE_OUT);

		if (isMusicBoxHidden) {
			slideTransition.setToX(0);
		} else {
			slideTransition.setToX(MusicBox.getWidth() - 30);
		}

		slideTransition.play();
		isMusicBoxHidden = !isMusicBoxHidden;
	}

	//Hiện PopUp
	public void POPUP(String popUpString, Boolean isSuccess) {
		PopUpBox.setVisible(false);
		PopUpBox.getChildren().clear();
		String popUpColor;
		if (isSuccess) {
			popUpColor = "#7aea7a";
		} else {
			popUpColor = "rgb(190,36,36)";
		}
		Text label = new Text(popUpString);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		PopUpBox.getChildren().add(label);
		PopUpBox.setStyle("-fx-border-width: 3; -fx-border-color:rgb(7, 17, 17);-fx-border-radius: 10;-fx-background-radius: 10; -fx-background-color:" + popUpColor + ";");
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

	//Ẩn hiện log box - nhật ký
	public void logShow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogBox.fxml"));
		Parent layout = loader.load();
		LogStage = new Stage();
		LogStage.initOwner(MainStage);
		ownedStages.add(MainStage);
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

	//Ấn nút hướng dẫn
	@FXML
	private void tutorialClicked() {
		TutorialPicturePLayer.BlurMainStage();
		TutorialPicturePLayer.BlurOwnedStages(getOwnedStages());
		TutorialPicturePLayer.showStage();

	}

	//Ấn nút GitHub
	@FXML
	private void GitHubClicked() {
		main.openLinksInBrowser();
	}


}
