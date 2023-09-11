package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class GameSettingsController {
  @FXML private Button backButton;
  @FXML private CheckBox easyCheckBox;
  @FXML private CheckBox mediumCheckBox;
  @FXML private CheckBox hardCheckBox;
  @FXML private CheckBox twoMinutesCheckBox;
  @FXML private CheckBox fourMinutesCheckBox;
  @FXML private CheckBox sixMinutesCheckBox;
  @FXML private CheckBox enableCheckBox;
  @FXML private CheckBox disableCheckBox;

  @FXML
  private void onBackPressed() throws IOException {
    App.setRoot(AppUi.MAIN_MENU);
  }

  @FXML
  private void onEasyPressed() throws IOException {
    mediumCheckBox.setSelected(false);
    hardCheckBox.setSelected(false);
  }

  @FXML
  private void onMediumPressed() throws IOException {
    easyCheckBox.setSelected(false);
    hardCheckBox.setSelected(false);
  }

  @FXML
  private void onHardPressed() throws IOException {
    mediumCheckBox.setSelected(false);
    easyCheckBox.setSelected(false);
  }

  @FXML
  private void onTwoPressed() throws IOException {
    fourMinutesCheckBox.setSelected(false);
    sixMinutesCheckBox.setSelected(false);
  }

  @FXML
  private void onFourPressed() throws IOException {
    twoMinutesCheckBox.setSelected(false);
    sixMinutesCheckBox.setSelected(false);
  }

  @FXML
  private void onSixPressed() throws IOException {
    fourMinutesCheckBox.setSelected(false);
    twoMinutesCheckBox.setSelected(false);
  }

  @FXML
  private void onEnablePressed() throws IOException {
    disableCheckBox.setSelected(false);
  }

  @FXML
  private void onDisablePressed() throws IOException {
    enableCheckBox.setSelected(false);
  }
}
