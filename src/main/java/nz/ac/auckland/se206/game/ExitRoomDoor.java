package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.ExitRoomDoorListener;

/**
 * This class represents the exit door in the game. The player can interact with the exit door to
 * exit the game.
 */
public class ExitRoomDoor extends Interactable {

  private ExitRoomDoorListener listener;

  /**
   * This constructor creates a new exit door for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the exit door.
   * @param listener The listener to listen for interactions with the exit door.
   */
  public ExitRoomDoor(Rectangle rectangle, ExitRoomDoorListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the exit door. */
  @Override
  public void interact() {
    listener.exitDoorInteracted();
  }

  /** This method is called when the player touches the exit door. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.exitDoorTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the exit door. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.exitDoorUntouched();
    touched = false;
  }
}
