package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.StoneCarvingListener;

/**
 * This class represents the stone carving in the game. The player can interact with the stone
 * carving to find another section of the password.
 */
public class StoneCarving extends Interactable {

  private StoneCarvingListener listener;

  /**
   * This constructor creates a new stone carving for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the stone carving.
   * @param listener The listener to listen for interactions with the stone carving.
   */
  public StoneCarving(Rectangle rectangle, StoneCarvingListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the stone carving. */
  @Override
  public void interact() {
    this.listener.stoneCarvingInteracted();
  }

  /** This method is called when the player touches the stone carving. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    this.listener.stoneCarvingTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the stone carving. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    this.listener.stoneCaringUntouched();
    touched = false;
  }
}
