package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;

public class MainMenuController {
  @FXML private Button startGameButton;
  @FXML private Button tutorialButton;
  @FXML private Button gameSettingButton;

  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.setRoot("gamesettings");
  }

  @FXML
  private void onTutorialPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }

  @FXML
  private void onStartPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }
}
