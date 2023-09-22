package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SecurityRoomDoorListener;

public class SecurityRoomDoor extends Interactable {

  private SecurityRoomDoorListener listener;

  public SecurityRoomDoor(Rectangle rectangle, SecurityRoomDoorListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.securityDoorInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.securityDoorTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    listener.securityDoorUntouched();
    touched = false;
  }
}
