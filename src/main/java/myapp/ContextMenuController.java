package myapp;

import java.io.InterruptedIOException;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class ContextMenuController {

	private String selectedWord;

	public void setStg(Stage newStage) {
		this.DetailBoxStage = newStage;
	}

	private Stage DetailBoxStage = new Stage();

	public void handleDetail() throws IOException {
		handleOption("Word Detail");
	}

	public void handleAddGame() throws IOException {
		handleOption("Add to game library");
	}

	private void handleOption(String option) throws IOException {
		Stage currentDetailStage = new Stage();
		if (option == "Word Detail") {
			setStg(currentDetailStage);
			System.out.println(selectedWord);
			showDetailBox(selectedWord);

		}

	}

	public void setSelectedWord(String word) {
		this.selectedWord = word;
	}

	public void showDetailBox(String word) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailBox.fxml"));

		Parent layout = loader.load();
		DetailBoxController detailBoxController = loader.getController();
		System.out.println(detailBoxController.getWord());
		detailBoxController.setStage(DetailBoxStage);
		DetailBoxStage.setTitle("Word Detail");
		//DetailBoxStage.initModality(Modality.APPLICATION_MODAL);
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
		DetailBoxStage.setX(115);
		DetailBoxStage.setY(265);
		DetailBoxStage.setResizable(false);
		DetailBoxStage.initStyle(StageStyle.TRANSPARENT);
		DetailBoxStage.setScene(loadingscene);
		DetailBoxStage.show();
		//Loading....
		Task<Scene> rederTask = new Task<>() {
			@Override
			protected Scene call() throws Exception {
				detailBoxController.setWord(selectedWord);
				Thread.sleep(1600);
				Scene scene;
				FadeTransition ft = new FadeTransition(Duration.millis(1300), layout);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				scene = new Scene(layout, 385, 500);
				return scene;
			}
		};
		rederTask.setOnSucceeded(event ->DetailBoxStage.setScene(rederTask.getValue()));
		new Thread(rederTask).start();




	}
}