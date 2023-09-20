package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import nz.ac.auckland.se206.KeyState;

public class Player extends Sprite {

  public static final String imagePath = "/images/player.png";
  private int moveSpeed = 5;
  private int oldX;
  private int oldY;
  private ArrayList<SolidBox> boundingBoxes;

  public Player(int width, int height, int posX, int posY) {
    super(imagePath, width, height, posX, posY);
    oldX = posX;
    oldY = posY;
    boundingBoxes = new ArrayList<SolidBox>();
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

    preventBoundingBoxCollision();
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
    if (this.posX > 816 - this.width) {
      this.posX = 816 - (int) this.width;
    }
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
    if (this.posY > 585 - this.height) {
      this.posY = 585 - (int) this.height;
    }
  }

  public void setBoundingBoxes(ArrayList<SolidBox> boundingBoxes) {
    this.boundingBoxes = boundingBoxes;
  }

  private void preventBoundingBoxCollision() {
    for (SolidBox box : this.boundingBoxes) {
      if (this.getBounds().intersects(box.getBounds())) {
        this.posX = oldX;
        this.posY = oldY;
        return;
      }
    }

    oldX = posX;
    oldY = posY;
  }

  public Rectangle2D getBounds() {
    return new Rectangle2D(posX, posY, width, height);
  }

  @Override
  public void setPosX(int posX) {
    super.setPosX(posX);
    this.oldX = posX;
  }

  @Override
  public void setPosY(int posY) {
    super.setPosY(posY);
    this.oldY = posY;
  }
}
