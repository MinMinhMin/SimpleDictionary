package myapp.Transition;

import javafx.scene.control.Button;
import javafx.util.Duration;
import myapp.Main;


public class ScaleTransitionForButton extends ScaleTransition{
	protected Button[] buttons;
	protected String[] LeftClicks,RightClicks;
	public ScaleTransitionForButton(Button[] buttons,String[] LeftClicks,String[] RightClicks){
		this.buttons = buttons;
		this.LeftClicks = LeftClicks;
		this.RightClicks = RightClicks;
	}


	@Override
	public void applyScaleTransition() {
		for (int index =0;index<buttons.length;index++){
			Button button = buttons[index];
			String leftClickText = LeftClicks[index];
			String rightClickText = RightClicks[index];
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
