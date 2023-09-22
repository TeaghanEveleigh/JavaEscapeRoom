package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.SceneManager;

/** Controller class for the game won view. */
public class GameWonController implements BaseController {

  /**
   * Ends the game and exits the program
   *
   * @param event the action event triggered by the go back button
   */
  @FXML
  private void onExitGame(ActionEvent event) {
    App.switchScenes(SceneManager.AppUi.MAIN_MENU);
  }
}
