package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;

public abstract class Interactable extends BoundsObject {

  protected boolean touched = false;

  public Interactable(Rectangle rectangle) {
    super(rectangle);
  }

  public abstract void interact();

  public abstract void touched();

  public abstract void untouched();
}
