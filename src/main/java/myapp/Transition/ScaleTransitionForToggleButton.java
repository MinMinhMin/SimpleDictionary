package myapp.Transition;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;

public class ScaleTransitionForToggleButton extends ScaleTransition {
	protected ToggleButton[] buttons;
	public ScaleTransitionForToggleButton(ToggleButton[] buttons){
		this.buttons = buttons;
	}

	@Override
	public void applyScaleTransition() {
		for (ToggleButton button:buttons){
			javafx.animation.ScaleTransition stGrow = new javafx.animation.ScaleTransition(Duration.millis(200), button);
			stGrow.setToX(0.9);
			stGrow.setToY(0.9);

			javafx.animation.ScaleTransition stShrink = new javafx.animation.ScaleTransition(Duration.millis(200), button);
			stShrink.setToX(1);
			stShrink.setToY(1);

			button.setOnMouseEntered(e -> {
				stGrow.playFromStart();
			});

			button.setOnMouseExited(e -> {
				stShrink.playFromStart();
			});
		}

	}
}
