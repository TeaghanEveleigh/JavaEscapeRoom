package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.RightDinosaurListener;

public class RightDinosaur extends Interactable {

  private RightDinosaurListener listener;

  public RightDinosaur(Rectangle rectangle, RightDinosaurListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.rightDinosaurInteracted();
  }

  @Override
  public void touched() {
    if (touched) {
      return;
    }
    listener.rightDinosaurTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    listener.rightDinosaurUntouched();
    touched = false;
  }
}
