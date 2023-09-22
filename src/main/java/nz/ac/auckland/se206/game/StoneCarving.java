package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.StoneCarvingListener;

public class StoneCarving extends Interactable {

  private StoneCarvingListener listener;

  public StoneCarving(Rectangle rectangle, StoneCarvingListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    this.listener.stoneCarvingInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    this.listener.stoneCarvingTouched();
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    this.listener.stoneCaringUntouched();
    touched = false;
  }
}
