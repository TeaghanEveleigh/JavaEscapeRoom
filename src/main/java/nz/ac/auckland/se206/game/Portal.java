package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Portal extends Interactable {

  AppUi nextRoom;

  public Portal(Rectangle rectangle, AppUi nextRoom) {
    super(rectangle);
    this.nextRoom = nextRoom;
  }

  @Override
  public void interact() {
    return;
  }

  @Override
  public void touched() {
    if (touched) return;
    App.switchScenes(nextRoom);
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    touched = false;
  }
}
