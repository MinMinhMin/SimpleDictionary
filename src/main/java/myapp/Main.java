package myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		URL url = getClass().getResource("dictionary.png");
		Image image = new Image(url.toString());
		primaryStage.getIcons().add(image);

		primaryStage.setTitle("Simple Dictionary");
		primaryStage.centerOnScreen();
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(root, 1360, 800);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		MainController mainController = loader.getController();
		Main.mainController = mainController;
		mainController.setMainStage(primaryStage);
		mainController.logShow();

		primaryStage.show();
	}

}
