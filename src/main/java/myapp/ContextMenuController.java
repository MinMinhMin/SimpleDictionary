package myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		System.out.print(option);
	}

	public void setSelectedWord(String word) {
		this.selectedWord = word;
	}

	public void showDetailBox(String word) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailBox.fxml"));

		Parent layout = loader.load();
		DetailBoxController detailBoxController = loader.getController();
		detailBoxController.setStage(DetailBoxStage);
		detailBoxController.setWord(selectedWord);
		DetailBoxStage.initModality(Modality.APPLICATION_MODAL);
		DetailBoxStage.setTitle("Word Detail");
		Scene scene = new Scene(layout, 385, 500);
		DetailBoxStage.setScene(scene);

		DetailBoxStage.setX(115);
		DetailBoxStage.setY(265);
		DetailBoxStage.setResizable(false);
		DetailBoxStage.initStyle(StageStyle.UNDECORATED);
		DetailBoxStage.showAndWait();
	}
}