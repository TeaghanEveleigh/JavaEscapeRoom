package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.ComputerListener;

/**
 * This class represents a computer in the game. The player can interact with the computer to
 * complete the game.
 */
public class Computer extends Interactable {

  private ComputerListener listener;

  /**
   * This constructor creates a new computer for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the computer.
   * @param listener The listener to listen for interactions with the computer.
   */
  public Computer(Rectangle rectangle, ComputerListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the computer. */
  @Override
  public void interact() {
    listener.computerInteracted();
  }

  /** This method is called when the player touches the computer. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.computerTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the computer. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.computerNotTouched();
    touched = false;
  }
}
