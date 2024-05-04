package myapp.Transition;

import javafx.scene.control.ToggleButton;
import javafx.util.Duration;
import myapp.Main;

public class ScaleTransitionForToggleButton extends ScaleTransition {
	protected ToggleButton[] buttons;
	protected String[] LeftClicks, RightClicks;

	public ScaleTransitionForToggleButton(ToggleButton[] buttons, String[] LeftClicks, String[] RightClicks) {
		this.buttons = buttons;
		this.LeftClicks = LeftClicks;
		this.RightClicks = RightClicks;
	}

	@Override
	public void applyScaleTransition() {
		for (int index = 0; index < buttons.length; index++) {
			ToggleButton button = buttons[index];
			String leftClickText = this.LeftClicks[index];
			String rightClickText = this.RightClicks[index];
			javafx.animation.ScaleTransition stGrow = new javafx.animation.ScaleTransition(Duration.millis(200), button);
			stGrow.setToX(0.9);
			stGrow.setToY(0.9);

			javafx.animation.ScaleTransition stShrink = new javafx.animation.ScaleTransition(Duration.millis(200), button);
			stShrink.setToX(1);
			stShrink.setToY(1);

			button.setOnMouseEntered(e -> {
				Main.mainController.setLeftClick(leftClickText);
				Main.mainController.setRightClick(rightClickText);
				stGrow.playFromStart();
			});

			button.setOnMouseExited(e -> {
				Main.mainController.setLeftClick("");
				Main.mainController.setRightClick("");
				stShrink.playFromStart();
			});
		}

	}
}
