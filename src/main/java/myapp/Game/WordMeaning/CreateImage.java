package myapp.Game.WordMeaning;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class CreateImage {
	protected String path;
	protected int width, height;

	public CreateImage(String path, int width, int height) {
		this.path = path;
		this.height = height;
		this.width = width;
	}

	public ImageView to_image() {
		URL picurl = getClass().getResource(path);
		Image image = new Image(picurl.toString());
		ImageView imageview = new ImageView(image);
		imageview.setFitWidth(width);
		imageview.setFitHeight(height);
		imageview.setPreserveRatio(true);
		return imageview;
	}
}
