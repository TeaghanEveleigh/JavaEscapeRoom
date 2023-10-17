package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Passcode;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.Computer;
import nz.ac.auckland.se206.game.ExitRoomDoor;
import nz.ac.auckland.se206.game.Safe;
import nz.ac.auckland.se206.game.SolidBox;
import nz.ac.auckland.se206.listeners.ComputerListener;
import nz.ac.auckland.se206.listeners.ExitRoomDoorListener;
import nz.ac.auckland.se206.listeners.SafeListener;

/**
 * Controller for the exit room. This is where the player can disable the cameras in the room with
 * the exit as well as find the keycode for the exit door.
 */
public class ExitRoomController extends GameController
    implements ComputerListener, SafeListener, ExitRoomDoorListener {
  @FXML private Text helpHint;
  @FXML private Canvas gameCanvas;
  @FXML private Text interractHint;
  @FXML private Text passwordText;
  @FXML private Button exitBtn;
  @FXML private Text password;
  @FXML private ImageView bigNote;
  @FXML private ImageView note;
  @FXML private ImageView openedSafe;
  @FXML private Label noteLabel;
  @FXML private Label safeLabel;
  @FXML private Rectangle monitorStand;
  @FXML private Line entranceLine1;
  @FXML private Line entranceLine2;
  @FXML private Line entranceLine3;
  @FXML private Text entranceLabel;

  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle computerBounds;
  @FXML private Rectangle doorBounds;
  @FXML private Rectangle safeBounds;
  private boolean safeOpened = false;
  private boolean computerOpened = false;
  private boolean incorrectPassword = false;
  @FXML private ImageView arrow1;
  @FXML private ImageView arrow2;
  @FXML private ImageView arrow3;
  @FXML private Label computerLabel;
  @FXML private Button btnHelp;
  @FXML private Button btnLogin;
  @FXML private Rectangle monitorScreen;
  @FXML private Rectangle rectangleText;
  @FXML private Text titleComputer;
  @FXML private Rectangle boundingBox1;
  @FXML private Rectangle boundingBox5;
  @FXML private Label hintsLabel;
  private Passcode passcode;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @Override
  public void initialize() {
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBox1));
    boundsObjects.add(new SolidBox(boundingBox5));

    boundsObjects.add(new Computer(computerBounds, this));
    boundsObjects.add(new Safe(safeBounds, this));
    boundsObjects.add(new ExitRoomDoor(doorBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    // Set up the floating animations for the arrows
    applyFloatingAnimation(arrow1);
    applyFloatingAnimation(arrow2);
    applyFloatingAnimation(arrow3);
  }

  /** Shows the computer label to the user. */
  @FXML
  private void showComputerLabel() {
    computerLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow1.toBack();
  }

  /** Hides the computer label from the user. */
  @FXML
  void hideComputerLabel() {
    computerLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow1.toFront();
  }

  /** This method is used to open the computer when the user clicks on it. */
  @FXML
  private void openComputer() {
    // Bring the computer to the front
    monitorScreen.toFront();
    btnHelp.toFront();
    btnLogin.toFront();
    rectangleText.toFront();
    titleComputer.toFront();
    monitorStand.toFront();
    exitBtn.toFront();
    passwordText.toFront();
    computerOpened = true; // set computer to opened
  }

  /** This method is used to closes the computer when the user exits it. */
  @FXML
  private void onHideComputer() {
    // Send the computer to the back
    monitorScreen.toBack();
    btnHelp.toBack();
    btnLogin.toBack();
    rectangleText.toBack();
    titleComputer.toBack();
    monitorStand.toBack();
    exitBtn.toBack();
    passwordText.toBack();
    computerOpened = false; // set computer to closed
    paused = false; // unpause room
    helpHint.toBack();
  }

  /** Shows the entrance label to the user. */
  @FXML
  private void showEntranceLabel() {
    // Make the entrance label visible
    entranceLine1.setOpacity(1);
    entranceLine2.setOpacity(1);
    entranceLine3.setOpacity(1);
    entranceLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow3.toBack();
  }

  /** Hides the entrance label from the user. */
  @FXML
  private void hideEntranceLabel() {
    // Make the entrance label invisible
    interractHint.setOpacity(0);
    entranceLine1.setOpacity(0);
    entranceLine2.setOpacity(0);
    entranceLine3.setOpacity(0);
    entranceLabel.setOpacity(0);
    arrow3.toFront();
  }

  /** Hides the safe label from the user. */
  @FXML
  private void hideSafeLabel() {
    safeLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow2.toFront();
  }

  /** Shows the safe label to the user. */
  @FXML
  private void showSafeLabel() {
    safeLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow2.toBack();
  }

  /** Shows the note label to the user. */
  @FXML
  private void showNoteLabel() {
    noteLabel.setOpacity(1);
  }

  /** Hides the note label from the user. */
  @FXML
  private void hideNoteLabel() {
    noteLabel.setOpacity(0);
    interractHint.setOpacity(0);
  }

  /** Runs when the safe has been opened. */
  @FXML
  public void safeOpen() {
    safeOpened = true; // set safe to opened
    // Bring the opened safe and note to the front
    openedSafe.toFront();
    note.toFront();
    noteLabel.toFront();
    hideSafeLabel();
    // Bring buttons and game canvas to the front
    gameCanvas.toFront();
    objectivesButton.toFront();
    talkToHackerButton.toFront();
    mainTimerLabel.toFront();
  }

  public void closeSafe() {
    safeOpened = false;
    openedSafe.toBack();
    note.toBack();
    noteLabel.toBack();
  }

  /** Shows the keycode on a note to the user. */
  @FXML
  private void showBigNote() {
    bigNote.toFront();
    password.toFront();
  }

  /** Hides the keycode on a note from the user. */
  private void hideBigNote() {
    bigNote.toBack();
    password.toBack();
  }

  /** Switches to the memory game. */
  @FXML
  private void memoryGame() {
    // switch to memory game on this click
  }

  /** Runs when the player is at the enterance. */
  @FXML
  private void gotoEntrance() {}

  /** Runs when the player interacts with the computer. */
  @Override
  public void computerInteracted() {
    hideComputerLabel();
    pauseRoom();
    computerOpened = true;
    passwordText.setText("");
    openComputer();
  }

  /** Runs when the player goes near the computer. */
  @Override
  public void computerTouched() {
    showComputerLabel();
  }

  /** Runs when the player moves away from the computer. */
  @Override
  public void computerNotTouched() {
    hideComputerLabel();
    onHideComputer();
    helpHint.toBack();
  }

  /** Runs when the player interacts with the safe. */
  @Override
  public void safeInteracted() {
    if (safeOpened) { // if safe is opened
      showBigNote();
    } else {
      pauseRoom();
      if (!SceneManager.containsUi(AppUi.MEMORY_GAME)) { // if memory game is not loaded
        // load memory game
        FXMLLoader memoryGameLoader = App.getFxmlLoader("memorygame");
        try {
          SceneManager.addUi(AppUi.MEMORY_GAME, memoryGameLoader.load());
        } catch (IOException e) {
          e.printStackTrace();
        }
        SceneManager.addController(AppUi.MEMORY_GAME, memoryGameLoader.getController());
      }
      MemoryGameController memoryController =
          (MemoryGameController) SceneManager.getUiController(AppUi.MEMORY_GAME);
      memoryController.start(); // start memory game
      App.switchScenes(AppUi.MEMORY_GAME); // switch to memory game
    }
  }

  /** Runs when the player goes near the safe. */
  @Override
  public void safeTouched() {
    if (!safeOpened) {
      showSafeLabel();
    } else {
      showNoteLabel();
    }
  }

  /** Runs when the player moves away from the safe. */
  @Override
  public void safeNotTouched() {
    hideSafeLabel();
    hideNoteLabel();
    hideBigNote();
  }

  /** Runs when the player interacts with the exit door. */
  @Override
  public void exitDoorInteracted() {
    pauseRoom();
    App.switchScenes(AppUi.EXIT_ROOM); // switch to exit room
  }

  /** Runs when the player goes near the exit door. */
  @Override
  public void exitDoorTouched() {
    showEntranceLabel();
  }

  /** Runs when the player moves away from the exit door. */
  @Override
  public void exitDoorUntouched() {
    hideEntranceLabel();
  }

  /** Runs when the player presses a key. */
  @Override
  @FXML
  public void keyPressedHandler(KeyEvent keyEvent) {
    if (!computerOpened) { // if computer is not opened
      super.keyPressedHandler(keyEvent);
    } else {
      updatePasswordText(keyEvent);
    }
  }

  /** Updates the password that the user has entered. */
  private void updatePasswordText(KeyEvent keyEvent) {
    String text = passwordText.getText();

    if (incorrectPassword) {
      // clear the password text if the user has entered an incorrect password
      passwordText.setText("");
      incorrectPassword = false;
    }

    if (keyEvent.getCode().equals(KeyCode.ENTER)) { // if enter is pressed
      checkPassword();
    } else if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)
        && text.length() > 0) { // if backspace is pressed
      passwordText.setText(text.substring(0, text.length() - 1));
    } else { // if any other key is pressed
      passwordText.setText(passwordText.getText() + keyEvent.getText());
    }
  }

  /** Checks if the password is correct. */
  private void checkPassword() {
    if (passwordText.getText().equals(passcode.getFullNum())) { // if password is correct
      GameState.isCamerasDisabled = true;
      GameController.updateAllChecklists(); // update checklists
      System.out.println("correc");
      passwordText.setText("CORRECT");
      GameState.disableCamera(); // disable cameras
    } else { // if password is incorrect
      passwordText.setText("INCORRECT");
      incorrectPassword = true;
    }
  }

  /** Shows the hint for the password to the user. */
  @FXML
  private void onShowHelpHint() {
    helpHint.toFront();
  }

  /**
   * Applies the floating animation to the arrows for interactable objects.
   *
   * @param imageView The image view to apply the animation to.
   */
  private void applyFloatingAnimation(ImageView imageView) {
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByY(5);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
  }

  /** This method is used to signal that the room hs started. */
  @Override
  public void start() {
    started = true;
  }

  /** This method is used to reset the room to its original state. */
  @Override
  public void reset() {
    super.reset();
    safeOpened = false;
    computerOpened = false;
    incorrectPassword = false;
    GameState value = GameState.getInstance();
    value.subscribe(hintsLabel); // reset the hints label
    passcode = Passcode.getInstance();
    password.setText(passcode.getKeyCode());
    this.player.setPosX(54);
    this.player.setPosY(450);
    closeSafe(); // reset the safe
  }
}
