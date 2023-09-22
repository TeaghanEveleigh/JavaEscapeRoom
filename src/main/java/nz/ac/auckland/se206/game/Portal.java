package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GameController;

public class Portal extends Interactable {

  AppUi nextRoom;
  GameController originalController;

  public Portal(Rectangle rectangle, GameController originalController, AppUi nextRoom) {
    super(rectangle);
    this.originalController = originalController;
    this.nextRoom = nextRoom;
  }

  @Override
  public void interact() {
    return;
  }

  @Override
  public void touched() {
    if (touched) return;
    originalController.pauseRoom();
    App.switchScenes(nextRoom);
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    touched = false;
  }
}
