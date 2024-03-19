package myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;

public class MainController {
    @FXML
    public TextField searchBar;
    @FXML
    private VBox suggestionBox;
    private AutoComplete.Trie trie;

    @FXML
    private void initialize() {
        trie = AutoComplete.buildTrieFromFile("data/List.txt");
        searchBar.textProperty().addListener((observable, oV, nV) -> {
            if (nV.isEmpty()) {
                suggestionBox.getChildren().clear();
            } else {
                SugesstionUpdate.sugesstionUpdate(nV,trie,suggestionBox,searchBar);
            }
        });
    }
    
    @FXML
    private void AddClicked(ActionEvent event) {
        TextInputDialog englishDialog = new TextInputDialog();
        englishDialog.setTitle("Add Vocabulary");
        englishDialog.setHeaderText(null);
        englishDialog.setContentText("Enter English word:");
        Optional<String> englishResult = englishDialog.showAndWait();
        englishResult.ifPresent(english -> {

            TextInputDialog meaningDialog = new TextInputDialog();
            meaningDialog.setTitle("Add Vocabulary");
            meaningDialog.setHeaderText(null);
            meaningDialog.setContentText("Enter meaning for " + english + ":");

            Optional<String> meaningResult = meaningDialog.showAndWait();
            meaningResult.ifPresent(meaning -> {
                String line = english + "\t" + meaning;
                FileUtil.AddtoFIle("data/List.txt", line);
                trie.insert(english, meaning);
                SugesstionUpdate.sugesstionUpdate(searchBar.getText(),trie,suggestionBox,searchBar);
            });
        });
    }

    @FXML
    private void DeleteClicked(ActionEvent event) {
        String word = searchBar.getText();
        System.out.println(word);
        if(word == ""){word = "b";}
        DeleteWord.deleteWord(word,trie);
        trie.remove(word);
        SugesstionUpdate.sugesstionUpdate(searchBar.getText(),trie,suggestionBox,searchBar);
    }



}