package myapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;

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

	private Set<String> wordsToFind;

    public void setWordsToFind(Set<String> wordsToFind) {
        this.wordsToFind = wordsToFind;
    }

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

        CrossBoard crossBoard=new CrossBoard();
        Set<String>answerString=new LinkedHashSet<>();
        answerString.add("hello");
        answerString.add("beautiful");
        answerString.add("bye");
        answerString.add("gadget");

        setWordsToFind(answerString);

        for (String answer:answerString){
            crossBoard.addWord(answer);
        }
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col].setText(String.valueOf(crossBoard.board[row][col]));
            }

	}

}
}
