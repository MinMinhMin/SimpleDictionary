package myapp.Translate;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import myapp.MainController;

public class TranslateBoxController {
	public static String TranslateString ="vi";

	public static Stage TranslateStage;
	@FXML
	private TextArea TranslateBox;
	@FXML
	private TextArea InputBox;
	public void initialize() {
		InputBox.textProperty().addListener((observable, oldValue, newValue) -> {
			String translateString = GoogleApi.translate(newValue,TranslateString);
			TranslateBox.setText(translateString);
			if(TranslateBox.getText()!=null){
			TranslateBox.positionCaret(TranslateBox.getText().length());}
		});
	}

	public void showTranslateStage(Parent layout)  {

		TranslateStage = new Stage();
		TranslateStage.initOwner(MainController.MainStage);
		TranslateStage.initModality(Modality.NONE);
		TranslateStage.setResizable(false);
		TranslateStage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(layout, 990, 130);

		TranslateStage.setScene(scene);
		MainController.MainStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		MainController.MainStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		adjustTranslateStagePosition();
        TranslateStage.show();

	}
	private void adjustTranslateStagePosition() {
		if (TranslateStage != null && MainController.MainStage != null) {
			double offsetX = -1240;
			double offsetY = 54;
			TranslateStage.setX(MainController.MainStage.getX() + MainController.MainStage.getWidth() + offsetX);
			TranslateStage.setY(MainController.MainStage.getY() + offsetY);
		}
	}


}
