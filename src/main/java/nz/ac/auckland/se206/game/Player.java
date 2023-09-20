package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import nz.ac.auckland.se206.KeyState;

public class Player extends Sprite {

  public static final String imagePath = "/images/player-idle.png";
  private int moveSpeed = 5;
  private int baseHeight;
  private int baseWidth;
  private int oldX;
  private int oldY;
  private ArrayList<BoundsObject> boundingBoxes;
  private Image idleImage;
  private Image runRightImage;
  private Image runLeftImage;

  public Player(int width, int height, int posX, int posY) {
    super(imagePath, width, height, posX, posY);
    baseHeight = height;
    baseWidth = width;
    oldX = posX;
    oldY = posY;
    boundingBoxes = new ArrayList<BoundsObject>();
    idleImage =
        new Image(
            getClass().getResource("/images/player-idle.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
    runRightImage =
        new Image(
            getClass().getResource("/images/player-run-right.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
    runLeftImage =
        new Image(
            getClass().getResource("/images/player-run-left.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
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

    if (keysPressed.size() == 0) {
      resetScale();
      this.image = idleImage;
    }

    handleCollisions();
  }

  private void applyScale() {
    this.width = (int) (baseWidth * 0.9);
    this.height = (int) (baseHeight * 0.9);
  }

  private void resetScale() {
    this.width = baseWidth;
    this.height = baseHeight;
  }

  private void moveLeft() {
    applyScale();
    this.image = runLeftImage;
    this.posX -= moveSpeed;
    // Prevent out of bounds
    if (this.posX < 0) {
      this.posX = 0;
    }
  }

  private void moveRight() {
    applyScale();
    this.image = runRightImage;
    this.posX += moveSpeed;
    if (this.posX > 816 - this.width) {
      this.posX = 816 - (int) this.width;
    }
  }

  private void moveUp() {
    applyScale();
    this.image = runRightImage;
    this.posY -= moveSpeed;
    // Prevent out of bounds
    if (this.posY < 0) {
      this.posY = 0;
    }
  }

  private void moveDown() {
    applyScale();
    this.image = runLeftImage;
    this.posY += moveSpeed;
    if (this.posY > 585 - this.height) {
      this.posY = 585 - (int) this.height;
    }
  }

  public void setBoundingBoxes(ArrayList<BoundsObject> boundingBoxes) {
    this.boundingBoxes = boundingBoxes;
  }

  private void handleCollisions() {
    for (BoundsObject boundsObject : this.boundingBoxes) {
      if (this.getBounds().intersects(boundsObject.getBounds())) {
        if (boundsObject instanceof SolidBox) {
          preventBoundingBoxCollision();
          return;
        } else if (boundsObject instanceof Interactable
            && KeyState.getKeysPressed().contains(KeyCode.E)) {
          Interactable interactable = (Interactable) boundsObject;
          interactable.interact();
        }
      }
    }

    oldX = posX;
    oldY = posY;
  }

  private void preventBoundingBoxCollision() {
    this.posX = oldX;
    this.posY = oldY;
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
