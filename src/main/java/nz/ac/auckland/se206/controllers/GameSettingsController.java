package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the game settings view. This is where the player can change the difficulty
 * of the game, the time limit, and whether or not text to speech is enabled.
 */
public class GameSettingsController implements BaseController {
  @FXML private Button backButton;
  @FXML private CheckBox easyCheckBox;
  @FXML private CheckBox mediumCheckBox;
  @FXML private CheckBox hardCheckBox;
  @FXML private CheckBox twoMinutesCheckBox;
  @FXML private CheckBox fourMinutesCheckBox;
  @FXML private CheckBox sixMinutesCheckBox;
  @FXML private CheckBox enableCheckBox;
  @FXML private CheckBox disableCheckBox;

  /**
   * Runs when the back button is pressed. Takes the user to the main menu.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onBackPressed() throws IOException {
    App.setRoot(AppUi.MAIN_MENU);
  }

  /**
   * Runs when the easy checkbox is pressed. Sets the difficulty to easy.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onEasyPressed() throws IOException {
    mediumCheckBox.setSelected(false);
    hardCheckBox.setSelected(false);
    GameState.getInstance().setEasy();
  }

  /**
   * Runs when the medium checkbox is pressed. Sets the difficulty to medium.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onMediumPressed() throws IOException {
    easyCheckBox.setSelected(false);
    hardCheckBox.setSelected(false);
    GameState.getInstance().setMedium();
  }

  /**
   * Runs when the hard checkbox is pressed. Sets the difficulty to hard.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onHardPressed() throws IOException {
    mediumCheckBox.setSelected(false);
    easyCheckBox.setSelected(false);
    GameState.getInstance().setHard();
  }

  /**
   * Runs when the two minutes checkbox is pressed. Sets the time limit to two minutes.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onTwoPressed() throws IOException {
    fourMinutesCheckBox.setSelected(false);
    sixMinutesCheckBox.setSelected(false);
    GameState.timeLimit = 2;
  }

  /**
   * Runs when the four minutes checkbox is pressed. Sets the time limit to four minutes.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onFourPressed() throws IOException {
    twoMinutesCheckBox.setSelected(false);
    sixMinutesCheckBox.setSelected(false);
    GameState.timeLimit = 4;
  }

  /**
   * Runs when the six minutes checkbox is pressed. Sets the time limit to six minutes.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onSixPressed() throws IOException {
    fourMinutesCheckBox.setSelected(false);
    twoMinutesCheckBox.setSelected(false);
    GameState.timeLimit = 6;
  }

  /**
   * Runs when the enable checkbox is pressed. Enables text to speech.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onEnablePressed() throws IOException {
    disableCheckBox.setSelected(false);
    GameState.textToSpeech = true;
  }

  /**
   * Runs when the disable checkbox is pressed. Disables text to speech.
   *
   * @throws IOException if the main menu fxml file cannot be found.
   */
  @FXML
  private void onDisablePressed() throws IOException {
    enableCheckBox.setSelected(false);
    GameState.textToSpeech = false;
  }

  @Override
  public void start() {
    return;
  }
}
