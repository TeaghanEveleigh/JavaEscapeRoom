package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.LeftDinosaurListener;

/**
 * This class represents a left dinosaur in the game. The player can interact with the left dinosaur
 * to find one of the dates needed for the password.
 */
public class LeftDinosaur extends Interactable {

  private LeftDinosaurListener listener;

  /**
   * This constructor creates a new left dinosaur for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the left dinosaur.
   * @param listener The listener to listen for interactions with the left dinosaur.
   */
  public LeftDinosaur(Rectangle rectangle, LeftDinosaurListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the left dinosaur. */
  @Override
  public void interact() {
    listener.leftDinosaurInteracted();
  }

  /** This method is called when the player touches the left dinosaur. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.leftDinosaurTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the left dinosaur. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.leftDinosaurUntouched();
    touched = false;
  }
}
