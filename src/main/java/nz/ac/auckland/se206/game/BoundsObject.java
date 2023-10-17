package nz.ac.auckland.se206.game;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

/** This class represents an object that has bounds. This is used for collision detection. */
public abstract class BoundsObject {
  protected Rectangle rectangle;

  /**
   * This constructor creates a new BoundsObject to define the collision bounds of an object.
   *
   * @param rectangle the rectangle to define the bounds of an object.
   */
  public BoundsObject(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  /**
   * This method is used to get the bounds of an object.
   *
   * @return the bounds of an object.
   */
  public Rectangle2D getBounds() {
    Bounds bounds = this.rectangle.localToScene(this.rectangle.getBoundsInLocal());

    return new Rectangle2D(
        bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
  }
}
