package myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Search App");
        primaryStage.setMinWidth(1360);
        primaryStage.setMinHeight(768);
        primaryStage.setMaxWidth(1360);
        primaryStage.setMaxHeight(768);
        primaryStage.centerOnScreen();
        Scene scene = new Scene(root, 1360, 768);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}