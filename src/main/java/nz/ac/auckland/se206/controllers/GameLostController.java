package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;

/** Controller class for the game lost view. This runs when the user runs out of time. */
public class GameLostController implements BaseController {

  /**
   * Ends the game and exits the program.
   *
   * @param event the action event triggered by the go back button
   * @throws IOException if the input is not recognised
   */
  @FXML
  private void onExitGame(ActionEvent event) throws IOException {
    App.restartGame();
  }

  /** This method is used to start the controller. */
  @Override
  public void start() {}
}
