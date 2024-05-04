package myapp;


import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogController {
	public Stage LogStage;
	public Scene scene;
	@FXML
	private VBox LogVBox;

	@FXML
	private void initialize() {
		Text label = new Text("Log Tabel: ");
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 20");
		label.setWrappingWidth(420);

		LogVBox.getChildren().add(label);
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
		Text label = new Text(updateString);
		label.setStyle("-fx-font-weight: 900;-fx-font-size: 15");
		label.setWrappingWidth(420);

		LogVBox.getChildren().add(label);

	}

}
