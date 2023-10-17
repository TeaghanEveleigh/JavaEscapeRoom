package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.RightDinosaurListener;

/**
 * This class represents a right dinosaur in the game. The player can interact with the right
 * dinosaur to find a section of the password.
 */
public class RightDinosaur extends Interactable {

  private RightDinosaurListener listener;

  /**
   * This constructor creates a new right dinosaur for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the right dinosaur.
   * @param listener The listener to listen for interactions with the right dinosaur.
   */
  public RightDinosaur(Rectangle rectangle, RightDinosaurListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the right dinosaur. */
  @Override
  public void interact() {
    listener.rightDinosaurInteracted();
  }

  /** This method is called when the player touches the right dinosaur. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.rightDinosaurTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the right dinosaur. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.rightDinosaurUntouched();
    touched = false;
  }
}
