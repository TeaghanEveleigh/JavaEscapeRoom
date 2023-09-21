package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.ExitRoomDoorListener;

public class ExitRoomDoor extends Interactable {

  private ExitRoomDoorListener listener;

  public ExitRoomDoor(Rectangle rectangle, ExitRoomDoorListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.exitDoorInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.exitDoorTouched();
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    listener.exitDoorUntouched();
    touched = false;
  }
}
