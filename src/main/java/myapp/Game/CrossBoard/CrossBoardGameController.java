package myapp.Game.CrossBoard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import myapp.Game.GameMenuController;
import myapp.Main;
import myapp.PicturePlayer;
import myapp.SuggestionBox.Words;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CrossBoardGameController implements Initializable {
	final private MediaPlayer Correct = new MediaPlayer(new Media(new File("src/main/java/myapp/Game/CrossBoard/Correct.mp3").toURI().toString()));
	final private MediaPlayer Clicksound = new MediaPlayer(new Media(new File("src/main/java/myapp/Game/CrossBoard/CLick2.mp3").toURI().toString()));
	final private MediaPlayer DeClicksound = new MediaPlayer(new Media(new File("src/main/java/myapp/Game/CrossBoard/CLick.mp3").toURI().toString()));
	private Stage stage;
	@FXML
	private GridPane wordSearchGridPane;
	@FXML
	private Button refresh, close, tutorial;
	private List<String> wordsToFind;
	private final Map<String, String> hint = new HashMap<>();
	private final Map<String, Text> textMap = new HashMap<>();
	private List<Image> TutorialImages = new ArrayList<>();
	private PicturePlayer TutorialPicturePlayer;
	@FXML
	private Text word1, word2, word3, word4, point, choosenWord, Time;
	private final Words words = Main.mainController.getWords();
	private ToggleButton[][] buttons;
	private final List<ToggleButton> selectedButtons = new ArrayList<>();
	private int lastRow = -1, lastCol = -1;
	private boolean isRow = true;
	private final StringBuilder selectedText = new StringBuilder();
	private Set<String> wordsSet = new HashSet<>();
	private int Point = 0, finalPoint = 0;
	private int timeSeconds = 150;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void close() {
		stage.close();
	}

	public void setWordsToFind() {
		this.wordsToFind = words.get_4_random_words();
		this.wordsSet = new HashSet<>(this.wordsToFind);
		int index = 1;
		for (String word : this.wordsToFind) {


			this.hint.put(word, CrossBoard.getHint(word));
			if (index == 1) {
				textMap.put(word, word1);
				word1.setText(this.hint.get(word));
			}
			if (index == 2) {
				textMap.put(word, word2);
				word2.setText(this.hint.get(word));
			}
			if (index == 3) {
				textMap.put(word, word3);
				word3.setText(this.hint.get(word));
			}
			if (index == 4) {
				textMap.put(word, word4);
				word4.setText(this.hint.get(word));
			}

			index++;

		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Platform.runLater(()->{
			URL picurl = getClass().getResource("/myapp/Game/CrossBoard_tutorial.png");
			Image image = new Image(picurl.toString());
			TutorialImages.add(image);
			TutorialPicturePlayer = new PicturePlayer(TutorialImages);
			TutorialPicturePlayer.setMainStage(Main.mainController.getMainStage());
			TutorialPicturePlayer.createStage();
		});
		Clicksound.setOnEndOfMedia(() -> {
			Clicksound.seek(Duration.ZERO);
			Clicksound.stop();
		});
		DeClicksound.setOnEndOfMedia(() -> {
			DeClicksound.seek(Duration.ZERO);
			DeClicksound.stop();
		});
		Correct.setOnEndOfMedia(() -> {
			Correct.seek(Duration.ZERO);
			Correct.stop();
		});
		Time.setText(String.valueOf(timeSeconds));
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
			timeSeconds--;
			Time.setText(Integer.toString(timeSeconds));
			if (timeSeconds <= 0) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/myapp/WinCrossBoard.fxml"));
				try {
					Parent layout = loader.load();
					Scene scene = new Scene(layout, 360, 500);
					Win.Point = finalPoint;
					stage.setScene(scene);

				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				timeline.stop();
			}
		}));
		timeline.playFromStart();
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{close, refresh, tutorial},
				new String[]{"Close", "Restart", "how to play"},
				new String[]{"", "", ""}
		);
		scaleTransition.applyScaleTransition();

		setWordsToFind();
		buttons = new ToggleButton[10][8];

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 8; col++) {
				int finalRow = row;
				int finalCol = col;
				ToggleButton button = new ToggleButton();
				button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black");
				button.setOnAction(event -> {
					if (button.isSelected()) {
						Clicksound.play();
						if (selectedButtons.isEmpty()) {
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
						} else if (selectedButtons.size() == 1) {
							if ((finalRow == lastRow && Math.abs(finalCol - lastCol) == 1) ||
									(finalCol == lastCol && Math.abs(finalRow - lastRow) == 1)) {
								isRow = finalRow == lastRow;
								button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
								selectedButtons.add(button);
								selectedText.append(button.getText());
								lastRow = finalRow;
								lastCol = finalCol;
							} else {
								selectedButtons.get(0).setStyle("-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								selectedButtons.get(0).setSelected(false);
								selectedButtons.clear();
								selectedText.setLength(0);
								button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
								selectedButtons.add(button);
								selectedText.append(button.getText());
								lastRow = finalRow;
								lastCol = finalCol;
							}
						} else if ((isRow && finalRow == lastRow && Math.abs(finalCol - lastCol) == 1) ||
								(!isRow && finalCol == lastCol && Math.abs(finalRow - lastRow) == 1)) {
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
						} else {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								b.setSelected(false);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
							if (selectedButtons.size() == 2) {
								isRow = GridPane.getRowIndex(selectedButtons.get(0)) == GridPane.getRowIndex(selectedButtons.get(1));
							}
						}
						if (wordsSet.contains(selectedText.toString())) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("");
								b.setSelected(false);
								wordSearchGridPane.getChildren().remove(b);
							}
							textMap.get(selectedText.toString()).setText("✔");
							Correct.play();
							Point += 100;
							finalPoint += Point + Integer.valueOf(Time.getText());
							point.setText((String.valueOf(finalPoint)));
							wordsSet.remove(selectedText);
							selectedButtons.clear();
							selectedText.setLength(0);
							if (Point == 400) {
								timeSeconds = 1;
							}
						}
						choosenWord.setText(selectedText.toString());
						System.out.println("press: " + finalRow + ", " + finalCol + ", isRow: " + isRow + ", text: " + selectedText.toString());
					} else {
						DeClicksound.play();
						button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
						selectedButtons.remove(button);
						if (!selectedButtons.isEmpty()) {
							ToggleButton lastButton = selectedButtons.get(selectedButtons.size() - 1);
							lastRow = GridPane.getRowIndex(lastButton);
							lastCol = GridPane.getColumnIndex(lastButton);
							if (selectedButtons.size() >= 2) {
								isRow = GridPane.getRowIndex(selectedButtons.get(0)) == GridPane.getRowIndex(selectedButtons.get(1));
							}
							selectedText.setLength(0);
							for (ToggleButton b : selectedButtons) {
								selectedText.append(b.getText());
							}
						} else {
							lastRow = -1;
							lastCol = -1;
							selectedText.setLength(0);
						}
						if (!selectedButtons.isEmpty() &&
								((isRow && finalRow == lastRow) || (!isRow && finalCol == lastCol))) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								b.setSelected(false);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
						}
						if (wordsSet.contains(selectedText.toString())) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("");
								b.setSelected(false);
								wordSearchGridPane.getChildren().remove(b);
							}
							textMap.get(selectedText.toString()).setText("✔");
							Correct.play();
							Point += 100;
							finalPoint += Point + Integer.valueOf(Time.getText());
							point.setText((String.valueOf(finalPoint)));
							selectedButtons.clear();
							selectedText.setLength(0);
							if (Point == 400) {
								timeSeconds = 1;

							}
						}

						choosenWord.setText(selectedText.toString());
						System.out.println("release: " + finalRow + ", " + finalCol + ", isRow: " + isRow + ", text: " + selectedText.toString());
					}
				});
				button.setMinSize(40, 40);
				wordSearchGridPane.add(button, col, row);
				buttons[row][col] = button;
			}
		}

		populateWords();
	}


	private void populateWords() {


		CrossBoard crossBoard = new CrossBoard();

		for (String answer : wordsToFind) {
			crossBoard.addWord(answer);
			System.out.println(answer + " " + this.hint.get(answer));
		}
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 8; col++) {
				buttons[row][col].setText(String.valueOf(crossBoard.board[row][col]));
			}

		}

	}

	@FXML
	private void refreshStage() throws IOException {
		stage.close();
		GameMenuController gameMenuController = new GameMenuController();
		gameMenuController.showCrossBoardGameBox();
	}

	@FXML
	private void tutorialClicked() {
		TutorialPicturePlayer.BlurOwnedStages(Main.mainController.getOwnedStages());
		TutorialPicturePlayer.BlurMainStage();
		TutorialPicturePlayer.showStage();
	}

}



