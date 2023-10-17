package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.SafeListener;

/**
 * This class represents a safe in the game. The player can interact with the safe to get the
 * keycode needed to unlock the exit door.
 */
public class Safe extends Interactable {

  private SafeListener listener;

  /**
   * This constructor creates a new safe for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the safe.
   * @param listener The listener to listen for interactions with the safe.
   */
  public Safe(Rectangle rectangle, SafeListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the safe. */
  @Override
  public void interact() {
    listener.safeInteracted();
  }

  /** This method is called when the player touches the safe. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.safeTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the safe. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.safeNotTouched();
    touched = false;
  }
}
