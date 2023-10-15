package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
  private MediaPlayer mediaPlayer;

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
    String runSound = getClass().getResource("/sounds/player-walk-sound.mp3").toExternalForm();
    this.mediaPlayer = new MediaPlayer(new Media(runSound));
    this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
  }

  public void updateMovement() {
    // Get set of all keys current pressed - polling
    Set<KeyCode> keysPressed = KeyState.getKeysPressed();

    // Run movement code for WASD
    for (KeyCode key : keysPressed) {
      switch (key) {
        case A:
        case LEFT:
          moveLeft();
          break;
        case D:
        case RIGHT:
          moveRight();
          break;
        case W:
        case UP:
          moveUp();
          break;
        case S:
        case DOWN:
          moveDown();
          break;
        default:
          break;
      }
    }

    if (keysPressed.size() == 0) {
      resetScale();
      stopRunSounds();
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
    playRunSound();
    applyScale();
    this.image = runLeftImage;
    this.posX -= moveSpeed;
    // Prevent out of bounds
    if (this.posX < 0) {
      this.posX = 0;
    }
  }

  private void moveRight() {
    playRunSound();
    applyScale();
    this.image = runRightImage;
    this.posX += moveSpeed;
    if (this.posX > 816 - this.width) {
      this.posX = 816 - (int) this.width;
    }
  }

  private void moveUp() {
    playRunSound();
    applyScale();
    this.image = runRightImage;
    this.posY -= moveSpeed;
    // Prevent out of bounds
    if (this.posY < 0) {
      this.posY = 0;
    }
  }

  private void moveDown() {
    playRunSound();
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
        } else if (boundsObject instanceof Interactable) {
          Interactable interactable = (Interactable) boundsObject;
          interactable.touched();
          if (KeyState.getKeysPressed().contains(KeyCode.E)) {
            interactable.interact();
          }
        }
      } else if (boundsObject instanceof Interactable) {
        Interactable interactable = (Interactable) boundsObject;
        interactable.notTouched();
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

  private void playRunSound() {
    mediaPlayer.play();
  }

  public void stopRunSounds() {
    mediaPlayer.stop();
  }
}
