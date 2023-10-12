package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.CanvasRenderer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.KeyState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.game.BoundsObject;
import nz.ac.auckland.se206.game.Player;
import nz.ac.auckland.se206.gpt.Ai;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class GameController implements BaseController {
  @FXML protected Canvas gameCanvas;
  @FXML protected Button hintButton;
  @FXML protected Button chatButton;
  @FXML protected Button talkToHackerButton;
  @FXML protected Label hintsLeftLabel;
  @FXML protected Rectangle hackerPanelBackground;
  @FXML protected ImageView exitHackerPanelImage;
  @FXML protected ImageView hackerIcon;
  @FXML protected TextArea hackerTextArea;
  @FXML protected Label mainTimerLabel;
  @FXML protected TextField chatTextField;
  @FXML protected ImageView exitChatImage;
  @FXML protected Button submitButton;
  @FXML protected Rectangle chatPanelBackground;
  @FXML protected Button objectivesButton;

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

  protected Ai ai = new Ai();
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
            if (paused) return;
            player.updateMovement();
            renderer.renderEntities();
          }
        };

    timer.start();
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
  public void onChatExitClicked() {
    disableChat();
  }

  @FXML
  public void onSubmitPressed() {
    String message = chatTextField.getText();
    chatTextField.clear();
    if (message.length() > 0) {
      disableHintChatAndExit();
      try {
        ai.runGpt(
            new ChatMessage("user", GptPromptEngineering.getChatResponse(message)), hackerTextArea);
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
      enableHintChatAndExit();
    }
  }

  @FXML
  public void onHackerExitClicked() {
    disableHackerPanel();
    disableChat();
  }

  @FXML
  public void onObjectiveExitClicked() {
    disbleObjectives();
  }

  @FXML
  public void onObjectivePressed() {
    enableObjectives();
  }

  @FXML
  public void onHintPressed() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit();
            if (GameState.isHard || (GameState.hintsLeft <= 0)) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
              enableHintChatAndExit();
              return null;
            }
            if (!GameState.isLasersDisabled
                && !GameState.isCamerasDisabled) { // neither lasers or cameras are disabled

              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getNothingDisabledHint()),
                  hackerTextArea);
            } else if (GameState.isLasersDisabled) { // lasers disabled
              if (!GameState.isTreasureStolen) { // lasers disabled but treasure not stolen

                ai.runGpt(
                    new ChatMessage(
                        "user", GptPromptEngineering.getLasersDisabledButNotStolenHint()),
                    hackerTextArea);
              } else if (GameState
                  .isCamerasDisabled) { // lasers disabled, treasure stolen, and cameras disabled

                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getBothDisabledHint()),
                    hackerTextArea);
              } else if (!GameState
                  .isCamerasDisabled) { // lasers disabled, treasure stolen, but cameras not
                // disabled

                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getLasersButNotCameraHint()),
                    hackerTextArea);
              }
            } else if (GameState.isCamerasDisabled
                && !GameState.isLasersDisabled) { // both lasers and camera disabled

              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCameraButNotLasersHint()),
                  hackerTextArea);
            }
            enableHintChatAndExit();
            if (GameState.isMedium) {
              GameState.hintsLeft--;
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  // Disables the hacker panel
  public void disableHackerPanel() {
    hackerPanelBackground.toBack();
    hintButton.toBack();
    chatButton.toBack();
    hackerIcon.toBack();
    hintsLeftLabel.toBack();
    hackerTextArea.toBack();
    exitHackerPanelImage.toBack();
    exitHackerPanelImage.setDisable(true);
    talkToHackerButton.setDisable(false);
    gameCanvas.requestFocus();
  }

  // Disables the hint and exit buttons
  public void disableHintChatAndExit() {
    hintButton.setDisable(true);
    chatButton.setDisable(true);
    exitHackerPanelImage.setDisable(true);
  }

  // Enables the hint and exit buttons
  public void enableHintChatAndExit() {
    hintButton.setDisable(false);
    chatButton.setDisable(false);
    exitHackerPanelImage.setDisable(false);
  }

  // Enables the hacker panel
  public void enableHackerPanel() {
    hackerPanelBackground.toFront();
    hintButton.toFront();
    chatButton.toFront();
    hackerIcon.toFront();
    hintsLeftLabel.toFront();
    hackerTextArea.toFront();
    exitHackerPanelImage.toFront();
    exitHackerPanelImage.setDisable(false);
    talkToHackerButton.setDisable(true);
    gameCanvas.requestFocus();
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

  // Enables the chat panel
  public void enableChat() {
    chatPanelBackground.toFront();
    submitButton.toFront();
    submitButton.setDisable(false);
    chatTextField.toFront();
    exitChatImage.toFront();
    exitChatImage.setDisable(false);
    gameCanvas.requestFocus();
  }

  // Disables the chat panel
  public void disableChat() {
    chatPanelBackground.toBack();
    submitButton.toBack();
    submitButton.setDisable(true);
    chatTextField.toBack();
    exitChatImage.toBack();
    exitChatImage.setDisable(true);
    gameCanvas.requestFocus();
  }

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
