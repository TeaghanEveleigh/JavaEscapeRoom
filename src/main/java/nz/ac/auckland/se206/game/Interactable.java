package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;

/**
 * This class represents an object that can be interacted with. Each interactable object in the game
 * extends this class.
 */
public abstract class Interactable extends BoundsObject {

  protected boolean touched = false;

  /**
   * This constructor creates a new interactable object for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the interactable object.
   */
  public Interactable(Rectangle rectangle) {
    super(rectangle);
  }

  /** This method is called when the player interacts with the interactable object. */
  public abstract void interact();

  /** This method is called when the player touches the interactable object. */
  public abstract void touched();

  /** This method is called when the player stops touching the interactable object. */
  public abstract void notTouched();
}
