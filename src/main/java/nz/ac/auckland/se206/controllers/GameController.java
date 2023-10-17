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
import javafx.scene.text.Font;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * Controller class for the main rooms. This is where the player can move around the map and
 * interact with the game world. Each main room extends this class as they all share the
 * functionality in this class.
 */
public class GameController extends HackerUiToggler implements BaseController {

  /** Updates the checklist in the security room, laser room, and exit room. */
  public static void updateAllChecklists() {
    SecurityRoomController securityRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    securityRoomController.updateChecklist(); // Updates the checklist in the security room
    LaserRoomController laserRoomController =
        (LaserRoomController) SceneManager.getUiController(AppUi.DINOSAUR_ROOM);
    laserRoomController.updateChecklist(); // Updates the checklist in the dinosaur room
    ExitRoomController exitRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    exitRoomController.updateChecklist(); // Updates the checklist in the exit room
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
  @FXML private ImageView planNote;

  protected GraphicsContext graphicsContext;
  protected CanvasRenderer renderer;
  protected Player player;
  protected ArrayList<BoundsObject> boundsObjects;
  protected boolean paused = true;
  private String scribble =
      getClass().getResource("/sounds/pencil_check_mark_2-105940.mp3").toString();
  private Media media = new Media(scribble);
  private MediaPlayer startSound = new MediaPlayer(media);


  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  public void initialize() {
    Font font = Font.loadFont(getClass().getResource("/fonts/KgTenThousandReasons-R1ll.ttf").toExternalForm(), 12);
    disabledCameraLabel.setFont(font);
    disabledLasersLabel.setFont(font);
    stolenTreasureLabel.setFont(font);
    objectivesLabel.setFont(font);
    keycodeFoundLabel.setFont(font);

    disbleObjectives();
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel); // Subscribes the main timer to the main timer label
    gameCanvas.requestFocus();
    disableHackerPanel(); // Disables the hacker panel
    hackerTextArea.setEditable(false);
    graphicsContext = gameCanvas.getGraphicsContext2D();
    renderer = new CanvasRenderer(gameCanvas, graphicsContext);
    boundsObjects = new ArrayList<BoundsObject>();

    // Creates the player
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
  }

  /** Pauses the room to prevent the player from moving when not appropriate. */
  public void pauseRoom() {
    paused = true;
    player.stopRunSounds();
  }

  /** Unpauses the room to allow the player to move. */
  public void unpauseRoom() {
    paused = false;
  }

  /**
   * Adds a bounds object to the room.
   *
   * @param keyEvent the key event that triggered the bounds object.
   */
  @FXML
  public void keyPressedHandler(KeyEvent keyEvent) {
    KeyState.keyPressed(keyEvent.getCode());
  }

  /**
   * Removes a bounds object from the room.
   *
   * @param keyEvent the key event that triggered the bounds object.
   */
  @FXML
  public void keyReleasedHandler(KeyEvent keyEvent) {
    KeyState.keyReleased(keyEvent.getCode());
  }

  /** Runs when the player clicks the objectives exit cross. */
  @FXML
  public void onObjectiveExitClicked() {
    disbleObjectives();
  }

  /** Runs when the player clicks the objectives button. */
  @FXML
  public void onObjectivePressed() {
    enableObjectives();
  }

  /** Disables the objectives panel. */
  public void disbleObjectives() {
    planNote.toBack();
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
    gameCanvas.requestFocus(); // Requests focus for the game canvas
  }

  /** Enables the objectives panel. */
  public void enableObjectives() {
    planNote.toFront();
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
    gameCanvas.requestFocus(); // Requests focus for the game canvas
  }

  /** Updates the checklist in the security room, laser room, and exit room. */
  public void updateChecklist() {
    startSound.play();
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
