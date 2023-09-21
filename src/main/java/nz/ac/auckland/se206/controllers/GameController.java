package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.CanvasRenderer;
import nz.ac.auckland.se206.KeyState;
import nz.ac.auckland.se206.game.BoundsObject;
import nz.ac.auckland.se206.game.Player;

public class GameController implements BaseController {
  @FXML protected Canvas gameCanvas;

  protected GraphicsContext graphicsContext;
  protected CanvasRenderer renderer;
  protected Player player;
  protected ArrayList<BoundsObject> boundsObjects;
  protected boolean paused = true;

  public void initialize() {
    gameCanvas.requestFocus();
    graphicsContext = gameCanvas.getGraphicsContext2D();
    renderer = new CanvasRenderer(gameCanvas, graphicsContext);
    boundsObjects = new ArrayList<BoundsObject>();

    player = new Player(100, 100, 50, 50);
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
}
