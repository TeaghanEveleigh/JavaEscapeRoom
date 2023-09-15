package nz.ac.auckland.se206;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class DraggableMaker {

  private double mouseX;
  private double mouseY;
  private double originalHeight;
  private double originalWidth;

  public void makeDraggable(Rectangle rectangle) {
    originalWidth = rectangle.widthProperty().get();
    originalHeight = rectangle.heightProperty().get();

    rectangle.setOnMousePressed(
        e -> {
          mouseX = e.getX();
          mouseY = e.getY();
          originalWidth = rectangle.widthProperty().get();
        });

    rectangle.setOnMouseDragged(
        e -> {
          rectangle
              .widthProperty()
              .set(
                  Math.sqrt(
                      Math.pow((originalWidth + e.getX() - mouseX), 2.0)
                          + Math.pow((originalHeight + e.getY() - mouseY), 2.0)));
          double deltaAngle = calculateRotationAngle(rectangle, e);
          Rotate rotate = new Rotate(deltaAngle, 0, rectangle.heightProperty().get() / 2);
          rectangle.getTransforms().add(rotate);
          System.out.println(deltaAngle);
        });
  }

  public double calculateRotationAngle(Rectangle rectangle, MouseEvent e) {
    double tanAngle = (e.getY() - mouseY) / (rectangle.widthProperty().get());
    double angle = Math.atan(tanAngle);
    return Math.toDegrees(angle);
  }
}
