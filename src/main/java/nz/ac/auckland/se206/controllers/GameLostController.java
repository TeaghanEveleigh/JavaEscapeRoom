package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;

/** Controller class for the game lost view. */
public class GameLostController implements BaseController {

  /**
   * Ends the game and exits the program
   *
   * @param event the action event triggered by the go back button
   * @throws IOException
   */
  @FXML
  private void onExitGame(ActionEvent event) throws IOException {
    App.restartGame();
  }

  @Override
  public void start() {
    return;
  }
}
