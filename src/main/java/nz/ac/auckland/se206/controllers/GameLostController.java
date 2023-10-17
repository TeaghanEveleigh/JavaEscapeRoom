package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.SceneManager;

/** Controller class for the game lost view. This runs when the user runs out of time. */
public class GameLostController implements BaseController {

  /**
   * Ends the game and exits the program.
   *
   * @param event the action event triggered by the go back button
   */
  @FXML
  private void onExitGame(ActionEvent event) {
    App.switchScenes(SceneManager.AppUi.MAIN_MENU);
  }
}
