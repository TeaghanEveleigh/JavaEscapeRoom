package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.WiresListener;

/**
 * This class represents the wires in the game. The player can interact with the wires to access the
 * wires minigame to disable the lasers.
 */
public class Wires extends Interactable {

  private WiresListener listener;

  /**
   * This constructor creates a new wires for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the wires.
   * @param listener The listener to listen for interactions with the wires.
   */
  public Wires(Rectangle rectangle, WiresListener listener) {
    super(rectangle);

    this.listener = listener;
  }

  /** This method is called when the player interacts with the wires. */
  @Override
  public void interact() {
    listener.wiresInteracted();
  }

  /** This method is called when the player touches the wires. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.wiresTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the wires. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.wiresUntouched();
    touched = false;
  }
}
