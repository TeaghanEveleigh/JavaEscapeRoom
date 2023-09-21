package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.LeftDinosaurListener;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.game.LeftDinosaur;
import nz.ac.auckland.se206.game.Portal;
import nz.ac.auckland.se206.game.SolidBox;

public class LaserRoomController extends GameController implements LeftDinosaurListener {
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
  @FXML private Rectangle leftDinosaurBounds;
  @FXML private Rectangle rightDinosaurBounds;
  @FXML private Rectangle objectBounds;

  @Override
  public void initialize() {
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBoxTwo));
    boundsObjects.add(new Portal(doorRectangle, AppUi.EXIT_ROOM));
    boundsObjects.add(new LeftDinosaur(leftDinosaurBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(472);
  }

  @FXML
  private void disableLasers() {
    laser1.toBack();
    laser2.toBack();
    laser3.toBack();
    laserShadow1.toBack();
    laserShadow2.toBack();
    laserShadow3.toBack();
    itemLabel.toFront();
  }

  @FXML
  private void itemLabelShow() {
    itemLabel.setOpacity(1);
  }

  @FXML
  private void itemLabelHide() {
    itemLabel.setOpacity(0);
  }

  @FXML
  private void stealItem() {
    object.toBack();
    itemLabel.toBack();
  }

  @FXML
  private void showDinoLabel1() {
    dinoLabel1.setOpacity(1);
  }

  @FXML
  private void hideDinoLabel1() {
    dinoLabel1.setOpacity(0);
  }

  @Override
  public void leftDinosaurInteracted() {
    return;
  }

  @Override
  public void leftDinosaurTouched() {
    showDinoLabel1();
  }

  @Override
  public void leftDinosaurUntouched() {
    hideDinoLabel1();
  }
}
