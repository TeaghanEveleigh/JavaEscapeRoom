package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.Door;
import nz.ac.auckland.se206.game.SolidBox;

public class LaserRoomController extends GameController {
  @FXML private Label dinoLabel1;
  @FXML private ImageView object;
  @FXML private ImageView laserShadow1;
  @FXML private ImageView laserShadow2;
  @FXML private ImageView laserShadow3;
  @FXML private ImageView laser1;
  @FXML private ImageView laser2;
  @FXML private ImageView laser3;
  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle boundingBoxTwo;
  @FXML private Rectangle doorRectangle;
  @FXML private Label itemLabel;

  @Override
  public void initialize() {
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBoxTwo));
    boundsObjects.add(new Door(doorRectangle, this, AppUi.MAIN_MENU));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(472);
    disableLasers();
    stealItem();
  }
  @FXML private void disableLasers(){
    laser1.toBack();
    laser2.toBack();
    laser3.toBack();
    laserShadow1.toBack();
    laserShadow2.toBack();
    laserShadow3.toBack();
    itemLabel.toFront();
  }
  @FXML private void itemLabelShow(){
    itemLabel.setOpacity(1);

  }
  @FXML private void itemLabelHide(){
    itemLabel.setOpacity(0);
  }
  @FXML private void stealItem(){
      object.toBack();
      itemLabel.toBack();
  }
  @FXML private void showDinoLabel1(){
    dinoLabel1.setOpacity(1);
  }
  @FXML private void hideDinoLabel1(){
    dinoLabel1.setOpacity(0);
    
  }
}
