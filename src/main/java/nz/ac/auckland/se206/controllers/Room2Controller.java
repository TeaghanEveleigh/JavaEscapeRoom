package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ComputerListener;
import nz.ac.auckland.se206.ExitRoomDoorListener;
import nz.ac.auckland.se206.SafeListener;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.Computer;
import nz.ac.auckland.se206.game.ExitRoomDoor;
import nz.ac.auckland.se206.game.Safe;
import nz.ac.auckland.se206.game.SolidBox;

public class Room2Controller extends GameController
    implements ComputerListener, SafeListener, ExitRoomDoorListener {
  @FXML private Text interractHint;
  @FXML private Text passwordText;
  private Boolean computerOpen = false;
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

  @FXML Label computerLabel;
  @FXML Button btnHelp;
  @FXML Button btnLogin;
  @FXML Rectangle monitorScreen;
  @FXML Rectangle rectangleText;
  @FXML Text titleComputer;

  @Override
  public void initialize() {
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new Computer(computerBounds, this));
    boundsObjects.add(new Safe(safeBounds, this));
    boundsObjects.add(new ExitRoomDoor(doorBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(300);
  }

  @FXML
  private void showComputerLabel() {
    computerLabel.setOpacity(1);
    interractHint.setOpacity(1);
  }

  @FXML
  void hideComputerLabel() {
    computerLabel.setOpacity(0);
    interractHint.setOpacity(0);
  }

  @FXML
  void openComputer() {
    monitorScreen.toFront();
    btnHelp.toFront();
    btnLogin.toFront();
    rectangleText.toFront();
    titleComputer.toFront();
    monitorStand.toFront();
    exitBtn.toFront();
    passwordText.toFront();
    computerOpen = true;
  }

  @FXML
  private void hideComputer() {
    monitorScreen.toBack();
    btnHelp.toBack();
    btnLogin.toBack();
    rectangleText.toBack();
    titleComputer.toBack();
    monitorStand.toBack();
    exitBtn.toBack();
    passwordText.toBack();
    computerOpen = false;
  }

  @FXML
  private void showEntranceLabel() {
    entranceLine1.setOpacity(1);
    entranceLine2.setOpacity(1);
    entranceLine3.setOpacity(1);
    entranceLabel.setOpacity(1);
    interractHint.setOpacity(1);
  }

  @FXML
  private void hideEntranceLabel() {
    interractHint.setOpacity(0);
    entranceLine1.setOpacity(0);
    entranceLine2.setOpacity(0);
    entranceLine3.setOpacity(0);
    entranceLabel.setOpacity(0);
  }

  @FXML
  private void hideSafeLabel() {
    safeLabel.setOpacity(0);
    interractHint.setOpacity(0);
  }

  @FXML
  private void showSafeLabel() {
    safeLabel.setOpacity(1);
    interractHint.setOpacity(1);
  }

  @FXML
  private void showNoteLabel() {
    noteLabel.setOpacity(1);
  }

  @FXML
  private void hideNoteLabel() {
    noteLabel.setOpacity(0);
    interractHint.setOpacity(0);
  }

  @FXML
  private void safeOpen() {
    safeOpened = true;
    openedSafe.toFront();
    note.toFront();
    noteLabel.toFront();
  }

  @FXML
  private void showBigNote() {
    bigNote.toFront();
    password.toFront();
  }

  @FXML
  private void memoryGame() {
    // switch to memory game on this click

  }

  @FXML
  public void appendPassword(KeyEvent e) {
    if (computerOpen) {
      // Check if the key pressed is backspace
      if (e.getCode() == KeyCode.BACK_SPACE && passwordText.getText().length() > 0) {
        // Remove the last character from the password Text object
        passwordText.setText(
            passwordText.getText().substring(0, passwordText.getText().length() - 1));
      } else if (passwordText.getText().length() < 10) {
        // Get the character pressed
        char keyChar = e.getCharacter().charAt(0);
        // Append the character to the password Text object
        passwordText.setText(passwordText.getText() + String.valueOf(keyChar));
      }
      // Consume the event to prevent default behavior
      e.consume();
    }
  }

  @FXML
  private void gotoEntrance() {}

  @Override
  public void computerInteracted() {
    hideComputerLabel();
    openComputer();
  }

  @Override
  public void computerTouched() {
    showComputerLabel();
  }

  @Override
  public void computerUntouched() {
    hideComputerLabel();
    hideComputer();
  }

  @Override
  public void safeInteracted() {
    if (safeOpened) {
      showBigNote();
    }
    // need to swap to memory game
  }

  @Override
  public void safeTouched() {
    if (!safeOpened) {
      showSafeLabel();
    } else {
      showNoteLabel();
    }
  }

  @Override
  public void safeUntouched() {
    hideSafeLabel();
    hideNoteLabel();
  }

  @Override
  public void exitDoorInteracted() {
    paused = true;
    App.switchScenes(AppUi.EXIT_ROOM);
  }

  @Override
  public void exitDoorTouched() {
    showEntranceLabel();
  }

  @Override
  public void exitDoorUntouched() {
    hideEntranceLabel();
  }
}
