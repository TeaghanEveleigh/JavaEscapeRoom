package nz.ac.auckland.se206.game;

import java.io.IOException;
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
    try {
      App.setRoot(nextRoot);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
