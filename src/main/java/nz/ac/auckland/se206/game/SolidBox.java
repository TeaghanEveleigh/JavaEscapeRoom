package nz.ac.auckland.se206.game;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

public class SolidBox {
  Rectangle rectangle;

  public SolidBox(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  public Rectangle2D getBounds() {
    Bounds bounds = this.rectangle.localToScene(this.rectangle.getBoundsInLocal());
    Rectangle2D rectangle =
        new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());

    return new Rectangle2D(
        bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
  }
}
