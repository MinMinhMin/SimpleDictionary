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
	private Button refresh,close;
	private List<String> wordsToFind;
    private Map<String,String>hint=new HashMap<>();
	private Map<String,Text> textMap = new HashMap<>();
	@FXML
	private Text word1,word2,word3,word4,point,choosenWord,win;
    public void setWordsToFind() {
        this.wordsToFind = MainController.words.get_4_random_words();
        this.wordsSet=new HashSet<>(this.wordsToFind);
		int index = 1;
        for (String word:this.wordsToFind){


            this.hint.put(word,CrossBoard.getHint(word));
			if(index == 1){textMap.put(word,word1); word1.setText(this.hint.get(word));}
			if(index == 2){textMap.put(word,word2); word2.setText(this.hint.get(word));}
			if(index == 3){textMap.put(word,word3); word3.setText(this.hint.get(word));}
			if(index == 4){textMap.put(word,word4); word4.setText(this.hint.get(word));}

					index++;

        }
    }


	private ToggleButton[][] buttons;
	private List<ToggleButton> selectedButtons = new ArrayList<>();
	private int lastRow = -1, lastCol = -1;
	private boolean isRow = true;
	private StringBuilder selectedText = new StringBuilder();
	private Set<String> wordsSet = new HashSet<>();
	private int Point = 0;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		MainController.applyScaleTransition(close);
		MainController.applyScaleTransition(refresh);
        setWordsToFind();
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
							textMap.get(selectedText.toString()).setText("✔");
							Point+=100;
							point.setText(String.valueOf(Point));
							wordsSet.remove(selectedText);
							selectedButtons.clear();
							selectedText.setLength(0);
							if(Point == 400){win.setText("WIN");}
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
							textMap.get(selectedText.toString()).setText("✔");
							Point+=100;
							point.setText(String.valueOf(Point));
							selectedButtons.clear();
							selectedText.setLength(0);
							if(Point == 400){win.setText("WIN");}
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

        for (String answer:wordsToFind){
            crossBoard.addWord(answer);
            System.out.println(answer+" "+this.hint.get(answer));
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



