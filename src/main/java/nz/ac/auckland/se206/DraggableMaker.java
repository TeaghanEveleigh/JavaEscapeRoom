package nz.ac.auckland.se206;

import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class DraggableMaker {

  private double mouseX;
  private double mouseY;
  private double originalHeight;
  private double originalWidth;
  private List<Circle> endpoints;

  public DraggableMaker(List<Circle> endpoints) {
    this.endpoints = endpoints;
  }

  public void makeDraggable(Rectangle rectangle) {
    originalWidth = rectangle.widthProperty().get();
    originalHeight = rectangle.heightProperty().get();

    rectangle.setOnMousePressed(
        e -> {
          mouseX = e.getX();
          mouseY = e.getY();
          originalWidth = rectangle.getWidth();
        });

    rectangle.setOnMouseDragged(
        e -> {
          rectangle
              .widthProperty()
              .set(
                  Math.sqrt(
                      Math.pow((originalWidth + e.getX() - mouseX), 2.0)
                          + Math.pow((originalHeight + e.getY() - mouseY), 2.0)));
          double deltaAngle = calculateAngle(e.getY() - mouseY, rectangle.getWidth());
          Rotate rotate = new Rotate(deltaAngle, 0, rectangle.getHeight() / 2);
          rectangle.getTransforms().add(rotate);
        });

    rectangle.setOnMouseReleased(
        e -> {
          for (Circle endpoint : endpoints) {
            // If the rectangle intersects with the endpoint
            if (!rectangle.getBoundsInParent().intersects(endpoint.getBoundsInParent())) {
              continue;
            } else {
              System.out.println(endpoint.getBoundsInParent());
              // Calculate the distance between the centre of the rectangle and the centre of the
              // endpoint
              double xDistance = endpoint.getLayoutX() - rectangle.getLayoutX();
              double yDistance =
                  endpoint.getLayoutY() - rectangle.getLayoutY() - originalHeight / 2;
              System.out.println(yDistance);

              // Set the rectangle's width to reach the centre of the endpoint
              double newWidth = Math.sqrt(Math.pow(xDistance, 2.0) + Math.pow(yDistance, 2.0));
              rectangle.setWidth(newWidth);
              // Calculate the angle between the rectangle and the endpoint
              double deltaAngle = calculateAngle(yDistance, xDistance);
              rectangle.getTransforms().clear();
              rectangle.getTransforms().add(new Rotate(deltaAngle, 0, rectangle.getHeight() / 2));
              break;
            }
          }
        });
  }

  public double calculateAngle(double opposite, double adjacent) {
    double tanAngle = opposite / adjacent;
    double angle = Math.atan(tanAngle);
    return Math.toDegrees(angle);
  }
}
