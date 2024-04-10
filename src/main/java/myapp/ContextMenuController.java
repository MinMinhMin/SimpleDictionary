package myapp;


import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class ContextMenuController {

	private String selectedWord;


	public static Stage primaryStage;



	public void handleDetail() throws IOException {
		handleOption("Word Detail");
	}

	public void handleAddGame() throws IOException {
		handleOption("Add to game library");
	}

	private void handleOption(String option) throws IOException {
		if (option == "Word Detail") {
			showDetailBox(selectedWord);

		}

	}


	private  static Stage DetailBoxStage;

	public void setSelectedWord(String word) {
		this.selectedWord = word;
	}

	public void showDetailBox(String word) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailBox.fxml"));
		Parent layout = loader.load();
		DetailBoxController detailBoxController = loader.getController();
		if(Objects.equals(word, detailBoxController.getWord())) {
			return;
		}
		if (DetailBoxStage != null) {
			DetailBoxStage.close();
		}
		DetailBoxStage = new Stage();
		DetailBoxStage.initOwner(primaryStage);
		DetailBoxStage.setTitle("Word Detail");
		DetailBoxStage.initModality(Modality.NONE);
		ProgressBar progressBar = new ProgressBar();
		progressBar.getStylesheets().add(ContextMenuController.class.getResource("Styling.css").toExternalForm());
		progressBar.getStyleClass().add("progress-bar");
		Text loadingText = new Text("Loading.....");
		loadingText.setStyle("-fx-font-weight:900");
		StackPane loadingPane = new StackPane();
		loadingPane.getChildren().add(progressBar);
		loadingPane.getChildren().add(loadingText);
		loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 10;-fx-background-radius: 30; -fx-border-radius:  20");
		Scene loadingscene = new Scene(loadingPane,385,500);
		DetailBoxStage.setResizable(false);
		DetailBoxStage.initStyle(StageStyle.TRANSPARENT);
		DetailBoxStage.setScene(loadingscene);

		detailBoxController.setStage(DetailBoxStage);

		primaryStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustDetailBoxPosition();
		});

		primaryStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustDetailBoxPosition();
		});

		adjustDetailBoxPosition();

		DetailBoxStage.show();

		//Loading....
		Task<Scene> rederTask = new Task<>() {
			@Override
			protected Scene call() throws Exception {
				detailBoxController.setWord(selectedWord);
				Thread.sleep(1000);
				Scene scene;
				FadeTransition ft = new FadeTransition(Duration.millis(1300), layout);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				scene = new Scene(layout, 385, 500);
				return scene;
			}
		};
		rederTask.setOnSucceeded(event -> DetailBoxStage.setScene(rederTask.getValue()));
		new Thread(rederTask).start();
	}
	private void adjustDetailBoxPosition() {
		if (DetailBoxStage != null && primaryStage != null) {
			double offsetX = -1335;
			double offsetY = 165;
			DetailBoxStage.setX(primaryStage.getX() + primaryStage.getWidth() + offsetX);
			DetailBoxStage.setY(primaryStage.getY() + offsetY);
		}
	}

}