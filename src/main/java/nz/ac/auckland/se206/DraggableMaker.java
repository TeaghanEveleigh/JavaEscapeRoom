package nz.ac.auckland.se206;

import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

public class DraggableMaker {

  // Fields
  private double mouseX;
  private double mouseY;
  private double originalHeight;
  private double originalWidth;
  private List<Circle> endpoints;
  private double currentX;
  private double currentY;
  private double leftXBound;
  private double rightXBound;
  private double topYBound;
  private double bottomYBound;
  private boolean isGreenCorrect = false;
  private boolean isRedCorrect = false;
  private boolean isBlueCorrect = false;
  private boolean isYellowCorrect = false;

  // Constructor
  public DraggableMaker(List<Circle> endpoints, Rectangle controlPanel) {
    this.endpoints = endpoints;
    leftXBound = controlPanel.getLayoutX();
    rightXBound = controlPanel.getLayoutX() + controlPanel.getWidth();
    topYBound = controlPanel.getLayoutY();
    bottomYBound = controlPanel.getLayoutY() + controlPanel.getHeight();
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
          currentX = e.getX();
          currentY = e.getY();

          rectangle
              .widthProperty()
              .set(
                  Math.sqrt(
                      Math.pow((originalWidth + currentX - mouseX), 2.0)
                          + Math.pow((originalHeight + currentY - mouseY), 2.0)));
          double deltaAngle = calculateAngle(currentY - mouseY, rectangle.getWidth());
          Rotate rotate = new Rotate(deltaAngle, 0, rectangle.getHeight() / 2);
          rectangle.getTransforms().add(rotate);
        });

    rectangle.setOnMouseReleased(
        e -> {
          for (Circle endpoint : endpoints) {
            // If the end of the rectangle intersects with the endpoint
            Shape intersect = Shape.intersect(rectangle, endpoint);
            if (intersect.getBoundsInLocal().getWidth() != -1.0) {
              // Calculate the distance between the centre of the rectangle and the centre of the
              // endpoint
              double xDistance = endpoint.getLayoutX() - rectangle.getLayoutX();
              double yDistance =
                  endpoint.getLayoutY() - rectangle.getLayoutY() - originalHeight / 2;

              // Set the rectangle's width to reach the centre of the endpoint
              double newWidth = Math.sqrt(Math.pow(xDistance, 2.0) + Math.pow(yDistance, 2.0));
              rectangle.setWidth(newWidth);

              // Calculate the angle between the rectangle and the endpoint
              double deltaAngle = calculateAngle(yDistance, xDistance);
              rectangle.getTransforms().clear();

              // Rotate the rectangle to the correct angle
              rectangle.getTransforms().add(new Rotate(deltaAngle, 0, rectangle.getHeight() / 2));

              // Checks if the wire has matched to the right endpoint
              if ((rectangle.getId().equals("greenWire"))
                  && (endpoint.getId().equals("twoCircle"))) {
                isGreenCorrect = true;
              } else if ((rectangle.getId().equals("redWire"))
                  && (endpoint.getId().equals("oneCircle"))) {
                isRedCorrect = true;
              } else if ((rectangle.getId().equals("blueWire"))
                  && (endpoint.getId().equals("fourCircle"))) {
                isBlueCorrect = true;
              } else if ((rectangle.getId().equals("yellowWire"))
                  && (endpoint.getId().equals("threeCircle"))) {
                isYellowCorrect = true;
              }

              // Checks if all the wires have matched to the right endpoints
              if (isGreenCorrect && isRedCorrect && isBlueCorrect && isYellowCorrect) {
                System.out.println("You win!");
                System.exit(0);
              }
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

  public boolean isWithinBounds(double x, double y) {
    return x >= leftXBound && x <= rightXBound && y >= topYBound && y <= bottomYBound;
  }
}
