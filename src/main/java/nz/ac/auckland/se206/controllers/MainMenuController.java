package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;

public class MainMenuController implements BaseController {
  @FXML private Button startGameButton;
  @FXML private Button tutorialButton;
  @FXML private Button gameSettingButton;

  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.setRoot(AppUi.GAME_SETTINGS);
  }

  @FXML
  private void onTutorialPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }

  @FXML
  private void onStartPressed(ActionEvent event) throws IOException {
    Timers.getInstance().initializeMainCountdown(GameState.timeLimit);

    FXMLLoader dinosaurRoomLoader = App.getFxmlLoader("room1");
    SceneManager.addUi(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.load());
    SceneManager.addController(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.getController());

    FXMLLoader exitRoomLoader = App.getFxmlLoader("securityroom");
    SceneManager.addUi(AppUi.EXIT_ROOM, exitRoomLoader.load());
    SceneManager.addController(AppUi.EXIT_ROOM, exitRoomLoader.getController());

    FXMLLoader securityRoomLoader = App.getFxmlLoader("room2");
    SceneManager.addUi(AppUi.SECURITY_ROOM, securityRoomLoader.load());
    SceneManager.addController(AppUi.SECURITY_ROOM, securityRoomLoader.getController());

    App.switchScenes(AppUi.DINOSAUR_ROOM);
  }
}
