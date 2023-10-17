package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
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

/**
 * This class is used to control the game. It implements the BaseController interface. The three
 * main rooms all extend this class.
 */
public class GameController extends HackerUiToggler implements BaseController {

  /** This method is used to update all checklists of all game rooms. */
  public static void updateAllChecklists() {
    // Get all room controllers and update their checklists
    ExitRoomController securityRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    securityRoomController.updateChecklist();
    LaserRoomController laserRoomController =
        (LaserRoomController) SceneManager.getUiController(AppUi.DINOSAUR_ROOM);
    laserRoomController.updateChecklist();
    SecurityRoomController exitRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    exitRoomController.updateChecklist();
  }

  /** This method resets all checklists. */
  public static void resetAllChecklists() {
    // Get and reset all room controllers checklists
    ExitRoomController securityRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    securityRoomController.resetChecklist();
    LaserRoomController laserRoomController =
        (LaserRoomController) SceneManager.getUiController(AppUi.DINOSAUR_ROOM);
    laserRoomController.resetChecklist();
    SecurityRoomController exitRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    exitRoomController.resetChecklist();
  }

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
  @FXML protected Label mainTimerLabel;

  protected GraphicsContext graphicsContext;
  protected CanvasRenderer renderer;
  protected Player player;
  protected ArrayList<BoundsObject> boundsObjects;
  protected boolean paused = true;
  private String scribble =
      getClass().getResource("/sounds/pencil_check_mark_2-105940.mp3").toString();
  private Media media = new Media(scribble);
  private MediaPlayer startSound = new MediaPlayer(media);

  protected boolean started = false;
  private AnimationTimer timer;

  /** Initialises the game controller - run when first loaded. */
  public void initialize() {
    // Load and set the font
    Font font =
        Font.loadFont(
            getClass().getResource("/fonts/KgTenThousandReasons-R1ll.ttf").toExternalForm(), 12);
    disabledCameraLabel.setFont(font);
    disabledLasersLabel.setFont(font);
    stolenTreasureLabel.setFont(font);
    objectivesLabel.setFont(font);
    keycodeFoundLabel.setFont(font);

    player = new Player(50, 100, 50, 50);
    boundsObjects = new ArrayList<BoundsObject>();

    // Must run reset code when initialising - contains initialisation code
    reset();
    disbleObjectives();
    gameCanvas.requestFocus();
    disableHackerPanel();
    hackerTextArea.setEditable(false);
    graphicsContext = gameCanvas.getGraphicsContext2D();
    // Create a new renderer for the player
    renderer = new CanvasRenderer(gameCanvas, graphicsContext);

    renderer.addEntity(player);

    // Create game loop timer for movement / interaction
    timer =
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

  /** Pauses movement / interaction timer and stops running sound. */
  public void pauseRoom() {
    paused = true;
    player.stopRunSounds();
  }

  /** This method is used to unpause room. */
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

  /** This method is run to disable the objectives panel. */
  public void disbleObjectives() {
    // Send all elements to back of scene
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
    // Show objectives button and enable it it
    objectivesButton.setVisible(true);
    objectivesButton.setDisable(false);
    gameCanvas.requestFocus();
  }

  /** This method is run to enble the objectives panel. */
  public void enableObjectives() {
    // Send all elements of the objectives panel to the front of the screen
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
    // Show objectives button and set it to be enabled
    objectivesButton.setVisible(false);
    objectivesButton.setDisable(true);
    gameCanvas.requestFocus();
  }

  /** Updates the checklist based on what the player has completed. */
  public void updateChecklist() {
    startSound.stop(); // Stop any currently playing sound
    startSound.seek(Duration.ZERO); // Reset the sound to the beginning

    startSound.setOnEndOfMedia(
        new Runnable() { // Set an event handler to reset the media player when the sound ends
          @Override
          public void run() {
            startSound.stop(); // Stop the sound
            startSound.seek(Duration.ZERO); // Reset the media player to the beginning
          }
        });

    startSound.play(); // Play the sound
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

  /** This method is used to reset the checklist. */
  public void resetChecklist() {
    Platform.runLater(
        () -> {
          // Fill laser circles with white to reset
          disabledLasersCircle.setFill(Color.WHITE);
          stolenTreasureCircle.setFill(Color.WHITE);
          disabledCameraCircle.setFill(Color.WHITE);
          keycodeFoundCircle.setFill(Color.WHITE);
          exitUnlockedCircle.setFill(Color.WHITE);
        });
  }

  /** This method is used to start the controller. */
  @Override
  public void start() {}

  public void reset() {
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel);
  }
}
