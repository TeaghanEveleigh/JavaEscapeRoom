package nz.ac.auckland.se206.game;

import java.util.Set;
import javafx.scene.input.KeyCode;
import nz.ac.auckland.se206.KeyState;

public class Player extends Sprite {

  public static final String imagePath = "/images/player.png";
  private int moveSpeed = 5;

  public Player(int width, int height, int posX, int posY) {
    super(imagePath, width, height, posX, posY);
  }

  public void updateMovement() {
    // Get set of all keys current pressed - polling
    Set<KeyCode> keysPressed = KeyState.getKeysPressed();

    // Run movement code for WASD
    for (KeyCode key : keysPressed) {
      switch (key) {
        case A:
          moveLeft();
          break;
        case D:
          moveRight();
          break;
        case W:
          moveUp();
          break;
        case S:
          moveDown();
          break;
        default:
          break;
      }
    }
  }

  private void moveLeft() {
    this.posX -= moveSpeed;
    // Prevent out of bounds
    if (this.posX < 0) {
      this.posX = 0;
    }
  }

  private void moveRight() {
    this.posX += moveSpeed;
    // Prevent out of bounds
    // if (this.posX > this.rightBound - this.width) {
    //   this.posX = this.rightBound - (int) this.width;
    // }
  }

  private void moveUp() {
    this.posY -= moveSpeed;
    // Prevent out of bounds
    if (this.posY < 0) {
      this.posY = 0;
    }
  }

  private void moveDown() {
    this.posY += moveSpeed;
    // Prevent out of bounds
    // if (this.posY > this.bottomBound - this.height) {
    //   this.posY = this.bottomBound - (int) this.height;
    // }
  }
}
