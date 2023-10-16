package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.KeypadListener;

/**
 * This class represents a keypad in the game. The player can interact with the keypad to unlock the
 * exit door and win the game.
 */
public class Keypad extends Interactable {

  private KeypadListener listener;

  /**
   * This constructor creates a new keypad for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the keypad.
   * @param listener The listener to listen for interactions with the keypad.
   */
  public Keypad(Rectangle rectangle, KeypadListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the keypad. */
  @Override
  public void interact() {
    System.out.println("interact");
    listener.keypadInteracted();
  }

  /** This method is called when the player touches the keypad. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.keypadTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the keypad. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.keypadNotTouched();
    touched = false;
  }
}
