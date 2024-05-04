package myapp;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

	public static MainController mainController;
	double xOffset, yOffset;

	public static void main(String[] args) {

		launch(args);
	}

	@Override

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		Parent root = loader.load();
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - xOffset);
			primaryStage.setY(event.getScreenY() - yOffset);
		});

		primaryStage.setTitle("Search App");
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		Scene scene = new Scene(root, 1360, 800);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		MainController mainController = loader.getController();
		Main.mainController = mainController;
		MainController.MainStage = primaryStage;
		mainController.logShow();

		primaryStage.show();
	}

}
