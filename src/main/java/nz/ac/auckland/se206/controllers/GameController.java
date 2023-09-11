package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import nz.ac.auckland.se206.CanvasRenderer;
import nz.ac.auckland.se206.KeyState;
import nz.ac.auckland.se206.game.Player;

public class GameController {
  @FXML private Canvas gameCanvas;

  private GraphicsContext graphicsContext;
  private CanvasRenderer renderer;

  public void initialize() {
    graphicsContext = gameCanvas.getGraphicsContext2D();
    renderer = new CanvasRenderer(gameCanvas, graphicsContext);

    Player player = new Player(50, 50, 50, 50);
    renderer.addEntity(player);

    AnimationTimer timer =
        new AnimationTimer() {

          @Override
          public void handle(long now) {
            player.updateMovement();
            renderer.renderEntities();
          }
        };

    timer.start();
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
