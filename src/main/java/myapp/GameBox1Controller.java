package myapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameBox1Controller implements Initializable {
	private Stage stage;
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@FXML
	public void close() {
		stage.close();
	}
	@FXML
	private GridPane wordSearchGridPane;

	private List<String> wordsToFind;

	private Button[][] buttons;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		buttons = new Button[10][8];

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 8; col++) {
				Button button = new Button();
				button.setMinSize(40, 40);
				button.setFont(Font.font(12));
				wordSearchGridPane.add(button, col, row);
				buttons[row][col] = button;

			}
		}


		populateWords();
	}

	private void populateWords() {




	}
}
