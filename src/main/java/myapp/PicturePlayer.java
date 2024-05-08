package myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PicturePlayer {
	protected Stage mainStage = new Stage();
	protected List<Stage> ownedStages = new ArrayList<>();
	protected List<Image> images;
	private final Stage picturePlayerStage;
	private int currentPictureIndex = 0;
	@FXML
	private VBox PictureBox;
	@FXML
	private Button next, previous, close;
	@FXML
	private Label Counter;
	@FXML
	private Circle circle1, circle2;
	private final List<ImageView> imageViews = new ArrayList<>();

	public PicturePlayer(List<Image> images) {
		picturePlayerStage = new Stage();
		this.images = images;

	}

	@FXML
	public void initialize() {
		Counter.setText("[" + (currentPictureIndex + 1) + "/" + images.size() + "]");
		next.setOnAction(event -> {
			Main.mainController.currentTutorialPage = currentPictureIndex;
			if (currentPictureIndex + 1 == images.size()) {
				return;
			}
			currentPictureIndex = (currentPictureIndex + 1) % images.size();
			PictureBox.getChildren().clear();
			PictureBox.getChildren().add(imageViews.get(currentPictureIndex));
			Counter.setText("[" + (currentPictureIndex + 1) + "/" + images.size() + "]");
		});
		previous.setOnAction(event -> {
			if (currentPictureIndex + 1 == 1) {
				return;
			}
			currentPictureIndex = (currentPictureIndex - 1 + images.size()) % images.size();
			PictureBox.getChildren().clear();
			PictureBox.getChildren().add(imageViews.get(currentPictureIndex));
			Counter.setText("[" + (currentPictureIndex + 1) + "/" + images.size() + "]");
		});
		close.setOnAction(event -> {
			for(Stage stage:ownedStages){
				Scene OScene = stage.getScene();
				Parent ORoot = OScene.getRoot();
				ORoot.setEffect(null);
			}
			Scene scene = mainStage.getScene();
			Parent root = scene.getRoot();
			picturePlayerStage.close();
			root.setEffect(null);


		});
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{next, previous, close},
				new String[]{"", "", ""},
				new String[]{"", "", ""}
		);
		scaleTransition.applyScaleTransition();


	}

	public void setMainStage(Stage MainStage) {
		this.mainStage = MainStage;
	}
	public void BlurMainStage(){
		Scene mainStageScene = mainStage.getScene();
		Parent mainStageSceneRoot = mainStageScene.getRoot();
		ColorAdjust colorAdjust = new ColorAdjust(0, -0.9, -0.5, 0);
		GaussianBlur gaussianBlur = new GaussianBlur(20);
		colorAdjust.setInput(gaussianBlur);
		mainStageSceneRoot.setEffect(colorAdjust);
	}
	public void BlurOwnedStages(List<Stage> OwnedStages){
		this.ownedStages = OwnedStages;
		for(Stage stage:ownedStages){
			Scene scene = stage.getScene();
			Parent root = scene.getRoot();
			ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
			GaussianBlur blur = new GaussianBlur(20);
			adj.setInput(blur);
			root.setEffect(adj);
		}
	}

	private void adjustpicturePlayerStagePosition() {
		if (picturePlayerStage != null && mainStage != null) {
			double offsetX = -1206;
			double offsetY = 130;
			picturePlayerStage.setX(mainStage.getX() + mainStage.getWidth() + offsetX);
			picturePlayerStage.setY(mainStage.getY() + offsetY);
		}
	}

	public void createStage() {
		picturePlayerStage.initModality(Modality.APPLICATION_MODAL);
		picturePlayerStage.setResizable(false);
		picturePlayerStage.initStyle(StageStyle.TRANSPARENT);
		picturePlayerStage.initOwner(mainStage);
		mainStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustpicturePlayerStagePosition();
		});
		picturePlayerStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustpicturePlayerStagePosition();
		});
		adjustpicturePlayerStagePosition();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PicturePlayer.fxml"));
			loader.setController(this);
			Parent layout = loader.load();
			Scene scene = new Scene(layout, 1040, 530);
			scene.setFill(Color.TRANSPARENT);
			for (Image image : images) {
				ImageView imageview = new ImageView(image);
				imageViews.add(imageview);
				imageview.setFitWidth(780);
				imageview.setFitHeight(540);
				imageview.setPreserveRatio(true);
			}
			PictureBox.getChildren().add(imageViews.get(0));
			picturePlayerStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showStage(){
		picturePlayerStage.show();
	}


}
