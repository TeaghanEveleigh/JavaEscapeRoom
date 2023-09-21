package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.Door;
import nz.ac.auckland.se206.game.SolidBox;

public class LaserRoomController extends GameController {
  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle boundingBoxTwo;
  @FXML private Rectangle doorRectangle;

  @Override
  public void initialize() {
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBoxTwo));
    boundsObjects.add(new Door(doorRectangle, this, AppUi.MAIN_MENU));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(472);
  }
}
