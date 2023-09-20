package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;

public abstract class Interactable extends BoundsObject {

  public Interactable(Rectangle rectangle) {
    super(rectangle);
  }

  public abstract void interact();
}
