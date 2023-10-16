package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.SecurityRoomDoorListener;

/**
 * This class represents the security door in the game. The player can interact with the security
 * door to enter the security room.
 */
public class SecurityRoomDoor extends Interactable {

  private SecurityRoomDoorListener listener;

  /**
   * This constructor creates a new security door for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the security door.
   * @param listener The listener to listen for interactions with the security door.
   */
  public SecurityRoomDoor(Rectangle rectangle, SecurityRoomDoorListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  /** This method is called when the player interacts with the security door. */
  @Override
  public void interact() {
    listener.onSecurityDoorInteracted();
  }

  /** This method is called when the player touches the security door. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.onSecurityDoorTouched();
    touched = true;
  }

  /** This method is called when the player stops touching the security door. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.onSecurityDoorNotTouched();
    touched = false;
  }
}
