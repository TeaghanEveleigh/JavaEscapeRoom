package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.CanvasRenderer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HackerUiToggler;
import nz.ac.auckland.se206.KeyState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.game.BoundsObject;
import nz.ac.auckland.se206.game.Player;

public class GameController extends HackerUiToggler implements BaseController {

  // Updates all checklists
  public static void updateAllChecklists() {
    SecurityRoomController securityRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    securityRoomController.updateChecklist();
    LaserRoomController laserRoomController =
        (LaserRoomController) SceneManager.getUiController(AppUi.DINOSAUR_ROOM);
    laserRoomController.updateChecklist();
    ExitRoomController exitRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    exitRoomController.updateChecklist();
  }

  @FXML protected Canvas gameCanvas;

  // Checklist
  @FXML protected Rectangle checklistRectangle;
  @FXML protected Label objectivesLabel;
  @FXML protected Label disabledLasersLabel;
  @FXML protected Label stolenTreasureLabel;
  @FXML protected Label disabledCameraLabel;
  @FXML protected Label keycodeFoundLabel;
  @FXML protected Label exitUnlockedLabel;
  @FXML protected Circle disabledLasersCircle;
  @FXML protected Circle stolenTreasureCircle;
  @FXML protected Circle disabledCameraCircle;
  @FXML protected Circle keycodeFoundCircle;
  @FXML protected Circle exitUnlockedCircle;
  @FXML protected ImageView exitObjectiveImage;

  protected GraphicsContext graphicsContext;
  protected CanvasRenderer renderer;
  protected Player player;
  protected ArrayList<BoundsObject> boundsObjects;
  protected boolean paused = true;

  public void initialize() {
    disbleObjectives();
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel);
    gameCanvas.requestFocus();
    disableHackerPanel();
    hackerTextArea.setEditable(false);
    graphicsContext = gameCanvas.getGraphicsContext2D();
    renderer = new CanvasRenderer(gameCanvas, graphicsContext);
    boundsObjects = new ArrayList<BoundsObject>();

    player = new Player(50, 100, 50, 50);
    renderer.addEntity(player);

    AnimationTimer timer =
        new AnimationTimer() {

          @Override
          public void handle(long now) {
            if (paused) {
              return;
            }
            player.updateMovement();
            renderer.renderEntities();
          }
        };

    timer.start();
    pauseRoom();
  }

  public void pauseRoom() {
    paused = true;
    player.stopRunSounds();
  }

  public void unpauseRoom() {
    paused = false;
  }

  @FXML
  public void keyPressedHandler(KeyEvent keyEvent) {
    KeyState.keyPressed(keyEvent.getCode());
  }

  @FXML
  public void keyReleasedHandler(KeyEvent keyEvent) {
    KeyState.keyReleased(keyEvent.getCode());
  }

  @FXML
  public void onTalkToHackerPressed() {
    enableHackerPanel();
  }

  @FXML
  public void onChatPressed() {
    enableChat();
  }

  @FXML
  public void onObjectiveExitClicked() {
    disbleObjectives();
  }

  @FXML
  public void onObjectivePressed() {
    enableObjectives();
  }

  // Disables the objectives panel
  public void disbleObjectives() {
    checklistRectangle.toBack();
    objectivesLabel.toBack();
    disabledLasersLabel.toBack();
    stolenTreasureLabel.toBack();
    disabledCameraLabel.toBack();
    keycodeFoundLabel.toBack();
    exitUnlockedLabel.toBack();
    disabledLasersCircle.toBack();
    stolenTreasureCircle.toBack();
    disabledCameraCircle.toBack();
    keycodeFoundCircle.toBack();
    exitUnlockedCircle.toBack();
    exitObjectiveImage.toBack();
    objectivesButton.setVisible(true);
    objectivesButton.setDisable(false);
    gameCanvas.requestFocus();
  }

  // Enbles the objectives panel
  public void enableObjectives() {
    checklistRectangle.toFront();
    objectivesLabel.toFront();
    disabledLasersLabel.toFront();
    stolenTreasureLabel.toFront();
    disabledCameraLabel.toFront();
    keycodeFoundLabel.toFront();
    exitUnlockedLabel.toFront();
    disabledLasersCircle.toFront();
    stolenTreasureCircle.toFront();
    disabledCameraCircle.toFront();
    keycodeFoundCircle.toFront();
    exitUnlockedCircle.toFront();
    exitObjectiveImage.toFront();
    objectivesButton.setVisible(false);
    objectivesButton.setDisable(true);
    gameCanvas.requestFocus();
  }

  // Updates the checklist based on what the player has completed
  public void updateChecklist() {
    if (GameState.isLasersDisabled) { // lasers disabled
      Platform.runLater(
          () -> {
            disabledLasersCircle.setFill(Color.BLACK);
          });
    }
    if (GameState.isCamerasDisabled) { // cameras disabled
      Platform.runLater(
          () -> {
            disabledCameraCircle.setFill(Color.BLACK);
          });
    }
    if (GameState.isTreasureStolen) { // treasure stolen
      Platform.runLater(
          () -> {
            stolenTreasureCircle.setFill(Color.BLACK);
          });
    }
    if (GameState.isKeycodeFound) { // keycode found
      Platform.runLater(
          () -> {
            keycodeFoundCircle.setFill(Color.BLACK);
          });
    }
    if (GameState.isExitDoorUnlocked) { // exit door unlocked
      Platform.runLater(
          () -> {
            exitUnlockedCircle.setFill(Color.BLACK);
          });
    }
  }
}
