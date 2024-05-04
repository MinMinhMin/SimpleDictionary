package myapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import javafx.fxml.Initializable;
public class PicturePlayer  {
	protected List<Image> images;
	private Stage picturePlayerStage;
	@FXML
	private VBox PictureBox;
	@FXML
	private Button next,previous;


	@FXML
	public void initialize(){

	}
	public PicturePlayer(List<Image> images)  {
		picturePlayerStage = new Stage();
		this.images = images;

	}
	public void showStage() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PicturePlayer.fxml"));
		Parent layout = loader.load();
		Scene scene = new Scene(layout,500,500);
		ImageView image = new ImageView(images.get(0));
		PictureBox.getChildren().add(image);
		picturePlayerStage.setScene(scene);
		picturePlayerStage.show();
	}


}
