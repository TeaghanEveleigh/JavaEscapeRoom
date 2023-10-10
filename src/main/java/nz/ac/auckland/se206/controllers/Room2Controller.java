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
import nz.ac.auckland.se206.ComputerListener;
import nz.ac.auckland.se206.ExitRoomDoorListener;
import nz.ac.auckland.se206.Passcode;
import nz.ac.auckland.se206.SafeListener;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.Computer;
import nz.ac.auckland.se206.game.ExitRoomDoor;
import nz.ac.auckland.se206.game.Safe;
import nz.ac.auckland.se206.game.SolidBox;

public class Room2Controller extends GameController
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
  Passcode passcode = Passcode.getInstance();

  @Override
  public void initialize() {
    password.setText(passcode.getKeyCode());
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBox1));
    boundsObjects.add(new SolidBox(boundingBox5));

    boundsObjects.add(new Computer(computerBounds, this));
    boundsObjects.add(new Safe(safeBounds, this));
    boundsObjects.add(new ExitRoomDoor(doorBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(450);
    applyFloatingAnimation( arrow1);
    applyFloatingAnimation( arrow2);
    applyFloatingAnimation( arrow3);
  }

  @FXML
  private void showComputerLabel() {
    computerLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow1.toBack();
  }

  @FXML
  void hideComputerLabel() {
    computerLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow2.toFront();
  }

  @FXML
  private void openComputer() {
    monitorScreen.toFront();
    btnHelp.toFront();
    btnLogin.toFront();
    rectangleText.toFront();
    titleComputer.toFront();
    monitorStand.toFront();
    exitBtn.toFront();
    passwordText.toFront();
    computerOpened = true;
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
    computerOpened = false;
    paused = false;
    helpHint.toBack();
  }

  @FXML
  private void showEntranceLabel() {
    entranceLine1.setOpacity(1);
    entranceLine2.setOpacity(1);
    entranceLine3.setOpacity(1);
    entranceLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow3.toBack();
  }

  @FXML
  private void hideEntranceLabel() {
    interractHint.setOpacity(0);
    entranceLine1.setOpacity(0);
    entranceLine2.setOpacity(0);
    entranceLine3.setOpacity(0);
    entranceLabel.setOpacity(0);
    arrow3.toFront();
  }

  @FXML
  private void hideSafeLabel() {
    safeLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow2.toBack();
  }

  @FXML
  private void showSafeLabel() {
    safeLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow2.toBack();
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
  public void safeOpen() {
    safeOpened = true;
    openedSafe.toFront();
    note.toFront();
    noteLabel.toFront();
    hideSafeLabel();
    gameCanvas.toFront();
  }

  @FXML
  private void showBigNote() {
    bigNote.toFront();
    password.toFront();
  }

  private void hideBigNote() {
    bigNote.toBack();
    password.toBack();
  }

  @FXML
  private void memoryGame() {
    // switch to memory game on this click

  }

  @FXML
  private void gotoEntrance() {}

  @Override
  public void computerInteracted() {
    hideComputerLabel();
    pauseRoom();
    computerOpened = true;
    passwordText.setText("");
    openComputer();
  }

  @Override
  public void computerTouched() {
    showComputerLabel();
  }

  @Override
  public void computerNotTouched() {
    hideComputerLabel();
    hideComputer();
    helpHint.toBack();
  }

  @Override
  public void safeInteracted() {
    if (safeOpened) {
      showBigNote();
    } else {
      pauseRoom();
      if (!SceneManager.containsUi(AppUi.MEMORY_GAME)) {
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
      memoryController.start();
      App.switchScenes(AppUi.MEMORY_GAME);
    }
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
  public void safeNotTouched() {
    hideSafeLabel();
    hideNoteLabel();
    hideBigNote();
  }

  @Override
  public void exitDoorInteracted() {
    pauseRoom();
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

  @Override
  @FXML
  public void keyPressedHandler(KeyEvent keyEvent) {
    if (!computerOpened) {
      super.keyPressedHandler(keyEvent);
    } else {
      updatePasswordText(keyEvent);
    }
  }

  private void updatePasswordText(KeyEvent keyEvent) {
    String text = passwordText.getText();

    if (incorrectPassword) {
      passwordText.setText("");
      incorrectPassword = false;
    }

    if (keyEvent.getCode().equals(KeyCode.ENTER)) {
      checkPassword();
    } else if (keyEvent.getCode().equals(KeyCode.BACK_SPACE) && text.length() > 0) {
      passwordText.setText(text.substring(0, text.length() - 1));
    } else {
      passwordText.setText(passwordText.getText() + keyEvent.getText());
    }
  }

  private void checkPassword() {
    if (passwordText.getText().equals(passcode.getFullNum())) {
      System.out.println("correc");
      passwordText.setText("CORRECT");
      SecurityController securityController =
          (SecurityController) SceneManager.getUiController(AppUi.EXIT_ROOM);
      securityController.disableCamera();
    } else {
      passwordText.setText("INCORRECT");
      incorrectPassword = true;
    }
  }
  @FXML private void showHelpHint(){
    helpHint.toFront();
  }
  private void applyFloatingAnimation(ImageView imageView) {
    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByY(5);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
}
}
