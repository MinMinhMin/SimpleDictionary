package myapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;


public class GameMenuController {
  static ContextMenu loadGameMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(GameMenuController.class.getResource("GameMenu.fxml"));
      ContextMenu gameMenu = loader.load();
      GameMenuController controller = loader.getController();
      gameMenu.setUserData(controller);
      return gameMenu;
    } catch (IOException e) {
      e.printStackTrace();
      return new ContextMenu();
    }
  }

  public void handleGame1() throws IOException {
    System.out.println("Game1");

  }

  public void handleGame2()throws IOException {
    System.out.println("Game2");

  }

}
