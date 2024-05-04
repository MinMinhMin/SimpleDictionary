package myapp;


import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class LogController  {
	public Stage LogStage;
	public LogController(){
		this.LogStage = new Stage();
	}
	public Scene scene;
	public  void  showLogBox(double offsetX,double offsetY) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogBox.fxml"));
		Parent layout = loader.load();
		LogStage = new Stage();
		LogStage.initOwner(MainController.MainStage);
		LogStage.initModality(Modality.NONE);
		scene = new Scene(layout, 440, 555);
		scene.setFill(Color.TRANSPARENT);
		LogStage.setScene(scene);
		LogStage.setResizable(false);
		LogStage.initStyle(StageStyle.TRANSPARENT);
		MainController.MainStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustLogBoxPosition(offsetX,offsetY);
		});

		MainController.MainStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustLogBoxPosition(offsetX,offsetY);
		});

		adjustLogBoxPosition(offsetX,offsetY);
		LogStage.show();
	}
	private void adjustLogBoxPosition(double offsetX,double offsetY) {
		if (LogStage != null && MainController.MainStage != null) {
			LogStage.setX(MainController.MainStage.getX() + MainController.MainStage.getWidth() + offsetX);
			LogStage.setY(MainController.MainStage.getY() + offsetY);
		}
	}
	public void animateStage(String operate) {
		// Get the root node of the scene
		Region root = (Region) scene.getRoot();

		// Create a ScaleTransition that scales the root node vertically from 1 to 0
		ScaleTransition st = new ScaleTransition(Duration.millis(1000), root);
		if(operate == "CLOSE"){
			st.setFromY(1.0);
			st.setToY(0.0);
		}
		if(operate =="OPEN"){
			st.setFromY(0.0);
			st.setToY(1.0);
		}
		st.play();
	}

}
