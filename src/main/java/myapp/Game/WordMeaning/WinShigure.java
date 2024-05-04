package myapp.Game.WordMeaning;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myapp.Game.GameMenuController;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.io.IOException;

public class WinShigure {
	final public MediaPlayer Die = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/youdie.mp3").toString()));
	final public MediaPlayer UiBeam = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/UIBeam.mp3").toString()));
	final public MediaPlayer WinTheme = new MediaPlayer(new Media(getClass().getResource("/myapp/Game/Shigure9mm.mp3").toString()));
	public Font pixelify;
	public Stage stage;
	public boolean isWinning = true;
	@FXML
	private Text You, YouWin;
	@FXML
	private Button close, refresh;
	@FXML
	private Pane Uibeam, Youdie, MainPane, Pepe, Meme;

	@FXML
	private void close() {
		GameMenuController.GameBoxStage.close();
	}

	@FXML
	private void refreshStage() throws IOException {
		GameMenuController.GameBoxStage.close();
		GameMenuController gameMenuController = new GameMenuController();
		gameMenuController.showWordmeaningBox();
	}

	private void setMute() {
		WinTheme.stop();
		Die.stop();
		UiBeam.stop();
	}

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			ScaleTransition scaleTransition = new ScaleTransitionForButton(
					new Button[]{close, refresh},
					new String[]{"Close", "Restart"},
					new String[]{"", ""}
			);
			scaleTransition.applyScaleTransition();
			stage.setOnHiding(e -> {
				setMute();
			});
			if (!isWinning) {
				You.setVisible(true);
				MainPane.setStyle("-fx-border-width: 5; -fx-border-color: rgb(255,255,255); -fx-background-radius: 30;-fx-border-radius:  20;-fx-background-color: rgb(0,0,0)");

				close.setStyle("-fx-background-color: white; -fx-border-color: white");
				refresh.setStyle("-fx-background-color: white; -fx-border-color: white");
				CreateImage uibeam = new CreateImage("/myapp/Game/UIBEAM.gif", 300, 300);
				Uibeam.getChildren().add(uibeam.to_image());
				CreateImage youdie = new CreateImage("/myapp/Game/youdie.gif", 300, 300);
				Youdie.getChildren().add(youdie.to_image());
				Die.seek(Die.getStartTime());
				Die.play();
				UiBeam.setOnEndOfMedia(() -> {
					UiBeam.seek(UiBeam.getStartTime());
					UiBeam.play();
				});
				UiBeam.seek(UiBeam.getStartTime());
				UiBeam.setVolume(0.4);
				UiBeam.play();

				You.setFont(pixelify);
				You.setStyle("-fx-font-size: 24");

			} else {
				YouWin.setVisible(true);
				WinTheme.seek(WinTheme.getStartTime());
				WinTheme.play();
				CreateImage PEPE = new CreateImage("/myapp/Game/pepe.gif", 350, 350);
				ImageView imageView = PEPE.to_image();
				Pepe.getChildren().add(imageView);
				CreateImage MEME = new CreateImage("/myapp/Game/MEME.jpg", 250, 250);
				Meme.getChildren().add(MEME.to_image());
				YouWin.setFont(pixelify);
				YouWin.setStyle("-fx-font-size: 20");

			}


		});


	}
}
