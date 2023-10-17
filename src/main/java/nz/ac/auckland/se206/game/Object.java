package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.ObjectListener;

/** This class represents an object in the game. Each object can be interacted with. */
public class Object extends Interactable {

  private ObjectListener listener;

  /**
   * This constructor creates a new object for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the object.
   * @param listener The listener to listen for interactions with the object.
   */
  public Object(Rectangle rectangle, ObjectListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the object. */
  @Override
  public void interact() {
    listener.objectInteracted();
  }

  /** This method is called when the player touches the object. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.objectTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the object. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.objectUntouched();
    touched = false;
  }
}
