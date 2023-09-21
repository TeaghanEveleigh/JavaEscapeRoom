package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.WiresListener;

public class Wires extends Interactable {

  private WiresListener listener;

  public Wires(Rectangle rectangle, WiresListener listener) {
    super(rectangle);

    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.wiresInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.wiresTouched();
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    listener.wiresUntouched();
    touched = false;
  }
}
