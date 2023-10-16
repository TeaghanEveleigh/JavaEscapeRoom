package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class LoadingMenuController {
  @FXML private ProgressBar loadingBar;

  @FXML
  private void initialize() {
    return;
  }

  public void updateLoadingBar(double progress) {
    loadingBar.setProgress(progress);
  }

  public void resetLoadingBar() {
    loadingBar.setProgress(0);
  }
}
