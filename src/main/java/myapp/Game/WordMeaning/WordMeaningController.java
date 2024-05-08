package myapp.Game.WordMeaning;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import myapp.Game.GameMenuController;
import myapp.Main;
import myapp.PicturePlayer;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;
import myapp.Transition.ScaleTransitionForPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WordMeaningController {
	final public MediaPlayer GMAUDIO = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/GMAUDIO.mp3").toString()));
	final public MediaPlayer SLASHAUDIO = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/Slash.mp3").toString()));
	final public MediaPlayer BLOWAUDIO = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/BLOW.mp3").toString()));
	final public MediaPlayer SELECTIONAUDIO = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/selection.mp3").toString()));
	final public MediaPlayer CHOOSEAUDIO = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/Choose.mp3").toString()));
	final public MediaPlayer THEME = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/theme.mp3").toString()));
	final public Font pixelify = Font.loadFont(new FileInputStream(new File("src/main/java/myapp/Game/WordMeaning/[phongchuviet.com] FVF Fernando 08/FVF Fernando 08.ttf")), 10);
	final private String[] questionsType = new String[]{"no_audio", "audio", "no_audio", "audio", "audio", "no_audio", "no_audio"};
	protected Stage stage;
	private List<Image> TutorialImages = new ArrayList<>();
	private PicturePlayer TutorialPicturePlayer;
	private int questionsIndex = -1;
	private int BeingAtacked = 0;
	private int AttackShigure = 0;
	@FXML
	private Button close, refresh, tutorial;
	@FXML
	private Pane BLOW, health, Boss, QuestionGif, Clickme1, Clickme2, Clickme3, Clickme4, Next, Speaker, ShigureHealth, SlashAttack;
	private final ImageView blw = new ImageView();
	private final ImageView sls = new ImageView();
	private final List<WordMeaning> questions = new ArrayList<>();
	private int slashTime = 0;
	private int Stringindex = 0;
	private String GMString = "";
	@FXML
	private Text GM, Question, Answer1, Answer2, Answer3, Answer4, MarkAnswer1, MarkAnswer2, MarkAnswer3, MarkAnswer4, nextText, YourHP, speakerText, WaveCount, Wave, Name, ShigureAtkPoint, YourAtk;
	public WordMeaningController() throws FileNotFoundException {
	}

	public void setStage(Stage gameBoxStage) {
		this.stage = gameBoxStage;

	}

	@FXML
	private void close() {
		stage.close();
	}

	public void setMute() {
		GMAUDIO.stop();
		SLASHAUDIO.stop();
		BLOWAUDIO.stop();
		SELECTIONAUDIO.stop();
		THEME.stop();
		CHOOSEAUDIO.stop();
	}

	@FXML
	private void refreshStage() throws IOException {
		stage.close();
		GameMenuController gameMenuController = new GameMenuController();
		gameMenuController.showWordmeaningBox();
	}

	private void NextWave() {
		CHOOSEAUDIO.setOnEndOfMedia(null);
		CHOOSEAUDIO.seek(CHOOSEAUDIO.getStartTime());
		CHOOSEAUDIO.play();
		blw.setVisible(false);
		Answer1.setDisable(false);
		Answer2.setDisable(false);
		Answer3.setDisable(false);
		Answer4.setDisable(false);
		Next.setVisible(false);
		questionsIndex++;
		if (questionsIndex > 0 && questionsIndex < 7) {
			Boss.getChildren().clear();
			CreateImage shigureChan = new CreateImage("/myapp/Game/shigure" + (questionsIndex + 1) + ".gif", 200, 200);
			Boss.getChildren().add(shigureChan.to_image());
		}
		Question.setText(questions.get(questionsIndex).getQuestion());
		String[] ans = questions.get(questionsIndex).getChoices();
		Boolean[] answers = questions.get(questionsIndex).getAnswers();
		Answer1.setVisible(true);
		Answer2.setVisible(true);
		Answer3.setVisible(true);
		Answer4.setVisible(true);
		Answer1.setText(ans[0]);
		Answer2.setText(ans[1]);
		Answer3.setText(ans[2]);
		Answer4.setText(ans[3]);
		if (questionsType[questionsIndex] == "audio") {

			Speaker.setVisible(true);
			System.out.println(questions.get(questionsIndex).getAudio_link_answer());
			final Media sound = new Media(questions.get(questionsIndex).getAudio_link_answer());
			final MediaPlayer mediaPlayer = new MediaPlayer(sound);
			Speaker.setOnMouseClicked(e -> {
				CHOOSEAUDIO.seek(CHOOSEAUDIO.getStartTime());
				CHOOSEAUDIO.play();
				CHOOSEAUDIO.setOnEndOfMedia(() -> {
					mediaPlayer.seek(mediaPlayer.getStartTime());
					THEME.setVolume(0.1);
					mediaPlayer.play();
					mediaPlayer.setOnEndOfMedia(() -> {
						THEME.setVolume(0.7);
					});
				});


			});
		} else {
			Speaker.setVisible(false);
		}
		WaveCount.setText("[" + (questionsIndex + 1) + "/" + "7]");
		Answer1.setOnMouseClicked(e -> {
			CHOOSEAUDIO.setOnEndOfMedia(null);
			Answer1.setDisable(true);
			Answer2.setVisible(false);
			Answer3.setVisible(false);
			Answer4.setVisible(false);
			BLOWAUDIO.setVolume(0.2);
			if (!answers[0]) {
				BeingAtacked++;
				if (BeingAtacked == 4) {
					setMute();
					Lose();
				}
				GameMaster("Shigure-chan " + "\n" + "attack you!!");
				health.getChildren().clear();
				CreateImage createImage = new CreateImage("/myapp/Game/player-health-" + (BeingAtacked) + ".png", 220, 50);
				health.getChildren().add(createImage.to_image());
				blw.setVisible(true);
				BLOWAUDIO.seek(BLOWAUDIO.getStartTime());
				BLOWAUDIO.play();
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(event -> {
					blw.setVisible(false);
					if (BeingAtacked == 3) {
						setMute();
						Lose();
					}
				});
				pause.play();
				Boss.getChildren().clear();
				CreateImage shigureChan = new CreateImage("/myapp/Game/SHINE.gif", 200, 200);
				Boss.getChildren().add(shigureChan.to_image());

			} else {
				AttackShigure();
				GameMaster("You attack" + "\n" + "Shigure-chan!!");
				ShigureHealth.getChildren().clear();
				AttackShigure++;
				CreateImage createImage = new CreateImage("/myapp/Game/shigure-health-" + (AttackShigure) + ".jpg", 170, 60);
				ShigureHealth.getChildren().add(createImage.to_image());
				BLOWAUDIO.play();
				PauseTransition pause = new PauseTransition(Duration.seconds(2.2));
				pause.setOnFinished(event -> {
					if (AttackShigure == 5) {
						setMute();
						Win();
					}
				});
				pause.play();
			}

		});
		Answer2.setOnMouseClicked(e -> {
			CHOOSEAUDIO.setOnEndOfMedia(null);
			Answer2.setDisable(true);
			Answer1.setVisible(false);
			Answer3.setVisible(false);
			Answer4.setVisible(false);
			if (!answers[1]) {
				BeingAtacked++;
				if (BeingAtacked == 4) {
					setMute();
					Lose();
				}
				GameMaster("Shigure-chan" + "\n" + "attack you!!");
				health.getChildren().clear();
				CreateImage createImage = new CreateImage("/myapp/Game/player-health-" + (BeingAtacked) + ".png", 220, 50);
				health.getChildren().add(createImage.to_image());
				blw.setVisible(true);
				BLOWAUDIO.seek(BLOWAUDIO.getStartTime());
				BLOWAUDIO.play();
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(event -> {
					blw.setVisible(false);
					if (BeingAtacked == 3) {
						setMute();
						Lose();
					}
				});
				pause.play();
				Boss.getChildren().clear();
				CreateImage shigureChan = new CreateImage("/myapp/Game/SHINE.gif", 200, 200);
				Boss.getChildren().add(shigureChan.to_image());

			} else {
				AttackShigure();
				GameMaster("You attack" + "\n" + "Shigure-chan!!");
				ShigureHealth.getChildren().clear();
				AttackShigure++;
				CreateImage createImage = new CreateImage("/myapp/Game/shigure-health-" + (AttackShigure) + ".jpg", 170, 60);
				ShigureHealth.getChildren().add(createImage.to_image());
				PauseTransition pause = new PauseTransition(Duration.seconds(2.2));
				pause.setOnFinished(event -> {
					if (AttackShigure == 5) {
						setMute();
						Win();
					}
				});
				pause.play();
			}
		});
		Answer3.setOnMouseClicked(e -> {
			CHOOSEAUDIO.setOnEndOfMedia(null);
			Answer3.setDisable(true);
			Answer1.setVisible(false);
			Answer2.setVisible(false);
			Answer4.setVisible(false);
			if (!answers[2]) {
				BeingAtacked++;
				if (BeingAtacked == 4) {
					setMute();
					Lose();
				}
				GameMaster("Shigure-chan" + "\n" + "attack you!!");
				health.getChildren().clear();
				CreateImage createImage = new CreateImage("/myapp/Game/player-health-" + (BeingAtacked) + ".png", 220, 50);
				health.getChildren().add(createImage.to_image());
				blw.setVisible(true);
				BLOWAUDIO.seek(BLOWAUDIO.getStartTime());
				BLOWAUDIO.play();
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(event -> {
					blw.setVisible(false);
					if (BeingAtacked == 3) {
						setMute();
						Lose();
					}
				});
				pause.play();
				Boss.getChildren().clear();
				CreateImage shigureChan = new CreateImage("/myapp/Game/SHINE.gif", 200, 200);
				Boss.getChildren().add(shigureChan.to_image());

			} else {
				AttackShigure();
				GameMaster("You attack" + "\n" + "Shigure-chan!!");
				ShigureHealth.getChildren().clear();
				AttackShigure++;
				CreateImage createImage = new CreateImage("/myapp/Game/shigure-health-" + (AttackShigure) + ".jpg", 170, 60);
				ShigureHealth.getChildren().add(createImage.to_image());
				PauseTransition pause = new PauseTransition(Duration.seconds(2.2));
				pause.setOnFinished(event -> {
					if (AttackShigure == 5) {
						setMute();
						Win();
					}
				});
				pause.play();
			}
		});
		Answer4.setOnMouseClicked(e -> {
			CHOOSEAUDIO.setOnEndOfMedia(null);
			Answer4.setDisable(true);
			Answer1.setVisible(false);
			Answer2.setVisible(false);
			Answer3.setVisible(false);
			if (!answers[3]) {
				BeingAtacked++;
				GameMaster("Shigure-chan" + "\n" + "attack you!!");
				health.getChildren().clear();
				CreateImage createImage = new CreateImage("/myapp/Game/player-health-" + (BeingAtacked) + ".png", 220, 50);
				health.getChildren().add(createImage.to_image());
				blw.setVisible(true);
				BLOWAUDIO.seek(BLOWAUDIO.getStartTime());
				BLOWAUDIO.play();
				PauseTransition pause = new PauseTransition(Duration.seconds(2));
				pause.setOnFinished(event -> {
					blw.setVisible(false);
					if (BeingAtacked == 3) {
						setMute();
						Lose();
					}
				});
				pause.play();
				Boss.getChildren().clear();
				CreateImage shigureChan = new CreateImage("/myapp/Game/SHINE.gif", 200, 200);
				Boss.getChildren().add(shigureChan.to_image());


			} else {
				AttackShigure();
				GameMaster("You attack" + "\n" + "Shigure-chan!!");
				ShigureHealth.getChildren().clear();
				AttackShigure++;
				CreateImage createImage = new CreateImage("/myapp/Game/shigure-health-" + (AttackShigure) + ".jpg", 170, 60);
				ShigureHealth.getChildren().add(createImage.to_image());
				PauseTransition pause = new PauseTransition(Duration.seconds(2.2));
				pause.setOnFinished(event -> {
					if (AttackShigure == 5) {
						setMute();
						Win();
					}
				});
				pause.play();
			}

		});


	}

	public void generateQuestion() {
		for (String question : questionsType) {
			WordMeaning wordMeaning = new WordMeaning(question);
			//System.out.println(wordMeaning.getAudio_link_answer());
			questions.add(wordMeaning);
		}
		NextWave();
	}

	private void AttackShigure() {
		sls.setVisible(true);
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), event -> {
			if (slashTime == 20) {
				sls.setVisible(false);
				slashTime = 0;
				timeline.stop();
			}
			slashTime++;
			CreateImage slash = new CreateImage("/myapp/Game/Slash" + slashTime % 6 + ".png", 140, 140);
			sls.setImage(slash.to_image().getImage());
			sls.setFitHeight(140);
			sls.setFitWidth(140);

		}));
		SLASHAUDIO.seek(SLASHAUDIO.getStartTime());
		SLASHAUDIO.setVolume(0.25);
		SLASHAUDIO.play();
		timeline.playFromStart();
	}

	private void GameMaster(String st) {
		CHOOSEAUDIO.seek(CHOOSEAUDIO.getStartTime());
		CHOOSEAUDIO.play();
		GMString = "";
		GMString += String.valueOf(st.charAt(0));
		GM.setText("");
		GM.setVisible(true);
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.06), event -> {
			if (Stringindex == st.length() + 13) {
				Stringindex = 0;
				GM.setVisible(false);
				Next.setVisible(true);
				timeline.stop();
			}
			if (Stringindex < st.length() && Stringindex >= 1) {
				GMString += String.valueOf(st.charAt(Stringindex));
				GM.setText(GMString);
			}
			Stringindex++;
		}));
		GMAUDIO.seek(GMAUDIO.getStartTime());
		GMAUDIO.play();
		timeline.playFromStart();


	}

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			stage.setOnHiding(e -> {
				setMute();
			});
		});
		URL picurl = getClass().getResource("/myapp/Game/Shigure-chan_tutorial.png");
		Image image = new Image(picurl.toString());
		TutorialImages.add(image);
		TutorialPicturePlayer = new PicturePlayer(TutorialImages);
		TutorialPicturePlayer.setMainStage(Main.mainController.getMainStage());
		TutorialPicturePlayer.createStage();

		THEME.setVolume(0.7);
		THEME.seek(THEME.getStartTime());
		THEME.play();
		THEME.setOnEndOfMedia(() -> {
			THEME.seek(THEME.getStartTime());
			THEME.play();
		});
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{close, refresh, tutorial},
				new String[]{"Close", "Restart", "how to play"},
				new String[]{"", "", ""}
		);
		scaleTransition.applyScaleTransition();
		CreateImage hp = new CreateImage("/myapp/Game/player-health.png", 220, 50);
		health.getChildren().add(hp.to_image());
		CreateImage shigure = new CreateImage("/myapp/Game/shigure1.gif", 200, 200);
		Boss.getChildren().add(shigure.to_image());
		CreateImage qizgif = new CreateImage("/myapp/Game/Questiongif.gif", 270, 250);
		QuestionGif.getChildren().add(qizgif.to_image());
		CreateImage click1 = new CreateImage("/myapp/Game/clickme.gif", 50, 50);
		CreateImage click2 = new CreateImage("/myapp/Game/clickme2.gif", 50, 50);
		CreateImage click3 = new CreateImage("/myapp/Game/clickme3.gif", 50, 50);
		CreateImage click4 = new CreateImage("/myapp/Game/clickme4.gif", 50, 50);
		CreateImage next = new CreateImage("/myapp/Game/next.png", 65, 80);
		Next.setStyle("-fx-background-color: transparent");
		CreateImage speaker = new CreateImage("/myapp/Game/Speaker.jpg", 45, 50);
		CreateImage shigureHP = new CreateImage("/myapp/Game/shigure-health-0.jpg", 170, 60);
		CreateImage blow = new CreateImage("/myapp/Game/Blow.gif", 210, 210);
		blw.setImage(blow.to_image().getImage());
		SELECTIONAUDIO.setVolume(0.5);
		blw.setFitWidth(210);
		blw.setFitHeight(210);
		blw.setTranslateY(-100);
		blw.setTranslateX(50);
		blw.setVisible(false);
		BLOW.getChildren().add(blw);
		sls.setTranslateX(12);
		sls.setTranslateY(45);
		SlashAttack.getChildren().add(sls);
		ShigureHealth.getChildren().add(shigureHP.to_image());
		Clickme1.getChildren().add(click1.to_image());
		Clickme2.getChildren().add(click2.to_image());
		Clickme3.getChildren().add(click3.to_image());
		Clickme4.getChildren().add(click4.to_image());
		Speaker.getChildren().add(speaker.to_image());
		Next.getChildren().add(next.to_image());
		Question.setFont(pixelify);
		GM.setFont(pixelify);
		Answer1.setFont(pixelify);
		Answer2.setFont(pixelify);
		Answer3.setFont(pixelify);
		Answer4.setFont(pixelify);
		MarkAnswer1.setFont(pixelify);
		MarkAnswer2.setFont(pixelify);
		MarkAnswer3.setFont(pixelify);
		MarkAnswer4.setFont(pixelify);
		nextText.setFont(pixelify);
		Name.setFont(pixelify);
		YourAtk.setFont(pixelify);
		speakerText.setFont(pixelify);
		YourHP.setFont(pixelify);
		WaveCount.setFont(pixelify);
		Wave.setFont(pixelify);
		ShigureAtkPoint.setFont(pixelify);

		Answer1.setOnMouseEntered(e -> {
			SELECTIONAUDIO.stop();
			SELECTIONAUDIO.seek(SELECTIONAUDIO.getStartTime());
			SELECTIONAUDIO.play();
			MarkAnswer1.setVisible(true);
			Clickme1.setVisible(false);
			Answer1.setFill(Color.CRIMSON);
		});
		Answer1.setOnMouseExited(e -> {
			MarkAnswer1.setVisible(false);
			Clickme1.setVisible(true);
			Answer1.setFill(Color.BLACK);
		});
		Answer2.setOnMouseEntered(e -> {
			SELECTIONAUDIO.stop();
			SELECTIONAUDIO.seek(SELECTIONAUDIO.getStartTime());
			SELECTIONAUDIO.play();
			MarkAnswer2.setVisible(true);
			Clickme2.setVisible(false);
			Answer2.setFill(Color.CRIMSON);
		});
		Answer2.setOnMouseExited(e -> {
			MarkAnswer2.setVisible(false);
			Clickme2.setVisible(true);
			Answer2.setFill(Color.BLACK);
		});
		Answer3.setOnMouseEntered(e -> {
			SELECTIONAUDIO.stop();
			SELECTIONAUDIO.seek(SELECTIONAUDIO.getStartTime());
			SELECTIONAUDIO.play();
			MarkAnswer3.setVisible(true);
			Clickme3.setVisible(false);
			Answer3.setFill(Color.CRIMSON);
		});
		Answer3.setOnMouseExited(e -> {
			MarkAnswer3.setVisible(false);
			Clickme3.setVisible(true);
			Answer3.setFill(Color.BLACK);
		});
		Answer4.setOnMouseEntered(e -> {
			SELECTIONAUDIO.stop();
			SELECTIONAUDIO.seek(SELECTIONAUDIO.getStartTime());
			SELECTIONAUDIO.play();
			MarkAnswer4.setVisible(true);
			Clickme4.setVisible(false);
			Answer4.setFill(Color.CRIMSON);
		});
		Answer4.setOnMouseExited(e -> {
			MarkAnswer4.setVisible(false);
			Clickme4.setVisible(true);
			Answer4.setFill(Color.BLACK);
		});

		scaleTransition = new ScaleTransitionForPane(
				new Pane[]{Next, Speaker},
				new String[]{"Next wave", "PLay audio"},

				new String[]{"", "", ""}
		);
		scaleTransition.applyScaleTransition();
		Next.setOnMouseClicked(e -> {
			NextWave();
		});

	}

	private void Lose() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/myapp/WinShigure.fxml"));
		try {
			Parent layout = loader.load();
			Scene scene = new Scene(layout, 360, 500);
			WinShigure winShigure = loader.getController();
			winShigure.isWinning = false;
			winShigure.stage = stage;
			winShigure.pixelify = pixelify;
			stage.setScene(scene);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void Win() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/myapp/WinShigure.fxml"));
		try {
			Parent layout = loader.load();
			Scene scene = new Scene(layout, 360, 500);
			WinShigure winShigure = loader.getController();
			winShigure.isWinning = true;
			winShigure.stage = stage;
			winShigure.pixelify = pixelify;
			stage.setScene(scene);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@FXML
	private void tutorialClicked() {
		TutorialPicturePlayer.BlurOwnedStages(Main.mainController.getOwnedStages());
		TutorialPicturePlayer.BlurMainStage();
		TutorialPicturePlayer.showStage();
	}
}
