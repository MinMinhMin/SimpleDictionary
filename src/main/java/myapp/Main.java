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

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileUtil.cache();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Search App");
        primaryStage.setMinWidth(1360);
        primaryStage.setMinHeight(768);
        primaryStage.setMaxWidth(1360);
        primaryStage.setMaxHeight(768);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        FadeTransition ft = new FadeTransition(Duration.millis(2600), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        Scene scene = new Scene(root, 1360, 768);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        MainController mainController = loader.getController();
        mainController.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}