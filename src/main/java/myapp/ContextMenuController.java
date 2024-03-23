package myapp;

import java.io.InterruptedIOException;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ContextMenuController {

	private String selectedWord;

	public void setStg() {
		this.DetailBoxStage = new Stage();
	}

	private Stage DetailBoxStage = new Stage();

	public void handleDetail() throws IOException {
		handleOption("Word Detail");
	}

	public void handleAddGame() throws IOException {
		handleOption("Add to game library");
	}

	private void handleOption(String option) throws IOException {
		if (option == "Word Detail") {
			setStg();
			showDetailBox(selectedWord);
		}
		System.out.println(option);
	}

	public void setSelectedWord(String word) {
		this.selectedWord = word;
	}

	public void showDetailBox(String word) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailBox.fxml"));

		Parent layout = loader.load();
		DetailBoxController detailBoxController = loader.getController();
		detailBoxController.setStage(DetailBoxStage);
		DetailBoxStage.setTitle("Word Detail");
		DetailBoxStage.initModality(Modality.APPLICATION_MODAL);
		ProgressBar progressBar = new ProgressBar();
		StackPane loadingPane = new StackPane();
		loadingPane.getChildren().add(progressBar);
		loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 10;");
		Scene loadingscene = new Scene(loadingPane,385,500);
		DetailBoxStage.setX(115);
		DetailBoxStage.setY(265);
		DetailBoxStage.setResizable(false);
		DetailBoxStage.initStyle(StageStyle.UNDECORATED);
		DetailBoxStage.setScene(loadingscene);
		DetailBoxStage.show();
		//Loading....
		Task<Scene> rederTask = new Task<Scene>() {
			@Override
			protected Scene call() throws Exception {
				detailBoxController.setWord(selectedWord);
				Scene scene;
				scene = new Scene(layout, 385, 500);
				return scene;
			}
		};
		rederTask.setOnSucceeded(event ->DetailBoxStage.setScene(rederTask.getValue()));
		new Thread(rederTask).start();



	}
}