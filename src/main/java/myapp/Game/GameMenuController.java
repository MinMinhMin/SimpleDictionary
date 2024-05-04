package myapp.Game;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.Game.CrossBoard.CrossBoardGameController;
import myapp.Game.WordMeaning.WordMeaningController;
import myapp.Main;
import myapp.MainController;
import myapp.SuggestionBox.ContextMenuController;

import java.io.IOException;


public class GameMenuController {
	public static Stage GameBoxStage;

	public ContextMenu loadGameMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(GameMenuController.class.getResource("/myapp/GameMenu.fxml"));
			ContextMenu gameMenu = loader.load();
			GameMenuController controller = loader.getController();
			gameMenu.setUserData(controller);
			return gameMenu;
		} catch (IOException e) {
			e.printStackTrace();
			return new ContextMenu();
		}
	}

	public void handleCrossBoard() throws IOException {
		System.out.println("Game1");
		showCrossBoardGameBox();

	}

	public void handleWordMeaning() throws IOException {
		System.out.println("Game2");
		showWordmeaningBox();

	}

	//Game 1
	public void showCrossBoardGameBox() throws IOException {
		showGameBox("CrossBoard");

	}
	//Game 2
	public void showWordmeaningBox() throws IOException {
        showGameBox("Shigure-chan");
	}

	private void adjustGameBoxPosition() {
		if (GameBoxStage != null && Main.mainController.getMainStage() != null) {
			double offsetX = -380;
			double offsetY = 227;
			GameBoxStage.setX(Main.mainController.getMainStage().getX() + Main.mainController.getMainStage().getWidth() + offsetX);
			GameBoxStage.setY(Main.mainController.getMainStage().getY() + offsetY);
		}
	}
	private void showGameBox(String gamemode) throws IOException {
		String FXMLString = "/myapp/CrossBoardGameBox.fxml";
		if(gamemode == "Shigure-chan"){FXMLString = "/myapp/WordmeaningGameBox.fxml";}
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLString));
		Parent layout = loader.load();
		if (GameBoxStage != null) {
			GameBoxStage.close();
		}
		GameBoxStage = new Stage();
		GameBoxStage.initOwner(Main.mainController.getMainStage());
		Main.mainController.addOwnedStage(GameBoxStage);
		GameBoxStage.initModality(Modality.NONE);
		ProgressBar progressBar = new ProgressBar();
		progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
		progressBar.getStyleClass().add("progress-bar");
		Text loadingText = new Text("Loading.....");
		loadingText.setStyle("-fx-font-weight:900");
		StackPane loadingPane = new StackPane();
		loadingPane.getChildren().add(progressBar);
		loadingPane.getChildren().add(loadingText);
		loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 5;-fx-background-radius: 30; -fx-border-radius:  20");
		Scene loadingscene = new Scene(loadingPane, 360, 500);
		loadingscene.setFill(Color.TRANSPARENT);
		GameBoxStage.setResizable(false);
		GameBoxStage.initStyle(StageStyle.TRANSPARENT);
		GameBoxStage.setScene(loadingscene);
		Main.mainController.getMainStage().xProperty().addListener((observable, oV, nV) -> {
			adjustGameBoxPosition();
		});

		Main.mainController.getMainStage().yProperty().addListener((observable, oV, nV) -> {
			adjustGameBoxPosition();
		});

		adjustGameBoxPosition();
		Button mouse = Main.mainController.mouse;
		mouse.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		GameBoxStage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				mouse.getStyleClass().remove("mouse-left-click");
				mouse.getStyleClass().add("mouse");
			}
			if (event.getButton() == MouseButton.SECONDARY) {
				mouse.getStyleClass().remove("mouse-right-click");
				mouse.getStyleClass().add("mouse");
			}

		});
		GameBoxStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				mouse.getStyleClass().remove("mouse");
				mouse.getStyleClass().add("mouse-left-click");
			}
			if (event.getButton() == MouseButton.SECONDARY) {
				mouse.getStyleClass().remove("mouse");
				mouse.getStyleClass().add("mouse-right-click");
			}

		});

		GameBoxStage.show();
		Task<Scene> rederTask = new Task<>() {
			@Override
			protected Scene call() throws Exception {
				if(gamemode == "Shigure-chan"){
					WordMeaningController wordMeaningController = loader.getController();
					wordMeaningController.setStage(GameBoxStage);
					wordMeaningController.generateQuestion();
				} else if (gamemode == "CrossBoard") {
					CrossBoardGameController crossBoardGameController = loader.getController();
					crossBoardGameController.setStage(GameBoxStage);
				}
				Thread.sleep(400);//waiting for little bit
				Scene scene;
				FadeTransition ft = new FadeTransition(Duration.millis(1100), layout);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				scene = new Scene(layout, 360, 500);
				scene.setFill(Color.TRANSPARENT);
				return scene;
			}
		};
		rederTask.setOnSucceeded(event -> GameBoxStage.setScene(rederTask.getValue()));
		new Thread(rederTask).start();


	}

}
