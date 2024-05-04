package myapp.SuggestionBox;


import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.LogController;
import myapp.Main;
import myapp.MainController;
import myapp.SuggestionBox.WordDetails.DetailBoxController;
import myapp.Transition.ScaleTransition;
import myapp.Transition.ScaleTransitionForButton;

import java.io.IOException;
import java.util.Objects;

public class ContextMenuController {

	private static Stage DetailBoxStage;
	private LogController log;
	private String selectedWord;
	private String mean;
	@FXML
	private MenuItem modify, update, wordDetail;
	private Stage UpdateStage;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			if (Main.mainController.getSuggestionModified() == "normal") {
				modify.setText("     Add to favorite");
			} else {
				modify.setText("  Remove from favorite");
			}
		});
	}

	public void handleDetail() throws IOException {
		handleOption("Word Detail");
	}

	public void handleUpdate() throws IOException {
		handleOption("Update");
	}

	public void handleFavoriteWord() throws IOException {
		handleOption("FavoriteWord");
	}

	private void handleOption(String option) throws IOException {
		log = Main.mainController.getLog();
		if (option == "Word Detail") {
			showDetailBox(selectedWord);

		}
		if (option == "Update") {
			showUpdateBox(selectedWord, mean);
		}
		if (option == "FavoriteWord") {
			if (Main.mainController.getSuggestionModified() == "normal") {
				Words.add_to_favourite(selectedWord, mean);
				Main.mainController.POPUP("(" + selectedWord + ": " + mean + ") is added to favorite words", true);
				log.Update(Main.mainController.getDate().getText() + " " + Main.mainController.getTime().getText() + " ( " + "Favorite added: (" + selectedWord + "," + mean + "))");
			} else {
				Words.add_to_unfavourite(selectedWord, mean);
				Main.mainController.POPUP("(" + selectedWord + ": " + mean + ") is removed from favorite words", true);
				log.Update(Main.mainController.getDate().getText() + " " + Main.mainController.getTime().getText() + " ( " + "Favorite removed: (" + selectedWord + "," + mean + "))");
			}

		}

	}

	//Khởi động Stage của nút update từ khi ấn
	public void showUpdateBox(String selectedWord, String CurrentMeaning) {
		UpdateStage = new Stage();
		UpdateStage.initOwner(Main.mainController.getMainStage());
		Label label = new Label("Update meaning of " + '"' + selectedWord + '"' + ':');
		label.setStyle("-fx-font-weight: 900;");
		label.setMaxWidth(Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		TextField meaning = new TextField();
		meaning.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black");
		Button setButton = new Button("Set");
		setButton.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		setButton.getStyleClass().add("normalButton");
		setButton.setMaxWidth(Double.MAX_VALUE);
		setButton.setAlignment(Pos.CENTER);
		setButton.setStyle("-fx-font-weight: 900;-fx-background-color: white;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black");
		Button closeButton = new Button("Close");
		ScaleTransition scaleTransition = new ScaleTransitionForButton(
				new Button[]{setButton, closeButton},
				new String[]{"Update word", "Close"},
				new String[]{"", ""}
		);
		scaleTransition.applyScaleTransition();
		setButton.setOnAction(event -> {
			String NewMeaning = meaning.getText();
			log.Update(Main.mainController.getDate().getText() + " " + Main.mainController.getTime().getText() + " ( " + "Change " + selectedWord + ": " + CurrentMeaning + " to " + selectedWord + ": " + NewMeaning + ")");
			ProgressBar progressBar = new ProgressBar();
			progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
			progressBar.getStyleClass().add("progress-bar");
			Text loadingText = new Text("Loading.....");
			loadingText.setStyle("-fx-font-weight:900");
			StackPane loadingPane = new StackPane();
			loadingPane.getChildren().add(progressBar);
			loadingPane.getChildren().add(loadingText);
			loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 4;-fx-background-radius: 30; -fx-border-radius:  20");
			Scene loadingscene = new Scene(loadingPane, 200, 180);
			UpdateStage.setScene(loadingscene);
			Task<Scene> rederTask = new Task<>() {
				@Override
				protected Scene call() throws Exception {

					Words.update_word(selectedWord, CurrentMeaning, NewMeaning);
					return null;
				}
			};
			rederTask.setOnSucceeded(event2 -> {
				UpdateStage.close();
				Main.mainController.POPUP("Change word success!", true);
			});
			new Thread(rederTask).start();

		});
		closeButton.setStyle("-fx-font-weight: 900;-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 3;-fx-border-color: black;-fx-background-color: white");
		closeButton.setOnAction(event -> {
			UpdateStage.close();
		});
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(closeButton, label, meaning, setButton);
		vbox.setSpacing(10);
		vbox.setStyle("-fx-background-radius: 40;-fx-border-radius: 20;-fx-border-width: 4;-fx-border-color: black");
		Scene scene = new Scene(vbox, 200, 180);
		scene.setFill(Color.TRANSPARENT);
		UpdateStage.initStyle(StageStyle.TRANSPARENT);
		UpdateStage.setX(1080);
		UpdateStage.setY(280);
		UpdateStage.setResizable(false);
		UpdateStage.setScene(scene);
		UpdateStage.initModality(Modality.APPLICATION_MODAL);
		UpdateStage.show();
	}

	public void setSelectedWord(String word, String meaning) {
		this.selectedWord = word;
		this.mean = meaning;
	}

	public void showDetailBox(String word) throws IOException {


		FXMLLoader loader = new FXMLLoader(getClass().getResource("/myapp/DetailBox.fxml"));
		Parent layout = loader.load();
		DetailBoxController detailBoxController = loader.getController();
		if (Objects.equals(word, detailBoxController.getWord())) {

			return;
		}
		if (DetailBoxStage != null) {
			DetailBoxStage.close();
		}
		DetailBoxStage = new Stage();
		DetailBoxStage.initOwner(Main.mainController.getMainStage());
		Main.mainController.addOwnedStage(DetailBoxStage);
		DetailBoxStage.setTitle("Word Detail");
		DetailBoxStage.initModality(Modality.NONE);
		ProgressBar progressBar = new ProgressBar();
		progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
		progressBar.getStyleClass().add("progress-bar");
		Text loadingText = new Text("Loading.....");
		loadingText.setStyle("-fx-font-weight:900");
		StackPane loadingPane = new StackPane();
		loadingPane.getChildren().add(progressBar);
		loadingPane.getChildren().add(loadingText);
		loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 5;-fx-background-radius: 30; -fx-border-radius:  20");
		Scene loadingscene = new Scene(loadingPane, 385, 500);
		loadingscene.setFill(Color.TRANSPARENT);
		DetailBoxStage.setResizable(false);
		DetailBoxStage.initStyle(StageStyle.TRANSPARENT);
		DetailBoxStage.setScene(loadingscene);

		detailBoxController.setStage(DetailBoxStage);

		Main.mainController.getMainStage().xProperty().addListener((observable, oV, nV) -> {
			adjustDetailBoxPosition();
		});

		Main.mainController.getMainStage().yProperty().addListener((observable, oV, nV) -> {
			adjustDetailBoxPosition();
		});
		Button mouse = Main.mainController.mouse;
		mouse.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		DetailBoxStage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				mouse.getStyleClass().remove("mouse-left-click");
				mouse.getStyleClass().add("mouse");
			}
			if (event.getButton() == MouseButton.SECONDARY) {
				mouse.getStyleClass().remove("mouse-right-click");
				mouse.getStyleClass().add("mouse");
			}

		});
		DetailBoxStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				mouse.getStyleClass().remove("mouse");
				mouse.getStyleClass().add("mouse-left-click");
			}
			if (event.getButton() == MouseButton.SECONDARY) {
				mouse.getStyleClass().remove("mouse");
				mouse.getStyleClass().add("mouse-right-click");
			}

		});

		adjustDetailBoxPosition();

		DetailBoxStage.show();

		//Loading....
		Task<Scene> rederTask = new Task<>() {
			@Override
			protected Scene call() throws Exception {
				detailBoxController.setWord(selectedWord);
				Thread.sleep(500);
				Scene scene;
				FadeTransition ft = new FadeTransition(Duration.millis(1300), layout);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
				scene = new Scene(layout, 385, 500);
				scene.setFill(Color.TRANSPARENT);
				return scene;
			}
		};
		rederTask.setOnSucceeded(event -> DetailBoxStage.setScene(rederTask.getValue()));
		new Thread(rederTask).start();
		log.Update(Main.mainController.getDate().getText() + " " + Main.mainController.getTime().getText() + " ( " + "Word Detail: " + selectedWord + ")");

	}

	private void adjustDetailBoxPosition() {
		if (DetailBoxStage != null && Main.mainController.getMainStage() != null) {
			double offsetX = -1335;
			double offsetY = 227;
			DetailBoxStage.setX(Main.mainController.getMainStage().getX() + Main.mainController.getMainStage().getWidth() + offsetX);
			DetailBoxStage.setY(Main.mainController.getMainStage().getY() + offsetY);
		}
	}

}