package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GameController;

public class Door extends Interactable {

  private AppUi nextRoot;
  private GameController leavingRoom;

  public Door(Rectangle rectangle, GameController leavingRoom, AppUi nextRoot) {
    super(rectangle);
    this.nextRoot = nextRoot;
  }

  @Override
  public void interact() {
    leavingRoom.pauseRoom();
    App.switchScenes(nextRoot);
  }

  @Override
  public void touched() {
 
  }

  @Override
  public void notTouched() {
   
  }
}
