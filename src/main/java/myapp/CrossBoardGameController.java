package myapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CrossBoardGameController implements Initializable {
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
    @FXML
	private Text choosenWord;
	@FXML
	private Button refresh;
	private Set<String> wordsToFind;

    public void setWordsToFind(Set<String> wordsToFind) {
        this.wordsToFind = wordsToFind;
    }


	private ToggleButton[][] buttons;
	private List<ToggleButton> selectedButtons = new ArrayList<>();
	private int lastRow = -1, lastCol = -1;
	private boolean isRow = true;
	private StringBuilder selectedText = new StringBuilder();
	private Set<String> wordsSet = new HashSet<>(Arrays.asList("hello", "beautiful", "bye", "gadget"));

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		buttons = new ToggleButton[10][8];

		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 8; col++) {
				int finalRow = row;
				int finalCol = col;
				ToggleButton button = new ToggleButton();
				button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black");
				button.setOnAction(event -> {
					if (button.isSelected()) {
						if (selectedButtons.isEmpty()) {
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
						} else if (selectedButtons.size() == 1) {
							if ((finalRow == lastRow && Math.abs(finalCol - lastCol) == 1) ||
									(finalCol == lastCol && Math.abs(finalRow - lastRow) == 1)) {
								isRow = finalRow == lastRow;
								button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
								selectedButtons.add(button);
								selectedText.append(button.getText());
								lastRow = finalRow;
								lastCol = finalCol;
							} else {
								selectedButtons.get(0).setStyle("-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								selectedButtons.get(0).setSelected(false);
								selectedButtons.clear();
								selectedText.setLength(0);
								button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
								selectedButtons.add(button);
								selectedText.append(button.getText());
								lastRow = finalRow;
								lastCol = finalCol;
							}
						} else if ((isRow && finalRow == lastRow && Math.abs(finalCol - lastCol) == 1) ||
								(!isRow && finalCol == lastCol && Math.abs(finalRow - lastRow) == 1)) {
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
						} else {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								b.setSelected(false);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
							button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: red;");
							selectedButtons.add(button);
							selectedText.append(button.getText());
							lastRow = finalRow;
							lastCol = finalCol;
							if (selectedButtons.size() == 2) {
								isRow = GridPane.getRowIndex(selectedButtons.get(0)) == GridPane.getRowIndex(selectedButtons.get(1));
							}
						}
						if (wordsSet.contains(selectedText.toString())) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("");
								b.setSelected(false);
								wordSearchGridPane.getChildren().remove(b);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
						}
						choosenWord.setText(selectedText.toString());
						System.out.println("press: " + finalRow + ", " + finalCol + ", isRow: " + isRow + ", text: " + selectedText.toString());
					} else {
						button.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
						selectedButtons.remove(button);
						if (!selectedButtons.isEmpty()) {
							ToggleButton lastButton = selectedButtons.get(selectedButtons.size() - 1);
							lastRow = GridPane.getRowIndex(lastButton);
							lastCol = GridPane.getColumnIndex(lastButton);
							if (selectedButtons.size() >= 2) {
								isRow = GridPane.getRowIndex(selectedButtons.get(0)) == GridPane.getRowIndex(selectedButtons.get(1));
							}
							selectedText.setLength(0);
							for (ToggleButton b : selectedButtons) {
								selectedText.append(b.getText());
							}
						} else {
							lastRow = -1;
							lastCol = -1;
							selectedText.setLength(0);
						}
						if (!selectedButtons.isEmpty() &&
								((isRow && finalRow == lastRow) || (!isRow && finalCol == lastCol))) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("-fx-border-width: 2;-fx-font-weight: 900;-fx-font-size: 15;-fx-border-color: black;");
								b.setSelected(false);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
						}
						if (wordsSet.contains(selectedText.toString())) {
							for (ToggleButton b : selectedButtons) {
								b.setStyle("");
								b.setSelected(false);
								wordSearchGridPane.getChildren().remove(b);
							}
							selectedButtons.clear();
							selectedText.setLength(0);
						}

						choosenWord.setText(selectedText.toString());
						System.out.println("release: " + finalRow + ", " + finalCol + ", isRow: " + isRow + ", text: " + selectedText.toString());
					}
				});
				button.setMinSize(40, 40);
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
     @FXML
     private void refreshStage() throws IOException {
		stage.close();
		GameMenuController gameMenuController = new GameMenuController();
		gameMenuController.showCrossBoardGameBox();
	 }

}



