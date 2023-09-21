package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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

  // Control panel
  @FXML private Rectangle controlPanelRectangle;

  // Background
  @FXML private Rectangle backgroundRectangle;

  // Win label
  @FXML private Label winLabel;

  // Number labels
  @FXML private Label oneLabel;
  @FXML private Label twoLabel;
  @FXML private Label threeLabel;
  @FXML private Label fourLabel;

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
  @FXML private Circle endpointoneCircle;
  @FXML private Circle endpointtwoCircle;
  @FXML private Circle endpointthreeCircle;
  @FXML private Circle endpointfourCircle;
  private List<Circle> endpoints;

  // Fields
  private double mouseX;
  private double mouseY;
  private double originalHeight;
  private double originalWidth;
  private double currentX;
  private double currentY;

  // Booleans
  private boolean isGreenCorrect = false;
  private boolean isRedCorrect = false;
  private boolean isBlueCorrect = false;
  private boolean isYellowCorrect = false;
  boolean[] isEndpointConnected = {false, false, false, false};

  // Colour of endpoints
  private Color endpointColour = Color.rgb(85, 96, 107);

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
    endpoints =
        List.of(endpointoneCircle, endpointtwoCircle, endpointthreeCircle, endpointfourCircle);
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
    App.switchScenes(AppUi.KEYPAD_ROOM);
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
          rectangle.toFront();

          // Changes the colour of the endpoint and respective label to grey if the wire is
          // disconnected
          for (Circle endpoint : endpoints) {
            if (endpoint.getFill().equals(rectangle.getFill())) {
              endpoint.setFill(endpointColour);
              if (endpoint.getId().equals("endpointtwoCircle")) {
                twoLabel.setTextFill(endpointColour);
                isEndpointConnected[1] = false;
              } else if (endpoint.getId().equals("endpointoneCircle")) {
                oneLabel.setTextFill(endpointColour);
                isEndpointConnected[0] = false;
              } else if (endpoint.getId().equals("endpointfourCircle")) {
                fourLabel.setTextFill(endpointColour);
                isEndpointConnected[3] = false;
              } else if (endpoint.getId().equals("endpointthreeCircle")) {
                threeLabel.setTextFill(endpointColour);
                isEndpointConnected[2] = false;
              }
            }
          }
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
          for (int i = 0; i < endpoints.size(); i++) {
            // If the end of the rectangle intersects with the endpoint
            Shape intersect = Shape.intersect(rectangle, endpoints.get(i));
            if ((intersect.getBoundsInLocal().getWidth() != -1.0) && (!isEndpointConnected[i])) {
              // Calculate the distance between the centre of the rectangle and the centre of the
              // endpoint
              isEndpointConnected[i] = true;
              endpoints.get(i).setFill(rectangle.getFill());
              double xDistance = endpoints.get(i).getLayoutX() - rectangle.getLayoutX();
              double yDistance =
                  endpoints.get(i).getLayoutY() - rectangle.getLayoutY() - originalHeight / 2;

              // Set the rectangle's width to reach the centre of the endpoint
              double newWidth = Math.sqrt(Math.pow(xDistance, 2.0) + Math.pow(yDistance, 2.0));
              rectangle.setWidth(newWidth);

              // Calculate the angle between the rectangle and the endpoint
              double deltaAngle = calculateAngle(yDistance, xDistance);
              rectangle.getTransforms().clear();

              // Rotate the rectangle to the correct angle
              rectangle.getTransforms().add(new Rotate(deltaAngle, 0, rectangle.getHeight() / 2));

              // Checks if the green wire has matched to the right endpoint
              endpoints.get(i).toFront();
              if (endpoints.get(i).getId().equals("endpointtwoCircle")) {
                twoLabel.setTextFill(rectangle.getFill());
                if (rectangle.getId().equals("greenWire")) {
                  isGreenCorrect = true;
                } else {
                  isGreenCorrect = false;
                }
              }

              // Checks if the red wire has matched to the right endpoint
              if (endpoints.get(i).getId().equals("endpointoneCircle")) {
                oneLabel.setTextFill(rectangle.getFill());
                if (rectangle.getId().equals("redWire")) {
                  isRedCorrect = true;
                } else {
                  isRedCorrect = false;
                }
              }

              // Checks if the blue wire has matched to the right endpoint
              if (endpoints.get(i).getId().equals("endpointfourCircle")) {
                fourLabel.setTextFill(rectangle.getFill());
                if (rectangle.getId().equals("blueWire")) {
                  isBlueCorrect = true;
                } else {
                  isBlueCorrect = false;
                }
              }

              // Checks if the yellow wire has matched to the right endpoint
              if (endpoints.get(i).getId().equals("endpointthreeCircle")) {
                threeLabel.setTextFill(rectangle.getFill());
                if (rectangle.getId().equals("yellowWire")) {
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
