package myapp.Game;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import myapp.Main;
import myapp.MainController;
import myapp.SuggestionBox.ContextMenuController;
import myapp.Game.CrossBoard.CrossBoardGameController;


public class GameMenuController {
  public  ContextMenu loadGameMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(GameMenuController.class.getResource("/myapp/GameMenu.fxml"));
      ContextMenu gameMenu = loader.load();
      GameMenuController controller = loader.getController();
      gameMenu.setUserData(controller);
      return gameMenu;
    } catch (IOException e) {
      e.printStackTrace();
      return new ContextMenu();
    }
  }

  public void handleCrossBoard() throws IOException {
    System.out.println("Game1");
    showCrossBoardGameBox();

  }

  public void handleGame2()throws IOException {
    System.out.println("Game2");

  }
  private static Stage Game1BoxStage;

  public void showCrossBoardGameBox() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/myapp/CrossBoardGameBox.fxml"));
    Parent layout = loader.load();
    CrossBoardGameController crossBoardGameController = loader.getController();
    if (Game1BoxStage != null) {
      Game1BoxStage.close();
    }
    Game1BoxStage = new Stage();
    Game1BoxStage.initOwner(Main.mainController.getMainStage());
    Game1BoxStage.initModality(Modality.NONE);
    ProgressBar progressBar = new ProgressBar();
    progressBar.getStylesheets().add(ContextMenuController.class.getResource("/myapp/Styling.css").toExternalForm());
    progressBar.getStyleClass().add("progress-bar");
    Text loadingText = new Text("Loading.....");
    loadingText.setStyle("-fx-font-weight:900");
    StackPane loadingPane = new StackPane();
    loadingPane.getChildren().add(progressBar);
    loadingPane.getChildren().add(loadingText);
    loadingPane.setStyle("-fx-border-color: rgb(7, 17, 17);-fx-border-width: 5;-fx-background-radius: 30; -fx-border-radius:  20");
    Scene loadingscene = new Scene(loadingPane,360,500);
    Game1BoxStage.setResizable(false);
    Game1BoxStage.initStyle(StageStyle.TRANSPARENT);
    Game1BoxStage.setScene(loadingscene);
    crossBoardGameController.setStage(Game1BoxStage);


    Main.mainController.getMainStage().xProperty().addListener((observable, oldValue, newValue) -> {
      adjustGame1BoxPosition();
    });

    Main.mainController.getMainStage().yProperty().addListener((observable, oldValue, newValue) -> {
      adjustGame1BoxPosition();
    });

    adjustGame1BoxPosition();
    Button mouse = Main.mainController.mouse;
    mouse.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
    Game1BoxStage.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
      if (event.getButton() == MouseButton.PRIMARY){
        mouse.getStyleClass().remove("mouse-left-click");
        mouse.getStyleClass().add("mouse");
      }
      if (event.getButton() == MouseButton.SECONDARY) {
        mouse.getStyleClass().remove("mouse-right-click");
        mouse.getStyleClass().add("mouse");
      }

    });
    Game1BoxStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
      if (event.getButton() == MouseButton.PRIMARY){
        mouse.getStyleClass().remove("mouse");
        mouse.getStyleClass().add("mouse-left-click");
      }
      if (event.getButton() == MouseButton.SECONDARY) {
        mouse.getStyleClass().remove("mouse");
        mouse.getStyleClass().add("mouse-right-click");
      }

    });

    Game1BoxStage.show();
    Task<Scene> rederTask = new Task<>() {
      @Override
      protected Scene call() throws Exception {

        Thread.sleep(500);
        Scene scene;
        FadeTransition ft = new FadeTransition(Duration.millis(1100), layout);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        scene = new Scene(layout, 360, 500);
        return scene;
      }
    };
    rederTask.setOnSucceeded(event -> Game1BoxStage.setScene(rederTask.getValue()));
    new Thread(rederTask).start();

  }
  private void adjustGame1BoxPosition() {
    if (Game1BoxStage != null && Main.mainController.getMainStage() != null) {
      double offsetX = -380;
      double offsetY = 227;
      Game1BoxStage.setX(Main.mainController.getMainStage().getX() + Main.mainController.getMainStage().getWidth() + offsetX);
      Game1BoxStage.setY(Main.mainController.getMainStage().getY() + offsetY);
    }
  }

}
