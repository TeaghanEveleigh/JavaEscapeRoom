package nz.ac.auckland.se206.game;

import java.util.ArrayList;
import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.KeyState;

/**
 * This class represents the player in the game. Each room has a player to control and they can move
 * them using the arrow keys or the WASD keys.
 */
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

  /**
   * This constructor creates a new player for the user to control.
   *
   * @param width The width of the player.
   * @param height The height of the player.
   * @param posX The x position of the player.
   * @param posY The y position of the player.
   */
  public Player(int width, int height, int posX, int posY) {
    super(imagePath, width, height, posX, posY);
    baseHeight = height;
    baseWidth = width;
    oldX = posX;
    oldY = posY;
    boundingBoxes = new ArrayList<BoundsObject>();
    // Load image for the player when they're standing still
    idleImage =
        new Image(
            getClass().getResource("/images/player-idle.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
    // Load image for the player when they're running to the right
    runRightImage =
        new Image(
            getClass().getResource("/images/player-run-right.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
    // Load image for the player when they're running to the left
    runLeftImage =
        new Image(
            getClass().getResource("/images/player-run-left.png").toExternalForm(),
            width,
            height,
            false,
            false,
            false);
    this.width = width;
    // Load sound for the player when they're running
    String runSound = getClass().getResource("/sounds/player-walk-sound.mp3").toExternalForm();
    this.mediaPlayer = new MediaPlayer(new Media(runSound));
    this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
  }

  /** This method updates the movement of the player so that they can move around. */
  public void updateMovement() {
    // Get set of all keys current pressed - polling
    Set<KeyCode> keysPressed = KeyState.getKeysPressed();

    // Run movement code for WASD
    for (KeyCode key : keysPressed) {
      switch (key) {
          // If the player wants to move left
        case A:
        case LEFT:
          moveLeft();
          break;
          // If the player wants to move right
        case D:
        case RIGHT:
          moveRight();
          break;
          // If the player wants to move up
        case W:
        case UP:
          moveUp();
          break;
          // If the player wants to move down
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

  /** This method applies a scale to the player. */
  private void applyScale() {
    this.width = (int) (baseWidth * 0.9);
    this.height = (int) (baseHeight * 0.9);
  }

  /** This method resets the scale of the player. */
  private void resetScale() {
    this.width = baseWidth;
    this.height = baseHeight;
  }

  /** This method moves the player to the left left. */
  private void moveLeft() {
    playRunSound();
    applyScale();
    this.image = runLeftImage;
    this.posX -= moveSpeed; // Position depends on the speed of movement
    // Prevent out of bounds
    if (this.posX < 0) {
      this.posX = 0;
    }
  }

  /** This method moves the player to the right. */
  private void moveRight() {
    playRunSound();
    applyScale();
    this.image = runRightImage;
    this.posX += moveSpeed; // Position depends on the speed of movement
    // Prevent out of bounds
    if (this.posX > 816 - this.width) {
      this.posX = 816 - (int) this.width;
    }
  }

  /** This method moves the player up. */
  private void moveUp() {
    playRunSound();
    applyScale();
    this.image = runRightImage;
    this.posY -= moveSpeed; // Position depends on the speed of movement
    // Prevent out of bounds
    if (this.posY < 0) {
      this.posY = 0;
    }
  }

  /** This method moves the player down. */
  private void moveDown() {
    playRunSound();
    applyScale();
    this.image = runLeftImage;
    this.posY += moveSpeed; // Position depends on the speed of movement
    // Prevent out of bounds
    if (this.posY > 585 - this.height) {
      this.posY = 585 - (int) this.height;
    }
  }

  /**
   * This method sets the bounding boxes for the player.
   *
   * @param boundingBoxes The bounding boxes to set.
   */
  public void setBoundingBoxes(ArrayList<BoundsObject> boundingBoxes) {
    this.boundingBoxes = boundingBoxes;
  }

  /** This method handles collisions between the player and other objects. */
  private void handleCollisions() {
    for (BoundsObject boundsObject : this.boundingBoxes) {
      if (this.getBounds()
          .intersects(boundsObject.getBounds())) { // If the player is colliding with an object
        if (boundsObject instanceof SolidBox) {
          preventBoundingBoxCollision();
          return;
        } else if (boundsObject instanceof Interactable) { // If the object is interactable
          Interactable interactable = (Interactable) boundsObject;
          interactable.touched(); // Shpw the label for the interactable
          if (KeyState.getKeysPressed().contains(KeyCode.E)) {
            interactable.interact(); // Interact with the object
          }
        }
      } else if (boundsObject instanceof Interactable) {
        Interactable interactable = (Interactable) boundsObject;
        interactable.notTouched(); // Hide the label for the interactable
      }
    }

    oldX = posX;
    oldY = posY;
  }

  /** This method prevents the player from moving if they are colliding with a solid object. */
  private void preventBoundingBoxCollision() {
    this.posX = oldX;
    this.posY = oldY;
  }

  /**
   * This method returns the bounds of the player.
   *
   * @return The bounds of the player.
   */
  public Rectangle2D getBounds() {
    return new Rectangle2D(posX, posY, width, height);
  }

  /** This method sets the x position of the player. */
  @Override
  public void setPosX(int posX) {
    super.setPosX(posX);
    this.oldX = posX;
  }

  /** This method sets the y position of the player. */
  @Override
  public void setPosY(int posY) {
    super.setPosY(posY);
    this.oldY = posY;
  }

  /** This method plays the run sounds for the player. */
  private void playRunSound() {
    mediaPlayer.play();
  }

  /** This method stops the run sounds from playing. */
  public void stopRunSounds() {
    mediaPlayer.stop();
  }
}
