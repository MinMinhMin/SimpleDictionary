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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

public class PicturePlayer  {
	protected Stage mainStage;
	protected List<Image> images;
	private Stage picturePlayerStage;
	private int currentPictureIndex =0;
	@FXML
	private VBox PictureBox;
	@FXML
	private Button next,previous,close;
	@FXML
	private Label Counter;
	@FXML
	private Circle circle1,circle2;
	@FXML
	public void initialize(){
		Counter.setText("["+(currentPictureIndex+1)+"/"+images.size()+"]");
		next.setOnAction(event -> {
			if(currentPictureIndex+1 ==images.size()){return;}
			currentPictureIndex = (currentPictureIndex + 1) % images.size();
			PictureBox.getChildren().clear();
			PictureBox.getChildren().add(imageViews.get(currentPictureIndex));
			Counter.setText("["+(currentPictureIndex+1)+"/"+images.size()+"]");
		});
		previous.setOnAction(event -> {
			if(currentPictureIndex+1 ==1){return;}
			currentPictureIndex = (currentPictureIndex -1 +images.size()) % images.size();
			PictureBox.getChildren().clear();
			PictureBox.getChildren().add(imageViews.get(currentPictureIndex));
			Counter.setText("["+(currentPictureIndex+1)+"/"+images.size()+"]");
		});
		close.setOnAction(event -> {
			Scene scene = mainStage.getScene();
			Parent root = (Parent) scene.getRoot();
			picturePlayerStage.close();
			root.setEffect(null);

		});
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{next,previous,close},
				new String[]{"","",""},
				new String[]{"","",""}
		);
		scaleTransition.applyScaleTransition();



	}
	public PicturePlayer(List<Image> images)  {
		picturePlayerStage = new Stage();
		this.images = images;

	}
	public void setMainStage(Stage MainStage){
		this.mainStage = MainStage;
		Scene scene = mainStage.getScene();
		Parent root = (Parent) scene.getRoot();
		ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
		GaussianBlur blur = new GaussianBlur(20);
		adj.setInput(blur);
		root.setEffect(adj);
		picturePlayerStage.initModality(Modality.APPLICATION_MODAL);
		picturePlayerStage.initOwner(MainStage);
		picturePlayerStage.setResizable(false);
		picturePlayerStage.initStyle(StageStyle.TRANSPARENT);
		MainStage.xProperty().addListener((observable, oldValue, newValue) -> {
			adjustpicturePlayerStagePosition();
		});
		picturePlayerStage.yProperty().addListener((observable, oldValue, newValue) -> {
			adjustpicturePlayerStagePosition();
		});
		adjustpicturePlayerStagePosition();
	}
	private void adjustpicturePlayerStagePosition(){
		if (picturePlayerStage != null && mainStage != null) {
			double offsetX = -1206;
			double offsetY = 130;
			picturePlayerStage.setX(mainStage.getX() + mainStage.getWidth() + offsetX);
			picturePlayerStage.setY(mainStage.getY() + offsetY);
		}
	}

	private List<ImageView> imageViews =new ArrayList<>();

	public void showStage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PicturePlayer.fxml"));
			loader.setController(this);
			Parent layout = loader.load();
			Scene scene = new Scene(layout, 1040, 530);
			scene.setFill(Color.TRANSPARENT);
			for(Image image:images){
				ImageView imageview = new ImageView(image);
				imageViews.add(imageview);
				imageview.setFitWidth(780);
				imageview.setFitHeight(540);
				imageview.setPreserveRatio(true);
			}
			PictureBox.getChildren().add(imageViews.get(0));
			picturePlayerStage.setScene(scene);

			picturePlayerStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
