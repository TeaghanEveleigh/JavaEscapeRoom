package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;

/** This class represents an object that has bounds. This is used for collision detection. */
public class SolidBox extends BoundsObject {

  /**
   * This constructor creates a new BoundsObject to define the collision bounds of an object.
   *
   * @param rectangle the rectangle to define the bounds of an object.
   */
  public SolidBox(Rectangle rectangle) {
    super(rectangle);
  }
}
