package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

/*
 * This is the controller class for the wires game window. The wires are made draggable and are checked if they're connected to the right endpoints.
 */
public class WiresController implements Initializable {

  // Buttons
  @FXML private Button backButton;

  // Opacity rectangle
  @FXML private Rectangle opacityRectangle;

  // Win label
  @FXML private Label winLabel;

  // Wires
  @FXML private Rectangle greenWire;
  @FXML private Rectangle redWire;
  @FXML private Rectangle yellowWire;
  @FXML private Rectangle blueWire;
  @FXML private Circle greenCircle;
  @FXML private Circle redCircle;
  @FXML private Circle yellowCircle;
  @FXML private Circle blueCircle;

  // Endpoints
  @FXML private Circle oneCircle;
  @FXML private Circle twoCircle;
  @FXML private Circle threeCircle;
  @FXML private Circle fourCircle;
  private List<Circle> endpoints;

  // Fields
  private double mouseX;
  private double mouseY;
  private double originalHeight;
  private double originalWidth;
  private double currentX;
  private double currentY;
  private boolean isGreenCorrect = false;
  private boolean isRedCorrect = false;
  private boolean isBlueCorrect = false;
  private boolean isYellowCorrect = false;

  /**
   * This method initialises the wires game window. The wires game allows the user to drag wires
   * around a control panel and try and match them to the correct endpoints
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    winLabel.setVisible(false);
    endpoints = List.of(oneCircle, twoCircle, threeCircle, fourCircle);
    makeDraggable(blueWire);
    makeDraggable(greenWire);
    makeDraggable(redWire);
    makeDraggable(yellowWire);
  }

  /**
   * This method returns the user to the main menu.
   *
   * @throws IOException
   */
  @FXML
  public void onBackPressed() throws IOException {
    App.setRoot(AppUi.MAIN_MENU);
  }

  /**
   * This method makes the rectangle draggable.
   *
   * @param rectangle The rectangle to be made draggable
   */
  public void makeDraggable(Rectangle rectangle) {

    // Initialise the original width and height of the rectangle
    originalWidth = rectangle.widthProperty().get();
    originalHeight = rectangle.heightProperty().get();

    // When the mouse is pressed on the rectangle
    rectangle.setOnMousePressed(
        e -> {
          // Set the mouse coordinates and the original width of the rectangle
          mouseX = e.getX();
          mouseY = e.getY();
          originalWidth = rectangle.getWidth();
        });

    // When the mouse is dragged
    rectangle.setOnMouseDragged(
        e -> {
          // Set the current coordinates of the mouse
          currentX = e.getX();
          currentY = e.getY();

          // Set the width of the rectangle to the distance between the mouse and the rectangle
          rectangle
              .widthProperty()
              .set(
                  Math.sqrt(
                      Math.pow((originalWidth + currentX - mouseX), 2.0)
                          + Math.pow((originalHeight + currentY - mouseY), 2.0)));

          // Calculate the angle between the rectangle and the mouse and set it
          double deltaAngle = calculateAngle(currentY - mouseY, rectangle.getWidth());
          Rotate rotate = new Rotate(deltaAngle, 0, rectangle.getHeight() / 2);
          rectangle.getTransforms().add(rotate);
        });

    // When the mouse is released
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

              // Checks if the green wire has matched to the right endpoint
              if (rectangle.getId().equals("greenWire")) {
                if (endpoint.getId().equals("twoCircle")) {
                  isGreenCorrect = true;
                } else {
                  isGreenCorrect = false;
                }
              }

              // Checks if the red wire has matched to the right endpoint
              if (rectangle.getId().equals("redWire")) {
                if (endpoint.getId().equals("oneCircle")) {
                  isRedCorrect = true;
                } else {
                  isRedCorrect = false;
                }
              }

              // Checks if the blue wire has matched to the right endpoint
              if (rectangle.getId().equals("blueWire")) {
                if (endpoint.getId().equals("fourCircle")) {
                  isBlueCorrect = true;
                } else {
                  isBlueCorrect = false;
                }
              }

              // Checks if the yellow wire has matched to the right endpoint
              if (rectangle.getId().equals("yellowWire")) {
                if (endpoint.getId().equals("threeCircle")) {
                  isYellowCorrect = true;
                } else {
                  isYellowCorrect = false;
                }
              }

              // Checks if all the wires have matched to the right endpoints
              if (isGreenCorrect && isRedCorrect && isBlueCorrect && isYellowCorrect) {

                // Displays the win message and blurs the screen
                opacityRectangle.toFront();
                opacityRectangle.setOpacity(0.9);
                winLabel.toFront();
                winLabel.setVisible(true);
                backButton.toFront();
              }
              break;
            }
          }
        });
  }

  /**
   * This method calculates the angle between the rectangle and the mouse.
   *
   * @param opposite The opposite side of the triangle
   * @param adjacent The adjacent side of the triangle
   */
  public double calculateAngle(double opposite, double adjacent) {
    double tanAngle = opposite / adjacent;
    double angle = Math.atan(tanAngle);
    return Math.toDegrees(angle);
  }
}
