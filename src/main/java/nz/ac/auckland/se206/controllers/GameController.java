package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.CanvasRenderer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.KeyState;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.game.BoundsObject;
import nz.ac.auckland.se206.game.Player;
import nz.ac.auckland.se206.gpt.Ai;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class GameController implements BaseController {
  @FXML protected Canvas gameCanvas;
  @FXML protected Button hintButton;
  @FXML protected ImageView exitHackerPanelImage;
  @FXML protected ImageView hackerIcon;
  @FXML protected Rectangle hackerRectangle;
  @FXML protected TextArea hackerTextArea;
  @FXML protected Label mainTimerLabel;

  protected Ai ai = new Ai();
  protected GraphicsContext graphicsContext;
  protected CanvasRenderer renderer;
  protected Player player;
  protected ArrayList<BoundsObject> boundsObjects;
  protected boolean paused = true;

  public void initialize() {
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
  public void onExitClicked() {
    disableHackerPanel();
  }

  @FXML
  public void onHintPressed() {
    enableHackerPanel();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintAndExit();
            if (GameState.isHard || (GameState.hintsLeft <= 0)) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
              enableHintAndExit();
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
            enableHintAndExit();
            if (GameState.isMedium) {
              GameState.hintsLeft--;
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  public void disableHackerPanel() {
    hackerIcon.toBack();
    hackerRectangle.toBack();
    hackerTextArea.toBack();
    exitHackerPanelImage.toBack();
    exitHackerPanelImage.setDisable(true);
    gameCanvas.requestFocus();
  }

  public void disableHintAndExit() {
    hintButton.setDisable(true);
    exitHackerPanelImage.setDisable(true);
  }

  public void enableHintAndExit() {
    hintButton.setDisable(false);
    exitHackerPanelImage.setDisable(false);
  }

  public void enableHackerPanel() {
    hackerRectangle.toFront();
    hackerIcon.toFront();
    hackerTextArea.toFront();
    exitHackerPanelImage.toFront();
    exitHackerPanelImage.setDisable(false);
    gameCanvas.requestFocus();
  }
}
