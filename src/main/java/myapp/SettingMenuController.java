package myapp;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import myapp.SuggestionBox.CreateSuggestionItem;
import myapp.Translate.TranslateBoxController;

import java.io.IOException;

public class SettingMenuController {
	@FXML
	private Button reloadInternet;
	@FXML
	private ToggleButton ChangeTranslateButton;
	@FXML
	private ContextMenu settingMenu;

	public void initialize() {
		for (MenuItem item : settingMenu.getItems()) {
			if (item instanceof CustomMenuItem) {
				((CustomMenuItem) item).setHideOnClick(false);
			}
		}
		RotateTransition rotateTransition = new RotateTransition();
		rotateTransition.setDuration(Duration.seconds(1));

		rotateTransition.setNode(reloadInternet);

		rotateTransition.setByAngle(-360);

		rotateTransition.setAxis(Rotate.Z_AXIS);
		reloadInternet.setOnAction(event -> {
			Main.mainController.Internetcheck();
             rotateTransition.play();
		});
		ChangeTranslateButton.getStylesheets().add(SettingMenuController.class.getResource("Styling.css").toExternalForm());
		String previouslanguage,language,removeStyle,addStyle;
		previouslanguage = TranslateBoxController.TranslateString;
		if(previouslanguage == "vi"){
			ChangeTranslateButton.getStyleClass().add("english");
			language ="en";
			removeStyle = "english";
			addStyle = "vietnam";
		}else{
			ChangeTranslateButton.getStyleClass().add("vietnam");
			language ="vi";
			removeStyle = "vietnam";
			addStyle = "english";
		}

		MainController.applyScaleTransition(reloadInternet);
		MainController.applyScaleTransitionForToggleButton(ChangeTranslateButton);
		ChangeTranslateButton.setOnAction(event -> {
			if(ChangeTranslateButton.isSelected()){
				TranslateBoxController.TranslateString = language;
				ChangeTranslateButton.getStyleClass().remove(removeStyle);
				ChangeTranslateButton.getStyleClass().add(addStyle);
			} else{
				TranslateBoxController.TranslateString = previouslanguage ;
				ChangeTranslateButton.getStyleClass().remove(addStyle);
				ChangeTranslateButton.getStyleClass().add(removeStyle);
			}
		});
	}

	public static ContextMenu loadSettingMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(SettingMenuController.class.getResource("SettingMenu.fxml"));
			ContextMenu settingMenu = loader.load();
			SettingMenuController controller = loader.getController();
			settingMenu.setUserData(controller);
			return settingMenu;
		} catch (IOException e) {
			e.printStackTrace();
			return new ContextMenu();
		}
	}
}
