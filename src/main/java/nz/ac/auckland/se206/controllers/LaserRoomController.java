package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.game.SolidBox;

public class LaserRoomController extends GameController {
  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle boundingBoxTwo;

  @Override
  public void initialize() {
    super.initialize();
    solidBoxes.add(new SolidBox(boundingBoxOne));
    solidBoxes.add(new SolidBox(boundingBoxTwo));
    this.player.setBoundingBoxes(solidBoxes);
    this.player.setPosX(54);
    this.player.setPosY(472);
  }
}
