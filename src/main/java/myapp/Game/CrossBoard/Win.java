package myapp.Game.CrossBoard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import myapp.Game.GameMenuController;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.io.IOException;
import java.net.URL;

public class Win {
	public static int Point;
	@FXML
	private Pane winBox;
	@FXML
	private Text point;
	@FXML
	private Button refresh, close;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			point.setText(String.valueOf(Point));
		});
		URL picurl = getClass().getResource("/myapp/Game/anime.jpg");
		Image image = new Image(picurl.toString());
		ImageView imageview = new ImageView(image);
		imageview.setFitWidth(400);
		imageview.setFitHeight(200);
		imageview.setPreserveRatio(true);
		winBox.getChildren().add(imageview);
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{close, refresh},
				new String[]{"Close", "Restart"},
				new String[]{"", ""}
		);
		scaleTransition.applyScaleTransition();

	}

	@FXML
	private void refreshStage() throws IOException {
		GameMenuController.GameBoxStage.close();
		GameMenuController gameMenuController = new GameMenuController();
		gameMenuController.showCrossBoardGameBox();
	}

	@FXML
	private void close() {
		GameMenuController.GameBoxStage.close();
	}

}
