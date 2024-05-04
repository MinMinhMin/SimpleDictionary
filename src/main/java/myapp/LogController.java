package myapp;


import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LogController {
	private final String LogTxt = "data/Log.txt";
	public Stage LogStage;
	public Scene scene;
	@FXML
	private VBox LogVBox;
	@FXML
	private void initialize() {
		List<String> logList = FileUtil.getlist("data/Log.txt");
		for(String logString:logList){
			Text label = new Text(logString);
			label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
			label.setWrappingWidth(420);
			LogVBox.getChildren().add(label);
		}


	}
	public void setLogStage(Stage logStage) {
		LogStage = logStage;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void animateStage(String operate) {
		Region root = (Region) scene.getRoot();

		ScaleTransition st = new ScaleTransition(Duration.millis(1000), root);
		if (operate == "CLOSE") {
			st.setFromY(1.0);
			st.setToY(0.0);
		}
		if (operate == "OPEN") {
			st.setFromY(0.0);
			st.setToY(1.0);
		}
		st.play();
	}

	public void Update(String updateString) {
		FileUtil.AddtoFIle(LogTxt,updateString);
		Text label = new Text(updateString);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		label.setWrappingWidth(420);
		LogVBox.getChildren().add(label);

	}

}
