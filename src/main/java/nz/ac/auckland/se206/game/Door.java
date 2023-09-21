package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Door extends Interactable {

  private AppUi nextRoot;

  public Door(Rectangle rectangle, AppUi nextRoot) {
    super(rectangle);
    this.nextRoot = nextRoot;
  }

  @Override
  public void interact() {
    App.switchScenes(nextRoot);
  }

  @Override
  public void touched() {
    return;
  }

  @Override
  public void untouched() {
    return;
  }
}
