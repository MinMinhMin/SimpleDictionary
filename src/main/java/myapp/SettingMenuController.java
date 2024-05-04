package myapp;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;
import myapp.Transition.ScaleTransitionForToggleButton;
import myapp.Translate.TranslateBoxController;

import java.io.IOException;

public class SettingMenuController {
	@FXML
	private Button reloadInternet, ChangeModify;
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
			System.out.println("reload");
			Main.mainController.Internetcheck();
			rotateTransition.play();
		});
		ChangeTranslateButton.getStylesheets().add(SettingMenuController.class.getResource("Styling.css").toExternalForm());
		String previouslanguage, language, removeStyle, addStyle;
		previouslanguage = TranslateBoxController.TranslateString;
		if (previouslanguage == "vi") {
			ChangeTranslateButton.getStyleClass().add("english");
			language = "en";
			removeStyle = "english";
			addStyle = "vietnam";
		} else {
			ChangeTranslateButton.getStyleClass().add("vietnam");
			language = "vi";
			removeStyle = "vietnam";
			addStyle = "english";
		}
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{reloadInternet, ChangeModify},
				new String[]{"Reload Internet", "Change filter"},
				new String[]{"", ""}
		);
		scaleTransition.applyScaleTransition();
		scaleTransition = new ScaleTransitionForToggleButton(
				new ToggleButton[]{ChangeTranslateButton},
				new String[]{"Translate change"},
				new String[]{""}
		);
		scaleTransition.applyScaleTransition();
		ChangeTranslateButton.setOnAction(event -> {
			if (ChangeTranslateButton.isSelected()) {
				TranslateBoxController.TranslateString = language;
				ChangeTranslateButton.getStyleClass().remove(removeStyle);
				ChangeTranslateButton.getStyleClass().add(addStyle);
			} else {
				TranslateBoxController.TranslateString = previouslanguage;
				ChangeTranslateButton.getStyleClass().remove(addStyle);
				ChangeTranslateButton.getStyleClass().add(removeStyle);
			}
		});
		Platform.runLater(() -> {
			ChangeModify.setText(Main.mainController.getSuggestionModified());
		});
		ChangeModify.setOnAction(e -> {
			String currentModify = Main.mainController.getSuggestionModified();
			String nextModify;
			if (currentModify == "normal") {
				nextModify = "favorite";
			} else {
				nextModify = "normal";
			}
			ChangeModify.setText(nextModify);
			Main.mainController.setSuggestionModified(nextModify);
			Main.mainController.POPUP("Search modified to show " + nextModify + " words!", true);

		});

	}

	public ContextMenu loadSettingMenu() {
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
