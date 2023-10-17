package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

/** Controller class for the loading menu view. This runs when the game is loading. */
public class LoadingMenuController {
  @FXML private ProgressBar loadingBar;

  /**
   * This method is used to initialise the controller. It is called automatically when the fxml file
   * is loaded.
   */
  @FXML
  private void initialize() {}

  public void updateLoadingBar(double progress) {
    loadingBar.setProgress(progress);
  }

  /** This method is used to reset the loading bar to 0. */
  public void resetLoadingBar() {
    loadingBar.setProgress(0);
  }
}
