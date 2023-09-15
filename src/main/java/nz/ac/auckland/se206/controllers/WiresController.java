package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.DraggableMaker;

public class WiresController implements Initializable {

  // Buttons
  @FXML private Button backButton;

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

  // DraggableMaker
  private DraggableMaker draggableMaker;

  /*
   * This method initialises the wires game window. It makes all the wires draggable and defines the coordinates of the corners of the control panel.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    endpoints = List.of(oneCircle, twoCircle, threeCircle, fourCircle);
    draggableMaker = new DraggableMaker(endpoints);
    draggableMaker.makeDraggable(blueWire);
    draggableMaker.makeDraggable(greenWire);
    draggableMaker.makeDraggable(redWire);
    draggableMaker.makeDraggable(yellowWire);
  }
}
