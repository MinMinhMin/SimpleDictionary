package myapp;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TranslateBoxController {


	public static Stage primaryStage;


	public static Stage TranslateStage;
	@FXML
	private TextArea TranslateBox;
	@FXML
	private TextArea InputBox;
	public void initialize() {
		InputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String translateString = GoogleApi.translate(newValue,"vi");
			TranslateBox.setText(translateString);
			if(TranslateBox.getText()!=null){
			TranslateBox.positionCaret(TranslateBox.getText().length());}
		});
	}

	public void showTranslateStage(Parent layout)  {

		TranslateStage = new Stage();
		TranslateStage.initOwner(primaryStage);
		TranslateStage.initModality(Modality.NONE);
		TranslateStage.setResizable(false);
		TranslateStage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(layout, 990, 130);

		TranslateStage.setScene(scene);
		primaryStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		primaryStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		adjustTranslateStagePosition();
        TranslateStage.show();

	}
	private void adjustTranslateStagePosition() {
		if (TranslateStage != null && primaryStage != null) {
			double offsetX = -1240;
			double offsetY = 22;
			TranslateStage.setX(primaryStage.getX() + primaryStage.getWidth() + offsetX);
			TranslateStage.setY(primaryStage.getY() + offsetY);
		}
	}


}
