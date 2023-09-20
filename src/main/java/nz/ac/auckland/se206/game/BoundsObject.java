package nz.ac.auckland.se206.game;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

public abstract class BoundsObject {
  protected Rectangle rectangle;

  public BoundsObject(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  public Rectangle2D getBounds() {
    Bounds bounds = this.rectangle.localToScene(this.rectangle.getBoundsInLocal());

    return new Rectangle2D(
        bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
  }
}
