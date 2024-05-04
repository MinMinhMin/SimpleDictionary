package myapp.Translate;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import myapp.Main;
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
		TranslateStage.initOwner(Main.mainController.getMainStage());
		TranslateStage.initModality(Modality.NONE);
		TranslateStage.setResizable(false);
		TranslateStage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(layout, 990, 130);

		TranslateStage.setScene(scene);
		Main.mainController.getMainStage().xProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		Main.mainController.getMainStage().yProperty().addListener((observable, oldValue, newValue) -> {
			adjustTranslateStagePosition();
		});

		adjustTranslateStagePosition();
        TranslateStage.show();

	}
	private void adjustTranslateStagePosition() {
		if (TranslateStage != null && Main.mainController.getMainStage() != null) {
			double offsetX = -1240;
			double offsetY = 54;
			TranslateStage.setX(Main.mainController.getMainStage().getX() + Main.mainController.getMainStage().getWidth() + offsetX);
			TranslateStage.setY(Main.mainController.getMainStage().getY() + offsetY);
		}
	}


}
